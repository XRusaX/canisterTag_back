package com.ma.hmc.iface.opsync;

public class Room {
	public Long id;
	public String name;
	public boolean removed;
	public long lastModified;

	public Room() {
	}

	public Room(Long id, String name, boolean removed, long lastModified) {
		this.id = id;
		this.name = name;
		this.removed = removed;
		this.lastModified = lastModified;
	}
}
