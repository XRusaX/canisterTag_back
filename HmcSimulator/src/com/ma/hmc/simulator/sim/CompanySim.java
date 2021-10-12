package com.ma.hmc.simulator.sim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.ma.common.rpc.HttpProxy;
import com.ma.hmc.iface.rfid.rpcinterface.HmcRfidRpcInterface;
import com.ma.hmc.iface.rfid.ruslandata.RfidData;
import com.ma.hmc.iface.servertest.rpcinterface.ServerTestRpcInterface;
import com.ma.hmc.simulator.ConnectionSettings;
import com.ma.hmc.simulator.HmcSimulatorFrame.FillParams;

public class CompanySim {

	private List<HmcSim> hmcs = new ArrayList<>();
	private List<String> rooms = new ArrayList<>();
	private List<String> operators = new ArrayList<>();
	public final CompanyDescr descr;
	private ServerTestRpcInterface proxy;

	private List<CanisterSim> canisters = new LinkedList<>();
	private ConnectionSettings connectionSettings;

	static class CompanyDescr {
		String name;
		String user;
		String password;

		public CompanyDescr(String name, String user, String password) {
			this.name = name;
			this.user = user;
			this.password = password;
		}
	}

	CompanyDescr[] companyDescrs = { //
			new CompanyDescr("СмартМи Инжиниринг", "smi", "smi"), //
			new CompanyDescr("E3 Investment", "e3", "e3"), //
			new CompanyDescr("Best Friend", null, null), //
			new CompanyDescr("Полимертехпром", null, null), //
			new CompanyDescr("“ИНГ ВАР” Инженерный консалтинг", null, null), //
			new CompanyDescr("ООО Севзап АКБ", null, null), new CompanyDescr("ООО ТРИКОТАЖНЫЕ ТЕХНОЛОГИИ", null, null), //
			new CompanyDescr("ООО \"М.Т.Д\"", null, null), new CompanyDescr("Криошоп", null, null), //
			new CompanyDescr("Агентство маркетинга и коммуникаций", null, null), //
			new CompanyDescr("Кафе Чеснок", null, null),//
	};

	String[] rooms2 = { "Конференц-зал", "Директор", "Приемная", "Спортзал", "Каб 101", "Каб 102", "Каб 103", "Каб 104",
			"Каб 105", "Каб 106", "Туалет", };

	String[] operators2 = { "Виктория Лоскунова", "Сергей Воротников", "Вадим Докумов", "Дмитрий Алексеев",
			"Данила Коробейников", "Андрей Косых", "Александр Ленивец", "Денис Ерошин", "Влад Горбатюк",
			"Александр Фокин" };

	public CompanySim(ServerTestRpcInterface proxy, ConnectionSettings connectionSettings, FillParams fillParams,
			int num) {
		this.proxy = proxy;
		this.connectionSettings = connectionSettings;
		// this.name = "company" + num;

		this.descr = companyDescrs[num];

		for (int j = 0; j < fillParams.rooms; j++) {
			String roomName = rooms2[j];
			rooms.add(roomName);
		}

		for (int j = 0; j < fillParams.operators; j++) {
			String name = operators2[j];
			operators.add(name);
		}

		Random r = new Random(67723547);

		for (int j = 0; j < fillParams.hmcs; j++) {
			int randomInt = r.nextInt();
			String serialNum = String.format("%08X%02d", randomInt, num);
			// HmcT
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

	public void createDB() throws Exception {
		proxy.createCustomerCompany(descr.name, descr.user, descr.password);
		for (HmcSim hmcSim : hmcs)
			hmcSim.createDB();
		for (String room : rooms)
			proxy.createRoom(room, descr.name);
		for (String operator : operators)
			proxy.createOperator(operator, descr.name);
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
					connectionSettings.serverURL + HmcRfidRpcInterface.api + HmcRfidRpcInterface.servletPath, null);
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
