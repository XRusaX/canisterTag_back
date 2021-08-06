package ru.aoit.hmc.rfid.rpcdata;

import java.util.Date;

public class HmcReport {
	public String hmcSerialNumber;
	public Date startTime;
	public Integer durationS;
	public Integer canisterId;
	public Integer consumtionML;
	public String user;
	public String company;
	public String room;
	public HmcReportStatus status;

	public enum HmcReportStatus {
		SUCSESS,//
		INTERRUPTED,//
		FAILURE
	}
}
