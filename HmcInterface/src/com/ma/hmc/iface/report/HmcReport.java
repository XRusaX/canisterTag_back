package com.ma.hmc.iface.report;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmc.iface.report.shared.HmcReportStatus;
import com.ma.hmc.iface.shared.HmcType;

public class HmcReport {
	@UILabel(label = "hmcType")
	public HmcType hmcType;

	@UILabel(label = "SerialNumber")
	public String hmcSerialNumber;

	public long time;
	
	@UILabel(label = "engineTimeMS")
	public Integer engineTimeS;

	@UILabel(label = "durationS")
	public Integer durationS;

	@UILabel(label = "canisterId")
	public Integer canisterId;

	@UILabel(label = "consumtionML")
	public Integer consumptionML;

	@UILabel(label = "remainML")
	public Integer remainML;

	@UILabel(label = "cleaningId")
	public Integer cleaningId;

	@UILabel(label = "operator")
	public String operatorName;

	public Long operatorId;

	@UILabel(label = "room")
	public String roomName;

	public Long roomId;

	@UILabel(label = "status")
	public HmcReportStatus status;

	public HmcReport() {
	}

	public HmcReport(String serialNum) {
		hmcSerialNumber = serialNum;
	}

	public static HmcReport cleaningOk(String serialNum, Integer cleaningId, Integer canisterId, Integer spent,
			Integer remainML, String roomName, String operatorName) {
		HmcReport report = new HmcReport(serialNum);

		report.status = HmcReportStatus.SUCSESS;
		report.durationS = 10;
		report.canisterId = canisterId;
		report.consumptionML = spent;
		report.remainML = remainML;
		report.cleaningId = cleaningId;
		report.roomName = roomName;
		report.operatorName = operatorName;

		return report;
	}
}
