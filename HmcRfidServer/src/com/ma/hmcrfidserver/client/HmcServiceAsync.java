package com.ma.hmcrfidserver.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ma.commonui.shared.cd.CDObject;

public interface HmcServiceAsync {
	void saveRoomCells(List<CDObject> list, long companyId, AsyncCallback<Void> callback);

	void getFirmwareList(AsyncCallback<Map<String, String>> callback);

	void addUserCompany(String newCompanyName, String adminName, String adminPasswordHash, String adminEmail,
			AsyncCallback<Void> callback);
}
