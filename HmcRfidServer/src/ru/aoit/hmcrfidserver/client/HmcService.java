package ru.aoit.hmcrfidserver.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.nppcrts.common.shared.cd.CDObject;

@RemoteServiceRelativePath("hmc")
public interface HmcService extends RemoteService {
	void saveAll(List<CDObject> list, long companyId) throws Exception;
	
}
