package ru.aoit.hmcserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.aoit.hmcserver.shared.ConnectionStatus;

public interface ServiceAsync {

	void getConnectionStatusList(AsyncCallback<List<ConnectionStatus>> callback);

}
