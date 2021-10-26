package com.ma.hmc.iface.ping;

import java.util.List;

import com.ma.hmc.iface.shared.HmcType;

public class HmcPing {
	public HmcType hmcType;
	public String hmcSerialNumber;

	public long lastSync;
	public List<Operator> operators;
	public List<Room> rooms;
}
