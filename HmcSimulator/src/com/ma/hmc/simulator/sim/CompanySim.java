package com.ma.hmc.simulator.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.ma.common.rpc.HttpProxy;
import com.ma.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import com.ma.hmc.rfid.rpcinterface.TestRpcInterface;
import com.ma.hmc.rfid.ruslandata.RfidData;
import com.ma.hmc.simulator.ConnectionSettings;
import com.ma.hmc.simulator.HmcSimulatorFrame.FillParams;

public class CompanySim {

	private List<HmcSim> hmcs = new ArrayList<>();
	private List<String> rooms = new ArrayList<>();
	private List<String> operators = new ArrayList<>();
	public final String name;
	private TestRpcInterface proxy;

	private List<CanisterSim> canisters = new LinkedList<>();
	private ConnectionSettings connectionSettings;

	String[] companies = {
			"СмартМи Инжиниринг",
			"E3 Investment",
			"ПК Марион",
			"Best Friend",
			"Полимертехпром",
			"“ИНГ ВАР” Инженерный консалтинг",
			"ООО Севзап АКБ",
			"ООО ТРИКОТАЖНЫЕ ТЕХНОЛОГИИ",
			"ООО \"М.Т.Д\"",
			"Криошоп",
			"Агентство маркетинга и коммуникаций",
			"Кафе Чеснок",
	};

	String[] rooms2 = {
			"Конференц-зал",
			"Директор",
			"Приемная",
			"Спортзал",
			"Каб 101",
			"Каб 102",
			"Каб 103",
			"Каб 104",
			"Каб 105",
			"Каб 106",
			"Туалет",
	};
	
	String[] operators2 = {
			"Виктория Лоскунова",
			"Сергей Воротников",
			"Вадим Докумов",
			"Дмитрий Алексеев",
			"Данила Коробейников",
			"Андрей Косых",
			"Александр Ленивец",
			"Денис Ерошин",
			"Влад Горбатюк",
			"Александр Фокин"
	};
	
	
	public CompanySim(TestRpcInterface proxy, ConnectionSettings connectionSettings, FillParams fillParams, int num) {
		this.proxy = proxy;
		this.connectionSettings = connectionSettings;
		//this.name = "company" + num;
		
		this.name = companies[num];

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
			String serialNum = String.format("%08X%02d",  r.nextInt(), num) ;
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
