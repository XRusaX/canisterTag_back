package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

@RestController
public class PingController {

	private static final int ONLINE_COUNTEER = 2000;

	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;

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
	private synchronized String report(@RequestBody String hmcping) throws IOException {

		HmcPing ping = new Gson().fromJson(hmcping, HmcPing.class);

		synchronized (pingMap) {
			pingMap.put(ping.hmcSerialNumber, new AtomicInteger(ONLINE_COUNTEER));
		}

		HmcPingResponse response = new HmcPingResponse();

		String result = database.exec(em -> {
			return new Gson().toJson(response);
		});

		return result;
	}
}
