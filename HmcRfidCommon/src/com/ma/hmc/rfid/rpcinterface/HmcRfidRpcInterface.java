package com.ma.hmc.rfid.rpcinterface;

import java.util.List;

import com.ma.hmc.rfid.rpcdata.User;
import com.ma.hmc.rfid.ruslandata.RfidData;

public interface HmcRfidRpcInterface {

	String servletPath = "/rfidrpc";
	String api = "/api";

	List<RfidData> getSigs(String agentName, int canisterVolume) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;

	void tagWriteDone(String id) throws Exception;
}
