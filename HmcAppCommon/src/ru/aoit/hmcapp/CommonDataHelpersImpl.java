package ru.aoit.hmcapp;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aoit.appcommon.AuthImpl;
import ru.aoit.appcommon.CommonDataImpl;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.rfid.Quota;
import ru.aoit.hmcdb.shared.rfid.Report;

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
