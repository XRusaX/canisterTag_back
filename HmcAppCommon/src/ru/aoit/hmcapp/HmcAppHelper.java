package ru.aoit.hmcapp;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.Database2.DBSelect.Dir;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.rfid.Report;
import ru.nppcrts.common.shared.Severity;

@Component
public class HmcAppHelper {
	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;


	public Hmc getCreateHmc(EntityManager em, String hmcSerialNumber) {
		Hmc hmc = getHmc(em, hmcSerialNumber);
		if (hmc != null)
			return hmc;

		hmc = new Hmc(hmcSerialNumber, null);
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
