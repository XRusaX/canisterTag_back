package com.ma.hmcrfidserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ma.common.shared.cd.CDObject;

@RemoteServiceRelativePath("hmc")
public interface HmcService extends RemoteService {
	void saveRoomCells(List<CDObject> list, long companyId) throws Exception;
	
}
