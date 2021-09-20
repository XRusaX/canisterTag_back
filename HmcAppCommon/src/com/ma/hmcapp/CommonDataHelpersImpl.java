package com.ma.hmcapp;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.AuthImpl;
import com.ma.appcommon.CommonDataImpl;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.rfid.Quota;
import com.ma.hmcdb.shared.rfid.Report;

@Component
public class CommonDataHelpersImpl extends CommonDataHelpersBase {

	@Autowired
	private AuthImpl authComponent;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@Autowired
	private HmcAppHelper hmcAppHelper;

	@Override
	public void onAfterCreate(Object obj) {
		if (obj instanceof Quota) {
			Quota quota = (Quota) obj;
			quota.userName = authComponent.getUser().name;
			quota.time = new Date();
		}
	}

	@Override
	public void onLoad(EntityManager em, Object obj) {
		try {
			if (obj instanceof Hmc) {
				Hmc hmc = (Hmc) obj;
				Report report = hmcAppHelper.getLastReport(em, hmc);
				if (report != null)
					hmc.status = report.status;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@PostConstruct
	private void init() {
	}
}
