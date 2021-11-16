package com.ma.hmc.iface.report;

public class HmcReport {
	public String hmcSerialNumber;

	public long time;

	public Integer engineTimeS;

	public Integer durationS;

	public Integer canisterId;

	public Integer consumptionML;

	public Integer remainML;

	public Integer cleaningId;

	public String operatorName;

	public Long operatorId;

	public String roomName;

	public Long roomId;

	/*
	 * значения этого поля должны быть из списка значений HmcReportStatus
	 */

	public String status;

	public HmcReport() {
	}

	public HmcReport(String serialNum) {
		hmcSerialNumber = serialNum;
	}

	public static HmcReport cleaningOk(String serialNum, Integer cleaningId, Integer canisterId, Integer spent,
			Integer remainML, String roomName, String operatorName) {
		HmcReport report = new HmcReport(serialNum);

		report.status = "SUCSESS";
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
