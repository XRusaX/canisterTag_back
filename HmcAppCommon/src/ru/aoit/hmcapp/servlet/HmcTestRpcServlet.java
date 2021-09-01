package ru.aoit.hmcapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import ru.aoit.appcommon.AuthImpl;
import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.RpcServlet;
import ru.aoit.appcommon.SpringUtils;
import ru.aoit.appcommon.ThreadLocalRequest;
import ru.aoit.appcommon.connection.ConnectionStatusModule;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.appcommon.shared.auth.AuthUtils;
import ru.aoit.appcommon.shared.auth.UserData;
import ru.aoit.appcommon.shared.connection.ConnectionStatus.ConnectionType;
import ru.aoit.hmc.test.rpcdata.TestReport;
import ru.aoit.hmc.test.rpcdata.User;
import ru.aoit.hmc.test.rpcinterface.HmcTestRpcInterface;

@SuppressWarnings("serial")
// @WebServlet(HmcTestRpcInterface.servletpath)
public class HmcTestRpcServlet extends RpcServlet implements HmcTestRpcInterface {

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@Autowired
	private Database2 database;

	@Autowired
	private AuthImpl authComponent;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private ConnectionStatusModule connectionStatusModule;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		SpringUtils.autowire(request, this);
		super.service(request, response);
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
		return new User();
	}

	@Override
	public void report(TestReport testReport) throws Exception {
		ru.aoit.hmcdb.shared.test.TestReport r = new ru.aoit.hmcdb.shared.test.TestReport(testReport.serialNumber);

		database.execVoid(conn -> conn.persist(r));
		database.incrementTableVersion(ru.aoit.hmcdb.shared.test.TestReport.class);

	}

}
