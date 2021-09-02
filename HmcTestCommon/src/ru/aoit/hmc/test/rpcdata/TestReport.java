package ru.aoit.hmc.test.rpcdata;

import java.io.Serializable;

import ru.aoit.hmc.test.shared.HmcTestStatus;
import ru.aoit.hmc.test.shared.HmcTestType;

@SuppressWarnings("serial")
public class TestReport implements Serializable {
	public String serialNumber;
	public HmcTestType testType;
	public HmcTestStatus testStatus;
	public String details;
}
