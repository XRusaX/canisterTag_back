package com.ma.hmc.rfid.rpcdata;

import com.ma.common.shared.cd.UILabel;
import com.ma.hmc.rfid.shared.HmcReportStatus;

public class HmcReport {
	@UILabel(label = "SerialNumber")
	public String hmcSerialNumber;

	public long startTime;

	@UILabel(label = "durationS")
	public Integer durationS;

	@UILabel(label = "canisterId")
	public Integer canisterId;

	@UILabel(label = "consumtionML")
	public Integer consumptionML;

	@UILabel(label = "operator")
	public String operatorName;

	public Long operatorId;

	@UILabel(label = "room")
	public String roomName;

	public Long roomId;

	@UILabel(label = "status")
	public HmcReportStatus status;
}