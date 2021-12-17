package com.ma.hmc.iface.rfid.ruslandata;

import java.io.UnsupportedEncodingException;

public class RfidDataUtils {
	public static byte[] getKey(RfidData data) throws UnsupportedEncodingException {
		return getKey(data.CAN_UNIQUE_ID, data.CAN_VOLUME_ML, data.CAN_MANUFACTURER_NAME, data.CAN_DESINFICTANT_NAME,
				data.CAN_ACTIVE_INGRIDIENT_NAME, data.CAN_INGRIDIENT_CONCENTRATION);
	}

	public static byte[] getKey(Integer id, Integer volume, String manufacturer, String name,
			String activeIngridientName, Integer concentration) throws UnsupportedEncodingException {
		return ("" + id + volume + manufacturer + name + activeIngridientName + concentration).getBytes("utf-8");
	}
}
