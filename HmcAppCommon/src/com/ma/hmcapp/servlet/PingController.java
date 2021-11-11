package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	private static final int ONLINE_COUNTEER = 2000;

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

	@GetMapping(value = "/ping")
	private synchronized void ping(@RequestParam("serNum") String hmcSerialNumber) throws IOException {
		synchronized (pingMap) {
			pingMap.put(hmcSerialNumber, new AtomicInteger(ONLINE_COUNTEER));
		}

	}
}
