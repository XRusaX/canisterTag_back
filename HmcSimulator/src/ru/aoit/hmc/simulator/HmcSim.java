package ru.aoit.hmc.simulator;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.nppcrts.common.gson.GsonUtils;

public class HmcSim {

	private ConnectionSettings connectionSettings;
	private HmcReport report;

	public HmcSim(ConnectionSettings data, HmcReport report) {
		this.connectionSettings = data;
		this.report = report;
	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public String report() {
		report.startTime = new Date();
		try {
			String requestJson = GsonUtils.requestJson(new URL(connectionSettings.serverURL + "/report"), report,
					String.class, null);
			return requestJson;
		} catch (IOException e) {
			return e.toString();
		}
	}

}
