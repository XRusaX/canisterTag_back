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
	public String userName;

	public Long userId;

//	@UILabel(label = "company", nullable = true)
//	public String companyName;
//
//	public Long companyId;

	@UILabel(label = "room", nullable = true)
	public String roomName;

	public Long roomId;

	@UILabel(label = "status")
	public HmcReportStatus status;
}
