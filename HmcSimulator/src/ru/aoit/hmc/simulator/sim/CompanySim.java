package ru.aoit.hmc.simulator.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ru.aoit.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import ru.aoit.hmc.rfid.rpcinterface.TestRpcInterface;
import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmc.simulator.ConnectionSettings;
import ru.aoit.hmc.simulator.HmcSimulatorFrame.FillParams;
import ru.nppcrts.common.rpc.HttpProxy;

public class CompanySim {

	private List<HmcSim> hmcs = new ArrayList<>();
	private List<String> rooms = new ArrayList<>();
	private List<String> operators = new ArrayList<>();
	public final String name;
	private TestRpcInterface proxy;

	private List<CanisterSim> canisters = new LinkedList<>();
	private ConnectionSettings connectionSettings;

	public CompanySim(TestRpcInterface proxy, ConnectionSettings connectionSettings, FillParams fillParams, int num) {
		this.proxy = proxy;
		this.connectionSettings = connectionSettings;
		this.name = "company" + num;

		for (int j = 0; j < fillParams.rooms; j++) {
			String roomName = "room_" + j;
			rooms.add(roomName);
		}

		for (int j = 0; j < fillParams.operators; j++) {
			String name = "Баба Маня " + j;
			operators.add(name);
		}

		for (int j = 0; j < fillParams.hmcs; j++) {
			String serialNum = "hmc_" + num + "_" + j;
			HmcSim hmcSim = new HmcSim(proxy, connectionSettings, this, serialNum);
			hmcs.add(hmcSim);
		}

	}

	public void start() {
		hmcs.forEach(h -> h.start());
	}

	public void stop() {
		hmcs.forEach(h -> h.stop());
	}

	public void createDB() throws IOException {
		proxy.createCustomerCompany(name);
		for (HmcSim hmcSim : hmcs)
			hmcSim.createDB();
		for (String room : rooms)
			proxy.createRoom(room, name);
		for (String operator : operators)
			proxy.createOperator(operator, name);
	}

	public String getRandomRoom() {
		return rooms.get((int) (Math.random() * rooms.size()));
	}

	public String getRandomOperator() {
		return operators.get((int) (Math.random() * operators.size()));
	}

	public CanisterSim getCanister() throws Exception {
		if (canisters.isEmpty()) {
			HmcRfidRpcInterface proxy2 = HttpProxy.makeProxy(HmcRfidRpcInterface.class,
					connectionSettings.serverURL + HmcRfidRpcInterface.servletPath, null);
			proxy2.login(connectionSettings.rfidUser, connectionSettings.rfidPassword);
			try {
				int volume = 3000;
				List<RfidData> sigs = proxy2.getSigs("Гриндез", volume);
				canisters = sigs.stream().map(s -> new CanisterSim(s.UNIQUE_ID, volume)).collect(Collectors.toList());
			} finally {
				proxy2.logout();
			}
		}
		return canisters.remove(0);
	}
}
