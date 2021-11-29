package com.ma.hmcrfidserver.client;

import java.io.IOException;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtservice/hmc")
public interface HmcService extends RemoteService {

	Map<String, String> getFirmwareList();

	void addUserCompany(String newCompanyName, String adminName, String adminPasswordHash, String adminEmail)
			throws IOException;

	void removeFirmware(String type);
}
