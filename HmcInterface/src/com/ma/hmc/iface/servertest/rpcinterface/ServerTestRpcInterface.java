package com.ma.hmc.iface.servertest.rpcinterface;

import com.ma.hmc.iface.shared.HmcType;

public interface ServerTestRpcInterface {

	String servletPath = "/api/servertest";

	void clear() throws Exception;

	void createCustomerCompany(String name, String user, String password) throws Exception;

	void createHmc(HmcType hmcType, String serialNum, String companyName) throws Exception;

	void createRoom(String roomName, String companyName) throws Exception;

	void createOperator(String operator, String companyName) throws Exception;

}
