package com.ma.hmcapp.shared;

import com.ma.commonui.shared.annotations.UILabel;

public enum HmcReportStatus {
	@UILabel(label = "Успешно")
	SUCSESS, //
	@UILabel(label = "Работа прервана")
	INTERRUPTED, //
	// FAILURE,

	I2C_FAILURE, //
	DEMULTIPLEXOR_FAILURE, //
	AIR_IN_SYSTEM_FAILURE, //
	RTC_FAILURE, //
	UART_FAILURE, //
	FLASH_RW_FAILURE, //
	RFID_RW_FAILURE,//
}