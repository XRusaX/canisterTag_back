package ru.aoit.hmc.test.shared;

import ru.nppcrts.common.shared.cd.UILabel;

public enum HmcTestType {
	@UILabel(label = "Часы")
	RTC, //
	@UILabel(label = "Термодатчик")
	THERMO, //
	@UILabel(label = "Моточасы")
	WORK_TIME, //
	@UILabel(label = "Компрессор")
	RELAY,
	@UILabel(label = "Wi-fi")
	WIFI,
	@UILabel(label = "Датчик уровня жидкости")
	CAP_SENSOR,
	@UILabel(label = "Светодиод")
	LED, //
	@UILabel(label = "Звук")
	BUZZ, //
	@UILabel(label = "Кнопки")
	DEMUX, //
	@UILabel(label = "READER")
	READER, //
	@UILabel(label = "Память")
	FLASH, //
}