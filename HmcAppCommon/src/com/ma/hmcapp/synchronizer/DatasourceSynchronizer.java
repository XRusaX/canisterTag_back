package com.ma.hmcapp.synchronizer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Id;

import com.ma.appcommon.datasource.DataSource;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.shared.Filter;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.shared.synchronizer.CompanyField;
import com.ma.hmcapp.shared.synchronizer.ModifTime;
import com.ma.hmcapp.shared.synchronizer.Name;
import com.ma.hmcapp.shared.synchronizer.StoreTime;

public class DatasourceSynchronizer<T> extends Synchronizer<T> {

	private DataSource<T> dataSource;

	public DatasourceSynchronizer(DataSource<T> dataSource, Company company) {
		super(company);
		this.dataSource = dataSource;
	}

	private Stream<Field> getAnnotatedFields(T t, Class<? extends Annotation> clazz) {
		Stream<Field> fields = Arrays.stream(t.getClass().getDeclaredFields())
				.filter(f -> f.getAnnotation(clazz) != null);
		return fields;
	}
	
	private Field getAnnotatedField(T t, Class<? extends Annotation> clazz) {
		Field field = getAnnotatedFields(t, clazz).findFirst().orElse(null);
		field.setAccessible(true);
		return field;
	}

	@Override
	protected long getId(T t) {
		try {
			return getAnnotatedField(t, Id.class).getLong(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void setId(T t, long id) {
		try {
			getAnnotatedField(t, Id.class).setLong(t, id);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected String getName(T t) {
		try {
			return (String) getAnnotatedField(t, Name.class).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Date getModifTime(T t) {
		try {
			return (Date) getAnnotatedField(t, ModifTime.class).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Date getStoreTime(T t) {
		try {
			return (Date) getAnnotatedField(t, StoreTime.class).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Company getCompany(T t) {
		try {
			return (Company) getAnnotatedField(t, CompanyField.class).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<T> getModified(EM em, Date lastSync) {
		return dataSource.loadRange(em, new Filter().addEQ("company", company.getId() + ""), null).right.stream()
				.filter(o -> getStoreTime(o).after(lastSync)).collect(Collectors.toList());
	}

	@Override
	protected T load(EM em, Long id) {
		return dataSource.load(em, id);
	}

	@Override
	protected T loadByName(EM em, String name) {
		return dataSource.loadRange(em, new Filter().addEQ("company", company.getId() + ""), null).right.stream()
				.filter(o -> getName(o).equals(name)).findFirst().orElse(null);
	}

	@Override
	protected void store(EM em, T t) {
		dataSource.store(em, t);
	}

}
