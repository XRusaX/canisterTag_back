package ru.aoit.hmcapp.servlet;

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
import ru.aoit.hmc.test.rpcdata.TestReport;
import ru.aoit.hmc.test.rpcdata.User;
import ru.aoit.hmc.test.rpcinterface.HmcTestRpcInterface;
import ru.aoit.hmcapp.HmcAppHelper;
import ru.aoit.hmcdb.shared.Hmc;

@RestController
@RequestMapping(HmcTestRpcInterface.servletpath)
public class HmcTestRpcController extends RpcController implements HmcTestRpcInterface {

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@Autowired
	private Database2 database;

	@Autowired
	private AuthImpl authComponent;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private HmcAppHelper hmcAppHelper;

	@Autowired
	private ConnectionStatusModule connectionStatusModule;

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
		return user == null ? null : new User();
	}

	@Override
	public void report(TestReport testReport) throws Exception {

		database.execVoid(conn -> {
			Hmc hmc = hmcAppHelper.getCreateHmc(conn, testReport.serialNumber);
			ru.aoit.hmcdb.shared.test.TestReport r = new ru.aoit.hmcdb.shared.test.TestReport(hmc, testReport.testType,
					testReport.testStatus, testReport.details);
			conn.persist(r);
		});

		database.incrementTableVersion(ru.aoit.hmcdb.shared.test.TestReport.class);

	}

}
