package ru.aoit.hmcserver.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ru.aoit.hmcserver.shared.ConnectionStatus;
import ru.aoit.hmcserver.shared.ConnectionStatus.ConnectionType;

@Component
public class ConnectionStatusModule {
	private Map<String, ConnectionStatus> map = new HashMap<>();

	public void add(String remoteHost, String sessionId, ConnectionType connectionType, String userName) {
		synchronized (map) {
			ConnectionStatus connectionStatus = map.get(sessionId);
			if (connectionStatus == null) {
				connectionStatus = new ConnectionStatus(remoteHost, sessionId, connectionType, new Date());
				map.put(sessionId, connectionStatus);
			}

			connectionStatus.lastTime = System.currentTimeMillis();
			connectionStatus.userName = userName;

			removeOld();
		}
	}

	public List<ConnectionStatus> getList() {
		synchronized (map) {
			removeOld();
			return new ArrayList<>(map.values());
		}
	}

	public void remove(String sessionId) {
		synchronized (map) {
			removeOld();
			map.remove(sessionId);
		}
	}

	private void removeOld() {
		map.values().removeIf(st -> System.currentTimeMillis() - st.lastTime  > 10_000);
	}

}
