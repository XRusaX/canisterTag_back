package com.ma.hmcapp.rest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class BaseController {

	protected <T> Example<T> getExample(Class<T> clazz, Map<String, String> searchParams) {
		searchParams.remove("page");
		searchParams.remove("size");
		searchParams.remove("sort");

		try {
			T probe = clazz.newInstance();
			applyParams(probe, searchParams);

			ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id");
			Example<T> e = Example.of(probe, matcher);

			return e;
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	private static void applyParams(Object probe, Map<String, String> searchParams) {
		searchParams.forEach((key, value) -> {
			try {
				Field field = probe.getClass().getField(key);

				Class<?> type = field.getType();
				if (type.equals(int.class) || type.equals(Integer.class))
					field.setInt(probe, Integer.valueOf(value));
				else if (type.equals(long.class) || type.equals(Long.class))
					field.setLong(probe, Long.valueOf(value));
				else if (type.equals(double.class) || type.equals(Double.class))
					field.setDouble(probe, Double.valueOf(value));
				else if (type.equals(String.class))
					field.set(probe, value);
				else if (type.isEnum()) {
					Enum<?>[] enumConstants = (Enum<?>[]) field.getType().getEnumConstants();
					Enum<?> v = Arrays.stream(enumConstants).filter(e -> e.name().equals(value)).findAny().orElse(null);
					field.set(probe, v);
				} else {
					Object ref = type.newInstance();
					ref.getClass().getField("id").setLong(ref, Long.valueOf(value));
					field.set(probe, ref);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	protected void shortenRefs(List<?> page) {
		page.forEach(probe -> {
			Arrays.stream(probe.getClass().getFields()).forEach(field -> {
				try {
					Class<?> type = field.getType();
					if (type.equals(int.class) || type.equals(Integer.class))
						;
					else if (type.equals(long.class) || type.equals(Long.class))
						;
					else if (type.equals(double.class) || type.equals(Double.class))
						;
					else if (type.equals(String.class))
						;
					else if (type.isEnum()) {
						;
					} else {
						Object ref = field.get(probe);
						Object ref2 = type.newInstance();
						ref2.getClass().getField("id").setLong(ref2, ref.getClass().getField("id").getLong(ref));
						field.set(probe, ref2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		});

	}

}
