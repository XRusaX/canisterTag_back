package com.ma.hmc.rfid.rpcdata;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	public boolean canWrite;

	public User(boolean canWrite) {
		this.canWrite = canWrite;
	}
}