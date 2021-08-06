package ru.aoit.hmc.test.rpcinterface;

import ru.aoit.hmc.test.rpcdata.TestReport;
import ru.aoit.hmc.test.rpcdata.User;

public interface HmcTestRpcInterface {
	
	public String servletpath = "/testrpc";
	
	void report(TestReport testReport) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;
}
