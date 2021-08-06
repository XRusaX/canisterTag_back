package ru.aoit.hmc.rfid.ruslandata;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

public class RfidDataReflection {

	private RfidData data;

	public RfidDataReflection(RfidData data) {
		this.data = data;
	}

	public void printFields() {
		Class<? extends RfidData> dataClass = data.getClass();

		Field[] fields = dataClass.getDeclaredFields();

		for (Field field : fields) {
			try {
				Object fieldValue = null;
				if (field.getType() == byte[].class) {
					byte[] src = (byte[]) field.get(data);
					if (src != null)
						fieldValue = Base64.getEncoder().encodeToString(src);
				} else
					fieldValue = field.get(data);

				if (fieldValue != null) {
					String comment = field.getAnnotation(Tag.class).comment();
					System.out.println((comment.isEmpty() ? field.getName() : comment) + " : " + fieldValue);
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public String getData() {
		StringJoiner joiner = new StringJoiner("\n");
		Class<? extends RfidData> dataClass = data.getClass();

		Field[] fields = dataClass.getDeclaredFields();

		for (Field field : fields) {
			try {
				Object fieldValue = null;
				if (field.getType() == byte[].class) {
					byte[] src = (byte[]) field.get(data);
					if (src != null)
						fieldValue = Base64.getEncoder().encodeToString(src);
				} else
					fieldValue = field.get(data);

				if (fieldValue != null) {
					String comment = field.getAnnotation(Tag.class).comment();
					joiner.add((comment.isEmpty() ? field.getName() : comment) + " : " + fieldValue);
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return joiner.toString();
	}

	public LinkedHashMap<String, String> getStruct() {
		LinkedHashMap<String, String> struct = new LinkedHashMap<>();
		Class<? extends RfidData> dataClass = data.getClass();
		try {
			Field[] fields = dataClass.getDeclaredFields();

			for (Field field : fields) {
				Object fieldValue = null;
				if (field.getType() == byte[].class) {
					byte[] src = (byte[]) field.get(data);
					if (src != null) {
						StringBuilder sb = new StringBuilder();
						for (byte b : src) {
							sb.append(String.format("%02X ", b));
						}
						fieldValue = sb.toString() + "\n" + Base64.getEncoder().encodeToString(src);
					}
				} else
					fieldValue = field.get(data);

				if (fieldValue != null) {
					String comment = field.getAnnotation(Tag.class).comment();
					struct.put((comment.isEmpty() ? field.getName() : comment),
							field.getName().equals("UID") ? String.format("%02X", fieldValue) : fieldValue.toString());
				}
			}
			return struct;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return struct;
	}

	public String getTagID() {
		Class<? extends RfidData> dataClass = data.getClass();
		try {
			Field[] fields = dataClass.getDeclaredFields();

			for (Field field : fields) {
				if (field.getName().equals("UNIQUE_ID")) {
					Object fieldValue = field.get(data);
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
		HashMap<String, String> struct = getStruct();
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
