package ru.aoit.hmcserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.aoit.hmcserver.shared.ConnectionStatus;

@RemoteServiceRelativePath("service")
public interface Service extends RemoteService {
	List<ConnectionStatus> getConnectionStatusList();
}
