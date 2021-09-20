package com.ma.hmcapp.servlet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.AuthImpl;
import com.ma.appcommon.Database2;
import com.ma.appcommon.ThreadLocalRequest;
import com.ma.appcommon.connection.ConnectionStatusModule;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.rpc.RpcController;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.appcommon.shared.connection.ConnectionStatus.ConnectionType;
import com.ma.hmc.test.rpcdata.TestReport;
import com.ma.hmc.test.rpcdata.User;
import com.ma.hmc.test.rpcinterface.HmcTestRpcInterface;
import com.ma.hmcapp.HmcAppHelper;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Permissions;

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
		authComponent.checkPermissions(Permissions.PERMISSION_TEST);

		database.execVoid(conn -> {
			Hmc hmc = hmcAppHelper.getCreateHmc(conn, testReport.serialNumber);
			com.ma.hmcdb.shared.test.TestReport r = new com.ma.hmcdb.shared.test.TestReport(hmc, testReport.testType,
					testReport.testStatus, testReport.details);
			conn.persist(r);
		});

		database.incrementTableVersion(com.ma.hmcdb.shared.test.TestReport.class);

	}

}
