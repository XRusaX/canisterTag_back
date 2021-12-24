package com.ma.hmcapp.servlet;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ma.hmc.iface.rfid.ruslandata.Tag;
import com.ma.hmcapp.datasource.AgentDataSource;
import com.ma.hmcapp.datasource.CanisterWorkModeDataSource;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.datasource.QuotaDataSource;
import com.ma.hmcapp.datasource.RfidLabelDataSource;
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.CanisterWorkMode;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.entity.rfid.Quota;
import com.ma.hmcapp.entity.rfid.RfidLabel;
import com.ma.hmcapp.shared.Permissions;

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
	private CanisterWorkModeDataSource canisterWorkModeDataSource;

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

			CanisterWorkMode canWorkMode = canisterWorkModeDataSource.getByAgent(conn, agent);

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
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);

			DateFormat dateString = new SimpleDateFormat("yyMMdd");

			for (int i = 0; i < company.getRfidBlockSize(); i++) {

				RfidLabel rfidLabel = new RfidLabel(0, time, user.name, company, agent, canisterVolume);
				rfidLabelDataSource.store(conn, rfidLabel);
				rfidLabel.setName((int) rfidLabel.getId());
				rfidLabelDataSource.store(conn, rfidLabel);

				int id = (int) rfidLabel.getId();

				RfidData data = new RfidData();
//				data.add(Tag.TAG_CAN_VERSION,1));
////				data.TAG_CAN_VERSION = 1; // TODO: Подумать куда вынести
				data.add(Tag.TAG_CAN_VOLUME_ML, canisterVolume);
////				data.TAG_CAN_VOLUME_ML = canisterVolume;
				data.add(Tag.TAG_CAN_MANUFACTURER_NAME, company.getName());
////				data.TAG_CAN_MANUFACTURER_NAME = company.getName();
//				data.add(Tag.TAG_CAN_ISSUE_DATE_YYMMDD,dateString.format(time)));
////				data.TAG_CAN_ISSUE_DATE_YYMMDD = dateString.format(time); // TODO: Переделать на нормальное значение
//				
//				calendar.add(Calendar.MONTH, agent.getShelfLife_months());
//				data.add(Tag.TAG_CAN_EXPIRATION_DATE_YYMMDD,dateString.format(calendar.getTime())));
//				
////				data.TAG_CAN_EXPIRATION_DATE_YYMMDD = dateString.format(calendar.getTime()); // TODO: Переделать на
//																							// нормальное значение
				data.add(Tag.TAG_CAN_ACTIVE_INGRIDIENT_NAME, agent.getIngridientName());
////				data.TAG_CAN_ACTIVE_INGRIDIENT_NAME = agent.getIngridientName();
//				data.add(Tag.TAG_CAN_RESIDUAL_VOLUME_ML,canisterVolume));
////				data.TAG_CAN_RESIDUAL_VOLUME_ML = canisterVolume;
				data.add(Tag.TAG_CAN_DESINFICTANT_NAME, agentName);
////				data.TAG_CAN_DESINFICTANT_NAME = agentName;
//				
//				data.add(Tag.TAG_CAN_RFID_MANUFACTURER_NAME,company.getName()));
////				data.TAG_CAN_RFID_MANUFACTURER_NAME = company.getName();
//				data.add(Tag.TAG_CAN_RFID_ISSUE_DATE_YYMMDD,dateString.format(time)));
////				data.TAG_CAN_RFID_ISSUE_DATE_YYMMDD = dateString.format(time);
				data.add(Tag.TAG_CAN_INGRIDIENT_CONCENTRATION, agent.getConcentration());
//				data.TAG_CAN_INGRIDIENT_CONCENTRATION = agent.getConcentration();
				data.add(Tag.TAG_CAN_UNIQUE_ID, id);
//				data.TAG_CAN_UNIQUE_ID = id;

//				data.add(Tag.BATCH_NUMBER,"тестовая партия"));
////				data.BATCH_NUMBER = "тестовая партия"; // НАЗВАНИЕ НЕ СООТВЕТСТВУЕТ ДОКУМЕНТУ

				// РЕЖИМЫ
				// TODO: Переделать
//				data.add(68,1));
////				data.TAG_CAN_WORK_MODE_ID = 1;
//				data.add(69,"Стандартный"));
////				data.TAG_CAN_WORK_MODE_NAME = "Стандартный";
//				data.add(70,10));
////				data.TAG_CAN_CONSUMPTION_ML_M3 = agent.getConsumption_ml_m3();
//				data.add(71,1800));
////				data.TAG_CAN_EXPOSURE_SEC = 1800;
//				data.add(72,3600));
////				data.TAG_CAN_AIRING_SEC = 3600;
//				data.add(73,0));
////				data.TAG_CAN_IMPULSE_PERIOD_SEC = 0;
//				data.add(74,0));
////				data.TAG_CAN_IMPULSE_WIDTH_SEC = 0;

				try {
					data.add(Tag.TAG_CAN_DIGIT_SIG, sig.sign(RfidData.getKey(data)));
//					data.TAG_CAN_DIGIT_SIG = sig.sign(RfidDataUtils.getKey(data));
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
