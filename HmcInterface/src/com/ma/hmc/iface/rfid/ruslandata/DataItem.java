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

	public enum ValType {
		TYPE_UINT, TYPE_CHAR, TYPE_EMPTY;
	}

	public enum RfidArea {
		NO_AREA, CON_AREA, MAN_AREA, SEC_AREA, DEV_AREA;
	}

}
