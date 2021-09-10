package ru.aoit.hmcrfidserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.nppcrts.common.shared.cd.CDObject;

public interface HmcServiceAsync {

	void saveAll(List<CDObject> list, long companyId, AsyncCallback<Void> callback);

}
