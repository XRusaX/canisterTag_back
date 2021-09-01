package ru.aoit.hmc.rfid.rpcinterface;

import java.io.IOException;

public interface TestRpcInterface {

	String servletPath = "/testapp";

	void clear() throws IOException;

	void createCustomerCompany(String name) throws IOException;

	void createHmc(String serialNum, String companyName) throws IOException;

	void createRoom(String roomName, String companyName) throws IOException;

	void createQuota() throws IOException;

}
