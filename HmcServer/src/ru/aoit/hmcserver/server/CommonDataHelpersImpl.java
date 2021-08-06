package ru.aoit.hmcserver.server;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.nppcrts.common.gwt.app.server.commondata.CommonDataHelper;
import ru.nppcrts.common.gwt.app.server.commondata.CommonDataHelpers;
import ru.nppcrts.common.gwt.auth.server.AuthComponent;

@Component
public class CommonDataHelpersImpl implements CommonDataHelpers {

	@Autowired
	private AuthComponent authComponent;

	@Override
	public <T> CommonDataHelper<T> getHelper(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBeforeStore(HttpSession session, EntityManager entityManager, Object obj) {
	}

	@Override
	public void onAfterStore(HttpSession httpSession, EntityManager entityManager, Object obj) {
	}

	@Override
	public void onAfterCreate(HttpSession session, Object obj) {
//		if (obj instanceof Quota) {
//			Quota quota = (Quota) obj;
//			quota.userName = authComponent.getUser(session).name;
//			quota.time = new Date();
//		}
	}

}
