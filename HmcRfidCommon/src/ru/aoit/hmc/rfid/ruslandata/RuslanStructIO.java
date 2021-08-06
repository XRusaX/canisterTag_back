package ru.aoit.hmc.rfid.ruslandata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;

import ru.nppcrts.common.Dump;

public class RuslanStructIO {

	public static void write(Object obj, OutputStream os) throws IllegalAccessException, IOException {

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			Tag rfidTag = field.getAnnotation(Tag.class);

			if (rfidTag == null)
				continue;

			Class<?> type = field.getType();

			Object value = field.get(obj);

			if (value == null)
				continue;

			if (type == Integer.class) {
				if (rfidTag.len() > 4)
					throw new IllegalArgumentException(field.getName() + ": len() > 4");
				os.write(rfidTag.tag());
				int v = (Integer) value;
				for (int i = 0; i < rfidTag.len(); i++)
					os.write(v >> (i * 8));
			} else if (type == Long.class) {
				if (rfidTag.len() > 8)
					throw new IllegalArgumentException(field.getName() + ": len() > 8");
				if (rfidTag.len() <= 4)
					throw new IllegalArgumentException(field.getName() + ": len() <= 4");
				os.write(rfidTag.tag());
				long v = (Long) value;
				for (int i = 0; i < rfidTag.len(); i++)
					os.write((int) (v >> (i * 8)));
			} else if (type == String.class) {
				os.write(rfidTag.tag());
				String v = (String) value;
				if (v.length() > rfidTag.len())
					throw new IllegalArgumentException(field.getName() + ": string length > len()");
				byte[] bytes = v.getBytes("Cp1251");
				for (int i = 0; i < rfidTag.len(); i++)
					os.write(i < bytes.length ? bytes[i] : 0);
			} else if (type == byte[].class) {
				os.write(rfidTag.tag());
				byte[] v = (byte[]) value;
				if (v.length != rfidTag.len())
					throw new IllegalArgumentException(field.getName() + ": byte array length != len()");
				os.write(v);
			} else {
				throw new IllegalArgumentException("unsupperted field type " + type.getName());
			}
		}

	}

	public static <T> T read(InputStream is, Class<T> clazz)
			throws InstantiationException, IllegalAccessException, IOException {
		Field[] fields = clazz.getDeclaredFields();

		T obj = clazz.newInstance();

		Map<Integer, Field> map = Arrays.stream(fields).filter(f -> f.getAnnotation(Tag.class) != null)//
				.collect(Collectors.toMap(f -> f.getAnnotation(Tag.class).tag(), f -> f));

		int c;
		while ((c = is.read()) != -1) {
			int tag = c & 0xFF;
			Field field = map.get(tag);
			if (field == null)
				throw new IOException(String.format("unexpected tag 0x%02X", tag));

			Tag rfidTag = field.getAnnotation(Tag.class);

			int len = rfidTag.len();
			byte[] bytes = new byte[len];
			int cnt = is.read(bytes);
			if (cnt != len)
				throw new IOException("unexpected EOF");

			Class<?> type = field.getType();

			if (type == Integer.class) {
				if (rfidTag.len() > 4)
					throw new IllegalArgumentException(field.getName() + ": len() > 4");
				int v = 0;
				for (int i = 0; i < len; i++)
					v |= ((bytes[i] & 0xFF) << (i * 8));
				field.set(obj, v);
			} else if (type == Long.class) {
				if (rfidTag.len() > 8)
					throw new IllegalArgumentException(field.getName() + ": len() > 8");
				if (rfidTag.len() <= 4)
					throw new IllegalArgumentException(field.getName() + ": len() <= 4");
				long v = 0;
				for (int i = 0; i < len; i++)
					v |= ((long) (bytes[i] & 0xFF) << (i * 8));
				field.set(obj, v);
			} else if (type == String.class) {
				int l = 0;
				for (int i = 0; i < len; i++) {
					if (bytes[i] == 0)
						break;
					l = i + 1;
				}
				String str = (String) field.get(obj);
				if (str == null)
					str = "";
				str += new String(Arrays.copyOf(bytes, l), "Cp1251");
				field.set(obj, str);
			} else if (type == byte[].class) {
				field.set(obj, bytes);
			} else {
				throw new IllegalArgumentException("unsupperted field type " + type.getName());
			}
		}

		return obj;
	}

	public static byte[] write(Object obj) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			write(obj, os);
		} catch (IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
		return os.toByteArray();
	}

	public static <T> T read(byte[] bytes, Class<T> clazz) {
		try {
			return read(new ByteArrayInputStream(bytes), clazz);
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	static class Struct {
		@Tag(tag = 4, len = 4)
		public Integer CANISTER_VOLUME_ML;
		@Tag(tag = 0x0A, len = 14)
		public String CANISTER_NAME;
		@Tag(tag = 0x27, len = 4)
		public Integer UNIQUE_ID;
	}

	public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException {
		Struct struct = new Struct();

		struct.CANISTER_VOLUME_ML = 12345678;
		struct.CANISTER_NAME = "name";

		byte[] bytes = write(struct);

		Dump.print(bytes);

		Struct s = read(bytes, Struct.class);

		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(s));
	}

}
