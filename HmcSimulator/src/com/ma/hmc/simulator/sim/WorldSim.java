package com.ma.hmc.simulator.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ma.hmc.rfid.rpcinterface.TestRpcInterface;
import com.ma.hmc.simulator.ConnectionSettings;
import com.ma.hmc.simulator.HmcSimulatorFrame.FillParams;

public class WorldSim {
	private List<CompanySim> companies = new ArrayList<>();
	private TestRpcInterface proxy;

	public WorldSim(TestRpcInterface proxy, ConnectionSettings connectionSettings, FillParams fillParams) {
		this.proxy = proxy;
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

	public void createDB() throws IOException {
		proxy.createQuota();

		for (CompanySim companySim : companies) {
			companySim.createDB();
		}
	}
}
