package com.ma.hmc.iface.boardtest.rpcdata;

import java.io.Serializable;

import com.ma.hmc.iface.boardtest.shared.HmcTestStatus;
import com.ma.hmc.iface.boardtest.shared.HmcTestType;
import com.ma.hmc.iface.shared.HmcType;

@SuppressWarnings("serial")
public class TestReport implements Serializable {
	public HmcType hmcType;
	public String serialNumber;
	public HmcTestType testType;
	public HmcTestStatus testStatus;
	public String details;
}
