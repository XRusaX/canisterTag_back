package ru.aoit.hmc.test.shared;

import ru.nppcrts.common.shared.cd.UILabel;

// НЕ НУЖНО НИЧЕГО УДАЛЯТЬ ИЗ ЭТОГО КЛАССА
public enum HmcTestType {
	@UILabel(label = "Часы")
	RTC("Часы"), //
	@UILabel(label = "Термодатчик")
	THERMO("Термодатчик"), //
	@UILabel(label = "Моточасы")
	WORK_TIME("Моточасы"), //
	@UILabel(label = "Компрессор")
	RELAY("Компрессор"), //
	@UILabel(label = "Wi-fi")
	WIFI("Wi-fi"), //
	@UILabel(label = "Вентиляторы")
	FAN("Вентиляторы"), //
	@UILabel(label = "Датчик уровня жидкости")
	CAP_SENSOR("Датчик уровня жидкости"), //
	@UILabel(label = "Светодиод")
	LED("Светодиод"), //
	@UILabel(label = "Звук")
	BUZZ("Звук"), //
	@UILabel(label = "Кнопки")
	DEMUX("Кнопки"), //
	@UILabel(label = "READER")
	READER("Чтение меток"), //
	@UILabel(label = "Планшет")
	TABLET("Планшет"), //
	@UILabel(label = "Память")
	FLASH("Память"), //
	@UILabel(label = "Режим работы")
	MODE("Режим работы"),;

	public String label;

	HmcTestType(String string) {
		this.label = string;
	} //
}