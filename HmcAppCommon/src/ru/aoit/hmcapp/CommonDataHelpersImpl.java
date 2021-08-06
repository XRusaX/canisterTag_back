package ru.aoit.hmcapp;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aoit.appcommon.AuthImpl;
import ru.aoit.appcommon.CommonDataHelper;
import ru.aoit.appcommon.CommonDataHelpers;
import ru.aoit.hmcdb.shared.rfid.Quota;

@Component
public class CommonDataHelpersImpl implements CommonDataHelpers {

	@Autowired
	private AuthImpl authComponent;

	@Override
	public void onBeforeStore(EntityManager conn, Object obj) {
	}

	@Override
	public void onAfterStore(EntityManager conn, Object obj) {
	}

	@Override
	public void onAfterCreate(Object obj) {
		if (obj instanceof Quota) {
			Quota quota = (Quota) obj;
			quota.userName = authComponent.getUser().name;
			quota.time = new Date();
		}
	}

	@Override
	public <T> CommonDataHelper<T> getHelper(Class<T> clazz) {
		return null;
	}

}
