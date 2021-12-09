package com.ma.hmc.iface.opsync;

import java.util.List;

public class OpSync {
	public long lastSync;
	public List<Operator> operators;

	public static class Operator {
		public long id; // id или 0 для вновь созданного оператора
		public String name;
		public String lastName;
		public String position;
		public int pin;
		public boolean removed;
		public long modifTime; // UTC3 время изменения
		public String company;

		public Operator() {
		}

		public Operator(long id, String name, String position, String company, boolean removed, long modifTime) {
			this.id = id;
			this.name = name;
			this.position = position;
			this.company = company;
			this.removed = removed;
			this.modifTime = modifTime;
		}
	}
}
