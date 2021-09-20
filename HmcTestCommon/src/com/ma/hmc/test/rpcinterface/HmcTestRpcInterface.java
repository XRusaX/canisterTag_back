package com.ma.hmc.test.rpcinterface;

import com.ma.hmc.test.rpcdata.TestReport;
import com.ma.hmc.test.rpcdata.User;

public interface HmcTestRpcInterface {
	
	public String servletpath = "/testrpc";
	
	void report(TestReport testReport) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;
}
