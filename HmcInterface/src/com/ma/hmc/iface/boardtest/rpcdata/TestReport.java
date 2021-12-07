package com.ma.hmc.iface.boardtest.rpcdata;

import java.io.Serializable;

import com.ma.hmc.iface.boardtest.shared.HmcTestStatus;
import com.ma.hmc.iface.boardtest.shared.HmcTestType;

@SuppressWarnings("serial")
public class TestReport implements Serializable {
	public String serialNumber;
	public HmcTestType testType;
	public HmcTestStatus testStatus;
	public String details;
}
