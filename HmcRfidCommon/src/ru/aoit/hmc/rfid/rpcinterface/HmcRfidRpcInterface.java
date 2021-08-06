package ru.aoit.hmc.rfid.rpcinterface;

import java.util.List;

import ru.aoit.hmc.rfid.rpcdata.User;
import ru.aoit.hmc.rfid.ruslandata.RfidData;

public interface HmcRfidRpcInterface {

	String servletPath = "/rfidrpc";

	List<RfidData> getSigs(String agentName, int canisterVolume) throws Exception;

	void login(String name, String password) throws Exception;

	void logout() throws Exception;

	User getUser() throws Exception;

	void tagWriteDone(String id) throws Exception;
}
