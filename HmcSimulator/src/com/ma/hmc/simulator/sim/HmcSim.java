package com.ma.hmc.simulator.sim;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.ma.common.gson.GsonUtils;
import com.ma.hmc.rfid.rpcdata.HmcReport;
import com.ma.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import com.ma.hmc.rfid.rpcinterface.TestRpcInterface;
import com.ma.hmc.rfid.shared.HmcReportStatus;
import com.ma.hmc.simulator.ConnectionSettings;

public class HmcSim {

	private String serialNum;

	private ConnectionSettings connectionSettings;

	private CompanySim company;
	private CanisterSim canister;

	private TestRpcInterface proxy;

	public HmcSim(TestRpcInterface proxy, ConnectionSettings connectionSettings, CompanySim company, String serialNum) {
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
			@Override
			public void run() {
				try {
					HmcReport report = new HmcReport();
					report.hmcSerialNumber = serialNum;
					report.durationS = 10;
					int consumption = 10;
					report.canisterId = getCanisterId(consumption);
					report.consumptionML = consumption;
					report.roomName = company.getRandomRoom();
					report.operatorName = company.getRandomOperator();
					report.status = HmcReportStatus.SUCSESS;
					report(connectionSettings.serverURL, report);
				} catch (Exception e1) {
					e1.printStackTrace();
					cancel();
				}
			}
		};
		timer.schedule(timerTask, 1000, 1000);
	}

	public void stop() {
		timerTask.cancel();
	}

	public static String report(String serverURL, HmcReport report) throws IOException {
		report.startTime = new Date().getTime();
		String requestJson = GsonUtils.requestJson(new URL(serverURL + HmcRfidRpcInterface.api + "/report"), report,
				String.class, null);
		return requestJson;
	}

	public void createDB() throws IOException {
		proxy.createHmc(serialNum, company.name);
	}

	private Integer getCanisterId(int consumption) throws Exception {
		if (canister == null || canister.rest < consumption) {
			canister = company.getCanister();
		}
		canister.rest -= consumption;
		return canister.id;
	}

}
