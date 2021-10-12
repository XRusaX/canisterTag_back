package com.ma.hmc.simulator.sim;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.ma.hmc.iface.report.HmcReport;
import com.ma.hmc.iface.servertest.rpcinterface.ServerTestRpcInterface;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmc.simulator.ConnectionSettings;
import com.ma.hmc.simulator.HmcSimulatorFrame;

public class HmcSim {

	private String serialNum;

	private ConnectionSettings connectionSettings;

	private CompanySim company;

	private ServerTestRpcInterface proxy;

	public HmcSim(ServerTestRpcInterface proxy, ConnectionSettings connectionSettings, CompanySim company,
			String serialNum) {
		this.proxy = proxy;
		this.connectionSettings = connectionSettings;
		this.company = company;
		this.serialNum = serialNum;
	}

	private final Timer timer = new Timer();
	private TimerTask timerTask;

	public void start() {
		if (timerTask != null)
			throw new IllegalStateException();

		timerTask = new TimerTask() {
			private CanisterSim canister;

			private int consumption = 200;

			@Override
			public void run() {
				try {
					int toSpend = consumption;
					doCleaning(toSpend);
				} catch (Exception e1) {
					e1.printStackTrace();
					cancel();
				}
			}

			private void doCleaning(int toSpend) throws Exception {
				int cleaningId = (int) (System.currentTimeMillis() / 1000);
				String roomName = company.getRandomRoom();
				String operatorName = company.getRandomOperator();

				while (toSpend > 0) {
					if (canister == null)
						canister = company.getCanister();

					int spent = Math.min(toSpend, canister.remainML);
					toSpend -= spent;
					canister.remainML -= spent;

					report(canister, cleaningId, spent, roomName, operatorName);

					if (canister.remainML == 0)
						canister = null;
				}
			}

		};
		timer.schedule(timerTask, (long) (Math.random() * 10_000), 10_000);
	}

	public void stop() {
		timerTask.cancel();
	}

	private void report(CanisterSim canister2, int cleaningId, int spent, String roomName, String operatorName)
			throws IOException {
		HmcReport report = HmcReport.cleaningOk(serialNum, cleaningId, canister2.id, spent, canister2.remainML,
				roomName, operatorName);
		HmcSimulatorFrame.report(connectionSettings.serverURL, report);
	}

	public void createDB() throws Exception {
		proxy.createHmc(HmcType.HMC_1, serialNum, company.descr.name);
	}

}
