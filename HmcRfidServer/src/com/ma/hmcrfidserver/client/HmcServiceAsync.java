package com.ma.hmcrfidserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ma.common.shared.cd.CDObject;

public interface HmcServiceAsync {
	void saveRoomCells(List<CDObject> list, long companyId, AsyncCallback<Void> callback);
}
