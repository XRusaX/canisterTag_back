package com.ma.hmcapp;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.Database2;
import com.ma.appcommon.Database2.DBSelect.Dir;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.rfid.Report;

@Component
public class HmcAppHelper {
	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;


	public Hmc getCreateHmc(EntityManager em, HmcType hmcType, String hmcSerialNumber) {
		Hmc hmc = getHmc(em, hmcSerialNumber);
		if (hmc != null)
			return hmc;

		hmc = new Hmc(hmcType, hmcSerialNumber, null);
		em.persist(hmc);
		database.incrementTableVersion(Hmc.class);

		msgLogger.add(null, Severity.INFO, "МГЦ " + hmcSerialNumber + " автоматически добавлен");

		return hmc;
	}


	public Hmc getHmc(EntityManager em, String hmcSerialNumber) {
		Hmc hmc = Database2.select(em, Hmc.class).whereEQ("serialNumber", hmcSerialNumber).getResultStream().findFirst()
				.orElse(null);
		return hmc;
	}

	public Report getLastReport(EntityManager em, String serNum) {
		Hmc hmc = getHmc(em, serNum);
		if (hmc == null)
			return null;
		Report report = getLastReport(em, hmc);
		return report;
	}

	public Report getLastReport(EntityManager em, Hmc hmc) {
		Report report = Database2.select(em, Report.class).whereEQ("hmc", hmc).orderBy("time", Dir.DESC)
				.getResultStream().findFirst().orElse(null);
		return report;
	}


	
}
