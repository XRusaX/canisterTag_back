package com.ma.hmc.iface.ping;

public class Operator {
	public Long id;
	public String name;
	public boolean removed;
	public long lastModified;

	public Operator() {
	}

	public Operator(Long id, String name, boolean removed, long lastModified) {
		this.id = id;
		this.name = name;
		this.removed = removed;
		this.lastModified = lastModified;
	}
}
