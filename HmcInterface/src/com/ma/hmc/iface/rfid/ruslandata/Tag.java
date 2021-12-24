package com.ma.hmc.iface.rfid.ruslandata;

import com.ma.hmc.iface.rfid.ruslandata.DataItem.RfidArea;
import com.ma.hmc.iface.rfid.ruslandata.DataItem.ValType;

public enum Tag {
	TAG_ZERO(0, "Ноль", ValType.TYPE_EMPTY, RfidArea.NO_AREA, 0), //
	TAG_FINAL(1, "Конец Данных", ValType.TYPE_EMPTY, RfidArea.NO_AREA, 0), //
	TAG_RFIDTYPE(2, "Тип RFID", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_CAN_VERSION(3, "Версия данных", ValType.TYPE_UINT, RfidArea.MAN_AREA, 1), //
	TAG_CAN_VOLUME_ML(4, "Объём, мл", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_MANUFACTURER_NAME(5, "Производитель", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_CAN_ISSUE_DATE_YYMMDD(6, "Дата выпуска", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 6), //
	TAG_CAN_EXPIRATION_DATE_YYMMDD(7, "Дата годности", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 6), //
	TAG_CAN_ACTIVE_INGRIDIENT_NAME(8, "Активное вещество", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_CAN_RESIDUAL_VOLUME_ML(9, "Остаток, мл", ValType.TYPE_UINT, RfidArea.DEV_AREA, 2), //
	TAG_CAN_DESINFICTANT_NAME(10, "Наименование дезинфиктанта", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_CAN_ABBR(11, "Аббревиатура", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_CAN_CONSUMPTION_ML_M3(12, "Расход, мл/м3", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_CONSUMPTION2_ML_M3(13, "Расход для промывки, мл/м3", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_AERATION_MIN(14, "Аэрация, мин", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_AREA1_SECTOR(15, "Контрольная область", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_AREA2_SECTOR(16, "Область производителя", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_AREA3_SECTOR(17, "Область устройства", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_AREA4_SECTOR(18, "Область безопасности", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_LAST_SECTOR(19, "Последний сектор", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_SWITCH(20, "Страница тэгов", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_ROOM_CARD_VERSION(21, "Версия данных помещения", ValType.TYPE_UINT, RfidArea.MAN_AREA, 1), //
	TAG_ROOM_VOLUME_M2(22, "Обьем помещения, м2", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_ROOM_ORGANIZATION_NAME(23, "Наименование организации", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_ROOM_NAME(24, "Наименование комнаты", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_ROOM_ID(25, "ID помещения", ValType.TYPE_UINT, RfidArea.MAN_AREA, 4), //
	TAG_ROOM_TYPE(26, "Тип помещения", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 2), //
	TAG_PERSON_CARD_VERSION(27, "Версия данных персонала", ValType.TYPE_UINT, RfidArea.MAN_AREA, 1), //
	TAG_PERSON_ORGANIZATION_NAME(28, "Наименование организации", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_PERSON_ORGANIZATION_ID(29, "ID организации", ValType.TYPE_UINT, RfidArea.MAN_AREA, 4), //
	TAG_PERSON_NAME(30, "Имя сотрудника", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 30), //
	TAG_PERSON_SURNAME(31, "Фамилия сотрудника", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_PERSON_ID(32, "ID сотрудника", ValType.TYPE_UINT, RfidArea.MAN_AREA, 4), //
	TAG_PERSON_POSITION(33, "Должность сотрудника", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_PERSON_ACCESS(34, "Уровень доступа", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_PERSON_PIN_NUM(35, "Пин код", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_RFID_MANUFACTURER_NAME(36, "Наименование производителя метки", ValType.TYPE_CHAR, RfidArea.SEC_AREA, 14), //
	TAG_CAN_RFID_ISSUE_DATE_YYMMDD(37, "Дата производства метки", ValType.TYPE_CHAR, RfidArea.SEC_AREA, 6), //
	TAG_CAN_INGRIDIENT_CONCENTRATION(38, "Концентрация активного вещества", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_UNIQUE_ID(39, "Уникальный номер метки", ValType.TYPE_UINT, RfidArea.SEC_AREA, 4), //
	TAG_CAN_DIGIT_SIG(40, "Цифровая подпись", ValType.TYPE_UINT, RfidArea.SEC_AREA, 64), //
	TAG_DEVICE_NAME(41, "Имя прибора", ValType.TYPE_CHAR, RfidArea.NO_AREA, 14), //
	TAG_DEVICE_CAN_UNIQUE_ID(42, "Уникальный номер прибора", ValType.TYPE_UINT, RfidArea.NO_AREA, 8), //
	TAG_WIFI_HOSTNAME(43, "Имя точки доступа", ValType.TYPE_CHAR, RfidArea.NO_AREA, 16), //
	TAG_WIFI_PASSWORD(44, "Пароль точки доступа", ValType.TYPE_CHAR, RfidArea.NO_AREA, 10), //
	TAG_WORK_MINUTES(45, "Счетчик моточасов", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_REPORTS_START_PAGE(46, "Начальная страница отчета", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_REPORTS_STOP_PAGE(47, "Конечная страница отчета", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_REPORT_USED_PAGE(48, "Указатель на страницу записи", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_REPORT_FREE_PAGE(49, "Указатель свободную страницу", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_UID(50, "UID", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_REQ(51, "REQ", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_ACK(52, "ACK", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_NAK(53, "NAK", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_RSP(54, "RSP", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_MSG(55, "MSG", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_ID(56, "ID", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_TEXT(57, "Текстовое сообщение", ValType.TYPE_CHAR, RfidArea.NO_AREA, 64), //
	TAG_START_TREATMENT_TIME(58, "unix time", ValType.TYPE_UINT, RfidArea.NO_AREA, 8), //
	TAG_COUNTER_SEC(59, "Время обработки", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_COUNTER_LIQ_ML(60, "Количество жижи", ValType.TYPE_UINT, RfidArea.NO_AREA, 2), //
	TAG_TREATMENT_STATE(61, "Статус обработки", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_ALARM_IDX(62, "индекс таймера", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_ALARM_STATE(63, "Статус таймера", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_PERIODIKAL(64, "периодичность таймера", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_DAYTIME_SEC(65, "время дня", ValType.TYPE_UINT, RfidArea.NO_AREA, 4), //
	TAG_WEEKDAY(66, "день недели", ValType.TYPE_UINT, RfidArea.NO_AREA, 1), //
	TAG_ROOM_HEIGHT_CM(67, "высота потолков", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_WORK_MODE_ID(68, "id режима обработки", ValType.TYPE_UINT, RfidArea.MAN_AREA, 1), //
	TAG_CAN_WORK_MODE_NAME(69, "название режима обработки", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 14), //
	TAG_CAN_CONSUMPTION_ML(70, "расход жижи на м3", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_EXPOSURE_SEC(71, "время экспозиции", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_AIRING_SEC(72, "время проветривания", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_IMPULSE_PERIOD_SEC(73, "период импульса", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CAN_IMPULSE_WIDTH_SEC(74, "ширина импульса", ValType.TYPE_UINT, RfidArea.MAN_AREA, 2), //
	TAG_CODE_PAGE_ID(75, "Идентификатор кодовой страницы", ValType.TYPE_UINT, RfidArea.CON_AREA, 1), //
	TAG_BATCH_NUMBER(76, "Номер партии", ValType.TYPE_CHAR, RfidArea.MAN_AREA, 20); //

	public int tagNumber;
	public String tagName;
	public ValType type;
	public RfidArea area;
	public int sizeBytes;

	Tag(int tagNumber, String tagName, ValType type, RfidArea area, int sizeBytes) {
		this.tagNumber = tagNumber;
		this.tagName = tagName;
		this.type = type;
		this.area = area;
		this.sizeBytes = sizeBytes;
	}
}