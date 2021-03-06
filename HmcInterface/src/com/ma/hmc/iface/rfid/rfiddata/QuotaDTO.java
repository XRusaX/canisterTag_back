package com.ma.hmc.iface.rfid.rfiddata;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuotaDTO implements Serializable {
	public String name;
	public double concentration;
	public int volume;

	public QuotaDTO(String name, double concentration, int volume) {
		this.name = name;
		this.concentration = concentration;
		this.volume = volume;
	}

	@Override
	public String toString() {
		return name + " " + concentration + "%" + " (" + volume + "мл.)";
	}

}
