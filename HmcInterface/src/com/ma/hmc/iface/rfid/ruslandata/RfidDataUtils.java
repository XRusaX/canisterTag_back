package com.ma.hmc.iface.rfid.ruslandata;

import java.io.UnsupportedEncodingException;

public class RfidDataUtils {
	public static byte[] getKey(RfidData data) throws UnsupportedEncodingException {
		return getKey(data.UNIQUE_ID, data.CANISTER_VOLUME_ML, data.CANISTER_MANUFACTURER_NAME, data.CANISTER_NAME,
				data.CANISTER_CONSUMPTION_ML_M3, data.CANISTER_CONSUMPTION2_ML_M3, data.CANISTER_AERATION_MIN);
	}

	public static byte[] getKey(Integer id, Integer volume, String manufacturer, String name, Integer consumption,
			Integer consumption2, Integer aeration) throws UnsupportedEncodingException {
		return ("" + id + volume + manufacturer + name + consumption + consumption2 + aeration).getBytes("utf-8");
	}
}
