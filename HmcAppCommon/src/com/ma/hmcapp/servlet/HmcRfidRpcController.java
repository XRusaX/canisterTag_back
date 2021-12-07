package com.ma.hmcapp.servlet;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.AuthComponent;
import com.ma.appcommon.connection.ConnectionStatusModule;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.rpc.RpcController;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.appcommon.shared.connection.ConnectionStatus.ConnectionType;
import com.ma.common.rsa.Sig;
import com.ma.common.shared.Severity;
import com.ma.hmc.iface.rfid.rpcdata.User;
import com.ma.hmc.iface.rfid.rpcinterface.HmcRfidRpcInterface;
import com.ma.hmc.iface.rfid.ruslandata.RfidData;
import com.ma.hmc.iface.rfid.ruslandata.RfidDataUtils;
import com.ma.hmcapp.datasource.AgentDataSource;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.datasource.QuotaDataSource;
import com.ma.hmcapp.datasource.RfidLabelDataSource;
import com.ma.hmcdb.entity.Agent;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.rfid.Quota;
import com.ma.hmcdb.entity.rfid.RfidLabel;
import com.ma.hmcdb.shared.Permissions;

@RestController
@RequestMapping(HmcRfidRpcInterface.servletPath)
public class HmcRfidRpcController extends RpcController implements HmcRfidRpcInterface {

	@Autowired
	private Sig sig;

	@Autowired
	private Database2 database;

	@Autowired
	private AuthComponent authComponent;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private ConnectionStatusModule connectionStatusModule;

	@Autowired
	private CompanyDataSource companyDataSource;

	@Autowired
	private AgentDataSource agentDataSource;

	@Autowired
	private QuotaDataSource quotaDataSource;

	@Autowired
	private RfidLabelDataSource rfidLabelDataSource;

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

			Company company = companyDataSource.load(conn, user.company);

			if (company.getRfidBlockSize() <= 0)
				throw new RuntimeException("Размер блока меток <= 0");

			Agent agent = agentDataSource.getByName(conn, agentName);

			if (agent == null)
				throw new RuntimeException("Вещество " + agentName + " не зарегистрировано с системе");

			List<Quota> quotas = quotaDataSource.get(conn, company, agent, canisterVolume);

			quotas.sort((q1, q2) -> q1.getTime().compareTo(q2.getTime()));

			// List<Quota> quotas = DBExt.select(conn, Quota.class,
			// "where companyId=? and agentId=? and volume=? and counter>0 order
			// by time", user.company, agent.id,
			// canisterVolume);

			int rest = quotas.stream().mapToInt(q -> q.getRemain()).sum();
			if (company.getRfidBlockSize() > rest) {
				throw new IllegalArgumentException("Недостаточно квоты " + agentName + " " + canisterVolume + "ml "
						+ company.getName() + " " + company.getRfidBlockSize() + ">" + rest);
			}

			int allowed = 0;

			for (Quota quota : quotas) {
				int x = Math.min(company.getRfidBlockSize() - allowed, quota.getRemain());
				allowed += x;
				quota.setRemain(quota.getRemain() - x);
				quotaDataSource.store(conn, quota);
				if (allowed == company.getRfidBlockSize())
					break;
			}

			if (allowed != company.getRfidBlockSize()) {
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

			for (int i = 0; i < company.getRfidBlockSize(); i++) {

				RfidLabel rfidLabel = new RfidLabel(0, time, user.name, company, agent, canisterVolume);
				rfidLabelDataSource.store(conn, rfidLabel);
				rfidLabel.setName((int) rfidLabel.getId());
				rfidLabelDataSource.store(conn, rfidLabel);

				int id = (int) rfidLabel.getId();

				RfidData data = new RfidData();
				data.UNIQUE_ID = id;
				data.CANISTER_NAME = agentName;
				data.CANISTER_VOLUME_ML = canisterVolume;
				data.CANISTER_MANUFACTURER_NAME = company.getName();
				data.CANISTER_CONSUMPTION_ML_M3 = agent.getConsumption_ml_m3();
				data.CANISTER_CONSUMPTION2_ML_M3 = agent.getConsumption2_ml_m3();
				data.CANISTER_AERATION_MIN = agent.getAeration_min();

				try {
					data.DIGIT_SIG = sig.sign(RfidDataUtils.getKey(data));
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					throw new RuntimeException(e);
				}

				list.add(data);
			}
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
		return user == null ? null : new User(user.hasPermission(Permissions.PERMISSION_WRITE_RFID));
	}

}
