package com.ma.hmc.iface.rfid.ruslandata;

import java.io.Serializable;

import com.ma.hmc.iface.rfid.rfiddata.Tag;

@SuppressWarnings("serial")
public class DataItem implements Serializable {
	public Tag tag;
	public String stringValue;
	public Integer intValue;
	public byte[] arrayValue;

	public DataItem(Tag tag, String stringValue, Integer intValue, byte[] arrayValue) {
		this.tag = tag;
		this.stringValue = stringValue;
		this.intValue = intValue;
		this.arrayValue = arrayValue;
	}

	public enum ValType {
		TYPE_UINT, TYPE_CHAR, TYPE_EMPTY;
	}

	public enum RfidArea {
		NO_AREA, CON_AREA, MAN_AREA, SEC_AREA, DEV_AREA;
	}

}
