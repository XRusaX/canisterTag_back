package ru.aoit.hmcserver.server;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import ru.aoit.hmcserver.client.Service;
import ru.aoit.hmcserver.shared.ConnectionStatus;
import ru.aoit.hmcserver.shared.ConnectionStatus.ConnectionType;
import ru.nppcrts.common.gwt.app.server.SpringRemoteServiceServlet;
import ru.nppcrts.common.gwt.auth.server.AuthComponent;
import ru.nppcrts.common.gwt.shared.auth.UserData;

@SuppressWarnings("serial")
@WebServlet("/hmcserver/service")
public class ServiceImpl extends SpringRemoteServiceServlet implements Service {

	@Autowired
	private ConnectionStatusModule connectionStatusModule;

	@Autowired
	private AuthComponent authComponent;
	
	@Override
	public List<ConnectionStatus> getConnectionStatusList() {
		HttpSession session = getSession();
		UserData user = authComponent.getUser(session);
		connectionStatusModule.add(getThreadLocalRequest().getRemoteHost(), session.getId(), ConnectionType.WEB,
				user == null ? null : user.name);

		return connectionStatusModule.getList();
	}

}
