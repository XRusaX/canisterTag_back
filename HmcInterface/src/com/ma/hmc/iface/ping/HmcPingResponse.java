package com.ma.hmc.iface.ping;

import java.util.List;

public class HmcPingResponse {
	public long lastSync; 
	public List<Operator> operators;
	public List<Room> rooms;
}
