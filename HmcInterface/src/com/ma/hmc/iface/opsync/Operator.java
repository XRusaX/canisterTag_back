package com.ma.hmc.iface.opsync;

public class Operator {
	public long id; // id или 0 для вновь созданного оператора
	public String name;
	public boolean removed;
	public long modifTime; // UTC3 время изменения

	public Operator() {
	}

	public Operator(long id, String name, boolean removed, long modifTime) {
		this.id = id;
		this.name = name;
		this.removed = removed;
		this.modifTime = modifTime;
	}
}