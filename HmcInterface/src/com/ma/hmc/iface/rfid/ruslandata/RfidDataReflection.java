package com.ma.hmc.iface.rfid.ruslandata;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import com.ma.hmc.iface.rfid.ruslandata.DataItem.RfidArea;
import com.ma.hmc.iface.rfid.ruslandata.DataItem.ValType;

public class RfidDataReflection {

	private RfidData data;

	public RfidDataReflection(RfidData data) {
		this.data = data;
	}

	public void printFields() {

		for (DataItem field : data.getRfidData()) {
			try {
				Object fieldValue = null;
				if (field.tag.type == ValType.TYPE_UINT) {
					if (field.tag.sizeBytes >= 8) {
						byte[] src = (byte[]) field.value;
						if (src != null)
							fieldValue = Base64.getEncoder().encodeToString(src);
					} else {
						Integer src = (Integer) field.value;
						if (src != null)
							fieldValue = src;
					}
				} else
					fieldValue = (String) field.value;

				if (fieldValue != null) {
					System.out.println(field.tag.tagName + " : " + fieldValue);
				}
			} catch (SecurityException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	private LinkedHashMap<String, String> getStruct() {
		LinkedHashMap<String, String> struct = new LinkedHashMap<>();

		try {
			for (DataItem field : data.getRfidData()) {
				Object fieldValue = null;
				if (field.tag.type == ValType.TYPE_UINT) {
					if (field.tag.sizeBytes > 4) {
						byte[] src = (byte[]) field.value;
						if (src != null) {
							StringBuilder sb = new StringBuilder();
							for (byte b : src) {
								sb.append(String.format("%02X ", b));
							}
							fieldValue = sb.toString() + "\n" + Base64.getEncoder().encodeToString(src);
						}
					} else {
						if (field.tag == Tag.TAG_UID) {
							fieldValue = (int) field.value;
							byte[] src = new byte[4];

							for (int i = 0; i < field.tag.sizeBytes; i++)
								src[i] = (byte) ((int) fieldValue >> (i * 8));

							if (src != null) {
								StringJoiner sb = new StringJoiner(":");
								for (byte b : src) {
									sb.add(String.format("%02X", b));
								}
								fieldValue = sb.toString();
							}
						} else {
							Integer src = (Integer) field.value;
							if (src != null)
								fieldValue = src;
						}
					}
				} else {
					fieldValue = field.value;
				}
				// Не добавляем в считанную структуру служебные поля кроме UID
				if ((field.tag.area == RfidArea.NO_AREA && field.tag != Tag.TAG_UID)
						|| field.tag.area == RfidArea.CON_AREA)
					continue;

				if (fieldValue != null) {
					struct.put(field.tag.tagName, fieldValue.toString());
				}
			}
			return struct;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return struct;

	}

	public String getTagID() {
		try {
			for (DataItem field : data.getRfidData()) {
				if (field.tag == Tag.TAG_CAN_UNIQUE_ID) {
					Object fieldValue = field.value;
					return Integer.toHexString((Integer) fieldValue).toUpperCase();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getHTMLTable(String hexColorCode) throws Exception {
		if (hexColorCode == null)
			hexColorCode = "#dddddd";

		String head = "<head>\n" + //
				"<style>\n" + //
				"table {\n" + //
				"  word-break: break-all;\n" + //
				"  font-family: arial, sans-serif;\n" + //
				"}\n" + //
				"\n" + //
				"td, th {\n" + //
				"  word-break: break-all;\n" + //
				"  border: 1px solid " + hexColorCode + ";\n" + //
				"  text-align: left;\n" + //
				"}\n" + //
				"\n" + //
				"th {\n" + //
				"  text-align: center;\n" + //
				"  background-color: " + hexColorCode + ";\n" + //
				"}\n" + //
				"</style>\n" + //
				"</head>"; //

		String body = "<body><table cellspacing=0><tr><th>Параметр</th><th>Значение</th></tr>";

		HashMap<String, String> struct = getStruct();
		Iterator<Entry<String, String>> it = struct.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			String innerValues = pair.getValue();
			if (pair.getValue().contains("\n")) {
				String[] split = pair.getValue().split("\n");
				innerValues = "<table>";
				for (String value : split) {
					innerValues += "<tr><td border=0>" + value + "</td></tr>";
				}
				innerValues += "</table>";
			}
			body += "<tr><td>" + pair.getKey() + "</td>" + "<td>" + innerValues + "</td>" + "</tr>";
			it.remove();
		}
		body += "</table></body>";
		return "<html>" + head + body + "</html>";
	}

}
