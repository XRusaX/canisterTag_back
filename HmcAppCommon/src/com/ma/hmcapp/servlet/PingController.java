package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.hmc.iface.ping.HmcPing;
import com.ma.hmc.iface.ping.HmcPingResponse;
import com.ma.hmcapp.datasource.HmcDataSource;
import com.ma.hmcapp.datasource.OperatorDataSource;
import com.ma.hmcapp.datasource.RoomDataSource;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;

@RestController
public class PingController {

	private static final int ONLINE_COUNTEER = 2000;

	@Autowired
	private Database2 database;

	@Autowired
	private HmcDataSource hmcDataSource;

	@Autowired
	private OperatorDataSource operatorDataSource;

	@Autowired
	private RoomDataSource roomDataSource;

	private Map<String, AtomicInteger> pingMap = new HashMap<>();

	public boolean isOnline(String serNum) {
		synchronized (pingMap) {
			return pingMap.containsKey(serNum);
		}
	}

	@Scheduled(fixedDelay = 1000)
	private void periodic() {
		synchronized (pingMap) {
			pingMap.values().forEach(cnt -> cnt.addAndGet(-1000));
			pingMap.values().removeIf(cnt -> cnt.get() < 0);
		}
	}

	@PostMapping(value = "/ping")
	private synchronized String ping(@RequestBody String hmcping) throws IOException {

		HmcPing ping = new Gson().fromJson(hmcping, HmcPing.class);

		synchronized (pingMap) {
			pingMap.put(ping.hmcSerialNumber, new AtomicInteger(ONLINE_COUNTEER));
		}

		synchronized (operatorDataSource) {
			synchronized (roomDataSource) {
				String result = database.exec(em -> {
					HmcPingResponse response = new HmcPingResponse();

					Hmc hmc = hmcDataSource.getBySerNum(em, ping.hmcSerialNumber);
					if (hmc.company != null) {
						List<Operator> changed = new ArrayList<>();
						if (ping.operators != null)
							changed = operatorDataSource.updateOperators(em, ping.operators, hmc.company);
						List<Operator> changedByOthers = operatorDataSource.getModified(em, hmc.company, ping.lastSync);
						changedByOthers.removeAll(changed);
						response.operators = changedByOthers.stream().map(
								o -> new com.ma.hmc.iface.ping.Operator(o.id, o.name, o.removed, o.modifTime.getTime()))
								.collect(Collectors.toList());
						response.lastSync = System.currentTimeMillis();
					}
					return new Gson().toJson(response);
				});
				return result;
			}
		}
	}
}
