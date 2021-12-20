package com.ma.hmcserver.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HmcServiceAsync {
	void getFirmwareList(AsyncCallback<Map<String, String>> callback);
	void removeFirmware(String type, AsyncCallback<Void> callback);

	void addUserCompany(String newCompanyName, String adminName, String adminPasswordHash, String adminEmail,
			AsyncCallback<Void> callback);
}
