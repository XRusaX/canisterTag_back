package com.ma.hmc.iface.rfid.ruslandata;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.ma.hmc.iface.rfid.ruslandata.DataItem.Tag;

@SuppressWarnings("serial")
public class RfidData implements Serializable {
	private List<DataItem> rfidData = new ArrayList<>();

	public Object getValByTag(Tag tag) {
		for (DataItem item : rfidData) {
			if (item.tag == tag)
				return item.value;
		}
		return null;
	}

	public void setValByTag(Tag tag, Object value) {
		for (DataItem item : rfidData) {
			if (item.tag == tag)
				item.value = value;
		}
	}

	public DataItem getItemByTag(Tag tag) {
		for (DataItem item : rfidData) {
			if (item.tag == tag)
				return item;
		}
		return null;
	}

	public static byte[] getKey(RfidData data) throws UnsupportedEncodingException {
		return getKey(data.getValByTag(Tag.CAN_UNIQUE_ID), data.getValByTag(Tag.CAN_VOLUME_ML),
				data.getValByTag(Tag.CAN_MANUFACTURER_NAME), data.getValByTag(Tag.CAN_DESINFICTANT_NAME),
				data.getValByTag(Tag.CAN_ACTIVE_INGRIDIENT_NAME), data.getValByTag(Tag.CAN_INGRIDIENT_CONCENTRATION));
	}

	public static byte[] getKey(Object... data) throws UnsupportedEncodingException {
		String key = "";
		for (Object o : data) {
			key += o;
		}
		return key.getBytes("utf-8");
	}

	public void add(Tag tag, int val) {
		rfidData.add(new DataItem(tag, val));
	}

	public void add(Tag tag, String text) {
		rfidData.add(new DataItem(tag, text));
	}

	public void add(Tag tag, byte[] array) {
		rfidData.add(new DataItem(tag, array));
	}

