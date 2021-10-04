package com.ma.hmc.simulator.sim;

public class CanisterSim {
	public final int id;
	public final int volumeML;
	public int remainML;

	public CanisterSim(int id, int volume) {
		this.id = id;
		this.remainML = this.volumeML = volume;
	}
}
