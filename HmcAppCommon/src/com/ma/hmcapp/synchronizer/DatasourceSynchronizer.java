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
import com.ma.hmcdb.shared.Company;

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

	@Override
	protected long getId(T t) {
		try {
			return getAnnotatedFields(t, Id.class).findFirst().orElse(null).getLong(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void setId(T t, long id) {
		try {
			getAnnotatedFields(t, Id.class).findFirst().orElse(null).setLong(t, id);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected String getName(T t) {
		try {
			return (String) getAnnotatedFields(t, Name.class).findFirst().orElse(null).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Date getModifTime(T t) {
		try {
			return (Date) getAnnotatedFields(t, ModifTime.class).findFirst().orElse(null).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Date getStoreTime(T t) {
		try {
			return (Date) getAnnotatedFields(t, StoreTime.class).findFirst().orElse(null).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Company getCompany(T t) {
		try {
			return (Company) getAnnotatedFields(t, CompanyField.class).findFirst().orElse(null).get(t);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<T> getModified(EM em, Date lastSync) {
		return dataSource.loadRange(em, new Filter().addEQ("company", company.id + ""), null).right.stream()
				.filter(o -> getStoreTime(o).after(lastSync)).collect(Collectors.toList());
	}

	@Override
	protected T load(EM em, Long id) {
		return dataSource.load(em, id);
	}

	@Override
	protected T loadByName(EM em, String name) {
		return dataSource.loadRange(em, new Filter().addEQ("company", company.id + ""), null).right.stream()
				.filter(o -> getName(o).equals(name)).findFirst().orElse(null);
	}

	@Override
	protected void store(EM em, T t) {
		dataSource.store(em, t);
	}

}
