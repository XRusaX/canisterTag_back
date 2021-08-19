package ru.aoit.hmc.simulator;

import ru.nppcrts.common.shared.cd.UILabel;

public class ConnectionSettings {
	@UILabel(label = "Server")
	public String serverURL;

	@UILabel(label = "User")
	public String user;

	@UILabel(label = "Password")
	public String password;
}
