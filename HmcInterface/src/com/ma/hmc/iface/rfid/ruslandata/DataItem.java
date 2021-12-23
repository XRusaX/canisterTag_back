package com.ma.hmc.iface.rfid.ruslandata;

public class DataItem {
	public Tag tag;
	public Object value;

	public DataItem(Tag tag, Object value) {
		if (tag.type == ValType.TYPE_CHAR && !(value instanceof String))
			throw new IllegalArgumentException(tag.tagName + ": не текст");
		if (tag.type == ValType.TYPE_UINT && tag.sizeBytes < 8 && !(value instanceof Integer)
				&& !(value instanceof Long))
			throw new IllegalArgumentException(tag.tagName + ": не число");
		if (tag.type == ValType.TYPE_UINT && tag.sizeBytes >= 8 && !(value instanceof byte[]))
			throw new IllegalArgumentException(tag.tagName + ": не массив байт");
		this.tag = tag;
		this.value = value;
	}

	public enum Tag {
		CAN_UNIQUE_ID(39, "Уникальный номер метки", ValType.TYPE_UINT, 4), //
		CAN_VOLUME_ML(4, "Объём, мл", ValType.TYPE_UINT, 2), //
		CAN_MANUFACTURER_NAME(5, "Производитель", ValType.TYPE_CHAR, 14), //
		CAN_DESINFICTANT_NAME(10, "Наименование дезинфиктанта", ValType.TYPE_CHAR, 14), //
		CAN_ACTIVE_INGRIDIENT_NAME(8, "Активное вещество", ValType.TYPE_CHAR, 14), //
		CAN_INGRIDIENT_CONCENTRATION(38, "Концентрация активного вещества", ValType.TYPE_UINT, 2), //
		CAN_DIGIT_SIG(40, "Концентрация активного вещества", ValType.TYPE_UINT, 64),
		TAG_UID(50, "UID", ValType.TYPE_UINT, 4),
		TAG_REQ(51, "REQ", ValType.TYPE_UINT, 4),
		TAG_ACK(52, "ACK", ValType.TYPE_UINT, 4),
		TAG_NAK(53, "NAK", ValType.TYPE_UINT, 4),
		TAG_RSP(54, "RSP", ValType.TYPE_UINT, 4),
		TAG_MSG(55, "MSG", ValType.TYPE_UINT, 4),
		TAG_ID(56, "ID", ValType.TYPE_UINT, 4);
		
		public int tagNumber;
		public String tagName;
		public ValType type;
		public int sizeBytes;

		Tag(int tagNumber, String tagName, ValType type, int sizeBytes) {
			this.tagNumber = tagNumber;
			this.tagName = tagName;
			this.type = type;
			this.sizeBytes = sizeBytes;
		}
	}

	public enum ValType {
		TYPE_UINT, TYPE_CHAR;
	}

}
