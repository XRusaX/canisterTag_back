package com.ma.hmc.iface.boardtest.rpcinterface;

import com.ma.hmc.iface.boardtest.rpcdata.TestReport;
import com.ma.hmc.iface.boardtest.rpcdata.User;

public interface HmcTestRpcInterface {
	
	public String servletpath = "/api/testrpc";
	
	void report(TestReport testReport) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;
}
