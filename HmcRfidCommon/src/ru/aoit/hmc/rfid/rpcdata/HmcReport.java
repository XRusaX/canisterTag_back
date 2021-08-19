package ru.aoit.hmc.rfid.rpcdata;

import java.util.Date;

import ru.aoit.hmc.rfid.shared.HmcReportStatus;
import ru.nppcrts.common.shared.cd.UILabel;

public class HmcReport {
	@UILabel(label = "SerialNumber")
	public String hmcSerialNumber;
	
	public Date startTime;
	
	@UILabel(label = "durationS")
	public Integer durationS;
	
	@UILabel(label = "canisterId")
	public Integer canisterId;
	
	@UILabel(label = "consumtionML")
	public Integer consumtionML;
	
	@UILabel(label = "user", nullable = true)
	public String user;
	
	@UILabel(label = "company", nullable = true)
	public String company;
	
	@UILabel(label = "room", nullable = true)
	public String room;
	
	@UILabel(label = "status")
	public HmcReportStatus status;
}
