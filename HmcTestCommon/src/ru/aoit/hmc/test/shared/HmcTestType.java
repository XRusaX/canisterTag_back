package ru.aoit.hmc.test.shared;

import ru.nppcrts.common.shared.cd.UILabel;

// НЕ НУЖНО НИЧЕГО УДАЛЯТЬ ИЗ ЭТОГО КЛАССА
public enum HmcTestType {
	@UILabel(label = "Часы")
	RTC(), //
	@UILabel(label = "Термодатчик")
	THERMO(), //
	@UILabel(label = "Моточасы")
	WORK_TIME(), //
	@UILabel(label = "Компрессор")
	RELAY(), //
	@UILabel(label = "Wi-fi")
	WIFI(), //
	@UILabel(label = "Вентиляторы")
	FAN(), //
	@UILabel(label = "Датчик уровня жидкости")
	CAP_SENSOR(), //
	@UILabel(label = "Светодиод")
	LED(), //
	@UILabel(label = "Звук")
	BUZZ(), //
	@UILabel(label = "Кнопки")
	DEMUX(), //
	@UILabel(label = "Чтение меток")
	READER(), //
	@UILabel(label = "Планшет")
	TABLET(), //
	@UILabel(label = "Память")
	FLASH(), //
	@UILabel(label = "Режим работы")
	MODE(),;


	public static String getLabel(Enum<?> e) throws NoSuchFieldException {

		return e.getClass().getDeclaredField(e.name()).getAnnotation(UILabel.class).label();

	}
}