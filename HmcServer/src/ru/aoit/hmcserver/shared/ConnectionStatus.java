package ru.aoit.hmcserver.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ConnectionStatus implements Serializable {

	public enum ConnectionType {
		WEB, WRITER
	}

	public ConnectionStatus() {
	}

	public ConnectionStatus(String host, String sessionId, ConnectionType connectionType, Date date) {
		this.host = host;
		this.sessionId = sessionId;
		this.connectionType = connectionType;
		this.connectingTime = date;
	}

	public String host;
	public String sessionId;
	public ConnectionType connectionType;
	public Date connectingTime;
	public String userName;
	public long lastTime;

}
