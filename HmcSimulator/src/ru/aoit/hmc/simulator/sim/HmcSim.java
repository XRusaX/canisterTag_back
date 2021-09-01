package ru.aoit.hmc.simulator.sim;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmc.rfid.rpcinterface.TestRpcInterface;
import ru.aoit.hmc.rfid.shared.HmcReportStatus;
import ru.nppcrts.common.gson.GsonUtils;

public class HmcSim {

	private String serialNum;

	private String serverURL;

	private CompanySim company;
	private CanisterSim canister;

	private TestRpcInterface proxy;

	public HmcSim(TestRpcInterface proxy, String serverURL, CompanySim company, String serialNum) {
		this.proxy = proxy;
		this.serverURL = serverURL;
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
					report.consumtionML = consumption;
					report.roomName = company.getRandomRoom();
					report.status = HmcReportStatus.SUCSESS;
					report(serverURL, report);
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
		report.startTime = new Date();
		String requestJson = GsonUtils.requestJson(new URL(serverURL + "/report"), report, String.class, null);
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
