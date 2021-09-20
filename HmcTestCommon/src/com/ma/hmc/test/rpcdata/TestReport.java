package com.ma.hmc.test.rpcdata;

import java.io.Serializable;

import com.ma.hmc.test.shared.HmcTestStatus;
import com.ma.hmc.test.shared.HmcTestType;

@SuppressWarnings("serial")
public class TestReport implements Serializable {
	public String serialNumber;
	public HmcTestType testType;
	public HmcTestStatus testStatus;
	public String details;
}
