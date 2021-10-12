package com.ma.hmc.simulator.sim;

import java.util.ArrayList;
import java.util.List;

import com.ma.hmc.iface.servertest.rpcinterface.ServerTestRpcInterface;
import com.ma.hmc.simulator.ConnectionSettings;
import com.ma.hmc.simulator.HmcSimulatorFrame.FillParams;

public class WorldSim {
	private List<CompanySim> companies = new ArrayList<>();

	public WorldSim(ServerTestRpcInterface proxy, ConnectionSettings connectionSettings, FillParams fillParams) {
		for (int i = 0; i < fillParams.companies; i++) {
			CompanySim companySim = new CompanySim(proxy, connectionSettings, fillParams, i);
			companies.add(companySim);
		}
	}

	public void start() {
		companies.forEach(h -> h.start());
	}

	public void stop() {
		companies.forEach(h -> h.stop());
	}

	public void createDB() throws Exception {
		for (CompanySim companySim : companies) {
			companySim.createDB();
		}
	}
}
