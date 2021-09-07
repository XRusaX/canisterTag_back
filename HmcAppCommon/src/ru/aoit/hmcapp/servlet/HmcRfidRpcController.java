package ru.aoit.hmcapp.servlet;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.aoit.appcommon.AuthImpl;
import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.ThreadLocalRequest;
import ru.aoit.appcommon.connection.ConnectionStatusModule;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.appcommon.rpc.RpcController;
import ru.aoit.appcommon.shared.auth.AuthUtils;
import ru.aoit.appcommon.shared.auth.UserData;
import ru.aoit.appcommon.shared.connection.ConnectionStatus.ConnectionType;
import ru.aoit.hmc.rfid.rpcdata.User;
import ru.aoit.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmc.rfid.ruslandata.RfidDataUtils;
import ru.aoit.hmcdb.shared.Agent;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Permissions;
import ru.aoit.hmcdb.shared.rfid.Quota;
import ru.aoit.hmcdb.shared.rfid.RfidLabel;
import ru.nppcrts.common.rsa.Sig;
import ru.nppcrts.common.shared.Severity;

@RestController
@RequestMapping(HmcRfidRpcInterface.servletPath)
public class HmcRfidRpcController extends RpcController implements HmcRfidRpcInterface {

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@Autowired
	private Sig sig;

	@Autowired
	private Database2 database;

	@Autowired
	private AuthImpl authComponent;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private ConnectionStatusModule connectionStatusModule;

	@Override
	public void tagWriteDone(String uid) {
		msgLogger.add(authComponent.getUserName(), Severity.INFO, "Метка " + uid + " записана");
	}

	@Override
	public synchronized List<RfidData> getSigs(String agentName, int canisterVolume) throws Exception {
		authComponent.checkPermissions(Permissions.PERMISSION_WRITE_RFID);
		try {
			List<RfidData> sigs = _getSigs(agentName, canisterVolume);
			msgLogger.add(authComponent.getUserName(), Severity.INFO,
					"Выделены метки " + agentName + "(" + canisterVolume + ")");
			return sigs;
		} catch (Exception e) {
			msgLogger.add(authComponent.getUserName(), Severity.ERROR,
					"Ошибка пр выделении меток " + agentName + "(" + canisterVolume + ")", e.toString());
			throw e;
		}
	}

	private synchronized List<RfidData> _getSigs(String agentName, int canisterVolume) throws Exception {

		UserData user = authComponent.getUser();

		List<RfidData> list = new ArrayList<>();

		database.execVoid(conn -> {

			Company company = conn.find(Company.class, user.company);

			if (company.rfidBlockSize <= 0)
				throw new RuntimeException("Размер блока меток <= 0");

			Agent agent = conn.createQuery("select a from Agent a where a.name=:name", Agent.class)
					.setParameter("name", agentName).getResultStream().findFirst().orElse(null);
			// Agent agent = DBExt.selectFirst(conn, Agent.class, "where
			// name=?", agentName);
			if (agent == null)
				throw new RuntimeException("Вещество " + agentName + " не зарегистрировано с системе");

			List<Quota> quotas = conn.createQuery(
					"select q from Quota q where company_id=:company_id and agent_id=:agent_id and volume=:volume and remain>0",
					Quota.class)//
					.setParameter("company_id", user.company)//
					.setParameter("agent_id", agent.id)//
					.setParameter("volume", canisterVolume)//
					.getResultList();

			quotas.sort((q1, q2) -> q1.time.compareTo(q2.time));

			// List<Quota> quotas = DBExt.select(conn, Quota.class,
			// "where companyId=? and agentId=? and volume=? and counter>0 order
			// by time", user.company, agent.id,
			// canisterVolume);

			int rest = quotas.stream().mapToInt(q -> q.remain).sum();
			if (company.rfidBlockSize > rest) {
				throw new IllegalArgumentException("Недостаточно квоты " + agentName + " " + canisterVolume + "ml "
						+ company.name + " " + company.rfidBlockSize + ">" + rest);
			}

			int allowed = 0;

			for (Quota quota : quotas) {
				int x = Math.min(company.rfidBlockSize - allowed, quota.remain);
				allowed += x;
				quota.remain -= x;
				conn.persist(quota);
				// DBExt.store(conn, quota);
				database.incrementTableVersion(Quota.class);
				if (allowed == company.rfidBlockSize)
					break;
			}

			if (allowed != company.rfidBlockSize) {
				throw new IllegalArgumentException("Ошибка при выделении RFID");
			}

			// RfidLabelGroup rfidLabelGroup = new RfidLabelGroup();
			//
			// rfidLabelGroup.canisterVolume = canisterVolume;
			// rfidLabelGroup.agent = agent;
			//
			// rfidLabelGroup.time = new Date();
			//
			// rfidLabelGroup.userName = user.name;
			// rfidLabelGroup.company = company;
			//
			// conn.persist(rfidLabelGroup);

			// DBExt.insert(conn, rfidLabelGroup);

			Date time = new Date();

			for (int i = 0; i < company.rfidBlockSize; i++) {

				RfidLabel rfidLabel = new RfidLabel(0, time, user.name, company, agent, canisterVolume);
				conn.persist(rfidLabel);
				rfidLabel.name = (int) rfidLabel.id;
				conn.persist(rfidLabel);

				int id = (int) rfidLabel.id;

				RfidData data = new RfidData();
				data.UNIQUE_ID = id;
				data.CANISTER_NAME = agentName;
				data.CANISTER_VOLUME_ML = canisterVolume;
				data.CANISTER_MANUFACTURER_NAME = company.name;
				data.CANISTER_CONSUMPTION_ML_M3 = agent.consumption_ml_m3;
				data.CANISTER_CONSUMPTION2_ML_M3 = agent.consumption2_ml_m3;
				data.CANISTER_AERATION_MIN = agent.aeration_min;

				try {
					data.DIGIT_SIG = sig.sign(RfidDataUtils.getKey(data));
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					throw new RuntimeException(e);
				}

				list.add(data);
			}
			database.incrementTableVersion(RfidLabel.class);
		});

		return list;
	}

	@Override
	public void login(String name, String password) throws Exception {
		authComponent.login(name,
				AuthUtils.getSecret(AuthUtils.getPwdHash(name, password), threadLocalRequest.getSession().getId()));
	}

	@Override
	public void logout() throws Exception {
		connectionStatusModule.remove(threadLocalRequest.getSession().getId());
		authComponent.logout();
	}

	@Override
	public User getUser() throws Exception {
		HttpSession session = threadLocalRequest.getSession();
		UserData user = authComponent.getUser();
		connectionStatusModule.add(threadLocalRequest.getThreadLocalRequest().getRemoteHost(), session.getId(),
				ConnectionType.WRITER, user == null ? null : user.name);
		return user == null ? null : new User(user.hasPermission("PERMISSION_WRITE_RFID"));
	}

}
