package ru.aoit.hmc.simulator;

import ru.nppcrts.common.shared.cd.UILabel;

public class ConnectionSettings {
	@UILabel(label = "Server")
	public String serverURL;

	@UILabel(label = "RFID User")
	public String rfidUser;

	@UILabel(label = "RFID Password")
	public String rfidPassword;
}
