package com.ma.hmc.iface.servertest.rpcinterface;

import java.io.IOException;

import com.ma.hmc.iface.shared.HmcType;

public interface ServerTestRpcInterface {

	String servletPath = "/servertest";

	void clear() throws IOException;

	void createCustomerCompany(String name) throws IOException;

	void createHmc(HmcType hmcType, String serialNum, String companyName) throws IOException;

	void createRoom(String roomName, String companyName) throws IOException;

	void createQuota() throws IOException;

	void createOperator(String operator, String companyName) throws IOException;

}
