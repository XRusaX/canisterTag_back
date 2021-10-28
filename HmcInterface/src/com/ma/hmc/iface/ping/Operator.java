package com.ma.hmc.iface.ping;

public class Operator {
	public Long id;
	public String name;
	public boolean removed;
	public long modifTime;

	public Operator() {
	}

	public Operator(Long id, String name, boolean removed, long modifTime) {
		this.id = id;
		this.name = name;
		this.removed = removed;
		this.modifTime = modifTime;
	}
}