	public List<DataItem> getRfidData() {
		return rfidData;
	}
//	public static byte[] getKey(Integer id, Integer volume, String manufacturer, String name,
//			String activeIngridientName, Integer concentration) throws UnsupportedEncodingException {
//		return ("" + id + volume + manufacturer + name + activeIngridientName + concentration).getBytes("utf-8");
//	}
	/*
	 * @Tag(tag = 2, len = 1, used = 22, comment = "Тип RFID") public Integer
	 * RFIDTYPE;
	 * 
	 * @Tag(tag = 3, len = 1, used=2, comment = "Версия данных") public Integer
	 * CAN_VERSION;
	 * 
	 * @Tag(tag = 4, len = 2, used=2, comment = "Объём, мл") public Integer
	 * CAN_VOLUME_ML;
	 * 
	 * @Tag(tag = 5, len = 14, used=2, comment = "Производитель") public String
	 * CAN_MANUFACTURER_NAME;
	 * 
	 * @Tag(tag = 6, len = 6, used=2, comment = "Дата выпуска") public String
	 * CAN_ISSUE_DATE_YYMMDD;
	 * 
	 * @Tag(tag = 7, len = 6, used=2, comment = "Дата годности") public String
	 * CAN_EXPIRATION_DATE_YYMMDD;
	 * 
	 * @Tag(tag = 8, len = 14, used=2, comment = "Активное вещество") public String
	 * CAN_ACTIVE_INGRIDIENT_NAME;
	 * 
	 * @Tag(tag = 9, len = 2, used=2, comment = "Остаток, мл") public Integer
	 * CAN_RESIDUAL_VOLUME_ML;
	 * 
	 * @Tag(tag = 10, len = 14, used=2, comment = "Наименование дезинфиктанта")
	 * public String CAN_DESINFICTANT_NAME;
	 * 
	 * @Tag(tag = 11, len = 14, used=2, comment = "Аббревиатура") public String
	 * CAN_ABBR;
	 * 
	 * @Tag(tag = 12, len = 2, used=2, comment = "Расход, мл/м3") public Integer
	 * CAN_CONSUMPTION_ML_M3;
	 * 
	 * @Tag(tag = 13, len = 2, used=2, comment = "Расход для промывки, мл/м3")
	 * public Integer CAN_CONSUMPTION2_ML_M3;
	 * 
	 * @Tag(tag = 14, len = 2, used=2, comment = "Аэрация, мин") public Integer
	 * CAN_AERATION_MIN;
	 * 
	 * @Tag(tag = 15, len = 1, used=22, comment = "Контрольная область") public
	 * Integer AREA1_SECTOR;
	 * 
	 * @Tag(tag = 16, len = 1, used=22, comment = "Область производителя") public
	 * Integer AREA2_SECTOR;
	 * 
	 * @Tag(tag = 17, len = 1, used=22, comment = "Область устройства") public
	 * Integer AREA3_SECTOR;
	 * 
	 * @Tag(tag = 18, len = 1, used=22, comment = "Область безопасности") public
	 * Integer AREA4_SECTOR;
	 * 
	 * @Tag(tag = 19, len = 1, used=22, comment = "Последний сектор") public Integer
	 * LAST_SECTOR;
	 * 
	 * @Tag(tag = 20, len = 1, used=1, comment = "Страница тэгов") public Integer
	 * SWITCH;
	 * 
	 * @Tag(tag = 21, len = 1, used=4, comment = "Версия данных помещения") public
	 * Integer ROOM_CARD_VERSION;
	 * 
	 * @Tag(tag = 22, len = 2, used=4, comment = "Обьем помещения, м2") public
	 * Integer ROOM_VOLUME_M2;
	 * 
	 * @Tag(tag = 23, len = 14, used=4, comment = "Наименование организации") public
	 * String ROOM_ORGANIZATION_NAME;
	 * 
	 * @Tag(tag = 24, len = 14, used=4, comment = "Наименование комнаты") public
	 * String ROOM_NAME;
	 * 
	 * @Tag(tag = 25, len = 4, used=4, comment = "ID помещения") public Integer
	 * ROOM_ID;
	 * 
	 * @Tag(tag = 26, len = 2, used=4, comment = "Тип помещения") public String
	 * ROOM_TYPE;
	 * 
	 * @Tag(tag = 27, len = 1, used=16, comment = "Версия данных персонала") public
	 * Integer PERSON_CARD_VERSION;
	 * 
	 * @Tag(tag = 28, len = 14, used=16, comment = "Наименование организации")
	 * public String PERSON_ORGANIZATION_NAME;
	 * 
	 * @Tag(tag = 29, len = 4, used=16, comment = "ID организации") public Integer
	 * PERSON_ORGANIZATION_ID;
	 * 
	 * @Tag(tag = 30, len = 30, used=16, comment = "Имя сотрудника") public String
	 * PERSON_NAME;
	 * 
	 * @Tag(tag = 31, len = 14, used=16, comment = "Фамилия сотрудника") public
	 * String PERSON_SURNAME;
	 * 
	 * @Tag(tag = 32, len = 4, used=16, comment = "ID сотрудника") public Integer
	 * PERSON_ID;
	 * 
	 * @Tag(tag = 33, len = 14, used=16, comment = "Должность сотрудника") public
	 * String PERSON_POSITION;
	 * 
	 * @Tag(tag = 34, len = 2, used=16, comment = "Уровень доступа") public Integer
	 * PERSON_ACCESS;
	 * 
	 * @Tag(tag = 35, len = 2, used=16, comment = "Пин код") public Integer
	 * PERSON_PIN_NUM;
	 * 
	 * @Tag(tag = 36, len = 14, used=2, comment =
	 * "Наименование производителя метки") public String CAN_RFID_MANUFACTURER_NAME;
	 * 
	 * @Tag(tag = 37, len = 6, used=2, comment = "Дата производства метки") public
	 * String CAN_RFID_ISSUE_DATE_YYMMDD;
	 * 
	 * @Tag(tag = 38, len = 2, used=2, comment = "Концентрация активного вещества")
	 * public Integer CAN_INGRIDIENT_CONCENTRATION;
	 * 
	 * @Tag(tag = 39, len = 4, used=2, comment = "Уникальный номер метки") public
	 * Integer CAN_UNIQUE_ID;
	 * 
	 * @Tag(tag = 40, len = 64, used=2, comment = "Цифровая подпись") public byte[]
	 * CAN_DIGIT_SIG;
	 * 
	 * @Tag(tag = 41, len = 14, used=32, comment = "Имя прибора") public String
	 * DEVICE_NAME;
	 * 
	 * @Tag(tag = 42, len = 8, used=32, comment = "Уникальный номер прибора") public
	 * Long DEVICE_CAN_UNIQUE_ID;
	 * 
	 * @Tag(tag = 43, len = 16, used=32, comment = "Имя точки доступа") public
	 * String WIFI_HOSTNAME;
	 * 
	 * @Tag(tag = 44, len = 10, used=32, comment = "Пароль точки доступа") public
	 * String WIFI_PASSWORD;
	 * 
	 * @Tag(tag = 45, len = 4, used=32, comment = "Счетчик моточасов") public
	 * Integer WORK_MINUTES;
	 * 
	 * @Tag(tag = 46, len = 2, used=32, comment = "Начальная страница отчета")
	 * public Integer REPORTS_START_PAGE;
	 * 
	 * @Tag(tag = 47, len = 2, used=32, comment = "Конечная страница отчета") public
	 * Integer REPORTS_STOP_PAGE;
	 * 
	 * @Tag(tag = 48, len = 2, used=32, comment = "Указатель на страницу записи")
	 * public Integer REPORT_USED_PAGE;
	 * 
	 * @Tag(tag = 49, len = 2, used=32, comment = "Указатель свободную страницу")
	 * public Integer REPORT_FREE_PAGE;
	 * 
	 * @Tag(tag = 50, len = 4, used=1, comment = "UID") public Integer UID;
	 * 
	 * @Tag(tag = 51, len = 4, used=1, comment = "REQ") public Integer REQ;
	 * 
	 * @Tag(tag = 52, len = 4, used=1, comment = "ACK") public Integer ACK;
	 * 
	 * @Tag(tag = 53, len = 4, used=1, comment = "NAK") public Integer NAK;
	 * 
	 * @Tag(tag = 54, len = 4, used=1, comment = "RSP") public Integer RSP;
	 * 
	 * @Tag(tag = 55, len = 4, used=1, comment = "MSG") public Integer MSG;
	 * 
	 * @Tag(tag = 56, len = 4, used=1, comment = "ID") public Integer ID;
	 * 
	 * @Tag(tag = 57, len = 64, used=1, comment = "Текстовое сообщение") public
	 * String TEXT;
	 * 
	 * @Tag(tag = 58, len = 8, used=1, comment = "unix time") public Long
	 * START_TREATMENT_TIME;
	 * 
	 * @Tag(tag = 59, len = 2, used=1, comment = "Время обработки") public Integer
	 * COUNTER_SEC;
	 * 
	 * @Tag(tag = 60, len = 2, used=1, comment = "Количество жижи") public Integer
	 * COUNTER_LIQ_ML;
	 * 
	 * @Tag(tag = 61, len = 1, used=1, comment = "Статус обработки") public Integer
	 * TREATMENT_STATE;
	 * 
	 * @Tag(tag = 62, len = 1, used=1, comment = "индекс таймера") public Integer
	 * ALARM_IDX;
	 * 
	 * @Tag(tag = 63, len = 1, used=1, comment = "Статус таймера") public Integer
	 * ALARM_STATE;
	 * 
	 * @Tag(tag = 64, len = 1, used=1, comment = "периодичность таймера") public
	 * Integer PERIODIKAL;
	 * 
	 * @Tag(tag = 65, len = 4, used=1, comment = "время дня") public Integer
	 * DAYTIME_SEC;
	 * 
	 * @Tag(tag = 66, len = 1, used=1, comment = "день недели") public Integer
	 * WEEKDAY;
	 * 
	 * @Tag(tag = 67, len = 2, used=4, comment = "высота потолков") public Integer
	 * ROOM_HEIGHT_CM;
	 * 
	 * @Tag(tag = 68, len = 1, used=2, comment = "id режима обработки") public
	 * Integer CAN_WORK_MODE_ID;
	 * 
	 * @Tag(tag = 69, len = 14, used=2, comment = "название режима обработки")
	 * public String CAN_WORK_MODE_NAME;
	 * 
	 * @Tag(tag = 70, len = 2, used=2, comment = "расход жижи на м3") public Integer
	 * CAN_CONSUMPTION_ML;
	 * 
	 * @Tag(tag = 71, len = 2, used=2, comment = "время экспозиции") public Integer
	 * CAN_EXPOSURE_SEC;
	 * 
	 * @Tag(tag = 72, len = 2, used=2, comment = "время проветривания") public
	 * Integer CAN_AIRING_SEC;
	 * 
	 * @Tag(tag = 73, len = 2, used=2, comment = "период импульса") public Integer
	 * CAN_IMPULSE_PERIOD_SEC;
	 * 
	 * @Tag(tag = 74, len = 2, used=2, comment = "ширина импульса") public Integer
	 * CAN_IMPULSE_WIDTH_SEC;
	 * 
	 * @Tag(tag = 75, len = 1, used=22, comment = "Идентификатор кодовой страницы")
	 * public Integer CODE_PAGE_ID;
	 * 
	 * @Tag(tag = 76, len = 20, used=22, comment = "Номер партии") public String
	 * BATCH_NUMBER;
	 */
}
