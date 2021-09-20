package com.ma.hmcapp;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import com.ma.appcommon.CommonDataHelper;
import com.ma.appcommon.CommonDataHelpers;

public class CommonDataHelpersBase implements CommonDataHelpers {
	private Map<Class<?>, CommonDataHelper<?>> helpers = new HashMap<>();

	@Override
	public void onBeforeStore(EntityManager conn, Object obj) {
	}

	@Override
	public void onAfterStore(EntityManager conn, Object obj) {
	}

	@Override
	public void onAfterCreate(Object obj) {
	}

	@Override
	public void onLoad(EntityManager entityManager, Object obj) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> CommonDataHelper<T> getHelper(Class<T> clazz) {
		return (CommonDataHelper<T>) helpers.get(clazz);
	}

	public <T> void addHelper(Class<T> clazz, CommonDataHelper<T> helper) {
		helpers.put(clazz, helper);
	}

}
