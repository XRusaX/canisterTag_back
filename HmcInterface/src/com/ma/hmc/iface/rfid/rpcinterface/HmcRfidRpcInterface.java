package com.ma.hmc.iface.rfid.rpcinterface;

import java.util.List;

import com.ma.hmc.iface.rfid.rfiddata.RfidData;
import com.ma.hmc.iface.rfid.rpcdata.User;

public interface HmcRfidRpcInterface {

	String servletPath = "/api/rfidrpc";
//	String api = "/api";

	List<RfidData> getSigs(String agentName, int canisterVolume) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;

	void tagWriteDone(String id) throws Exception;
}
