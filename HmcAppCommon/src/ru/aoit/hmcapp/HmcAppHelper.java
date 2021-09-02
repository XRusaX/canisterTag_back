package ru.aoit.hmcapp;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmcdb.shared.Hmc;
import ru.nppcrts.common.shared.Severity;

@Component
public class HmcAppHelper {
	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;


	public Hmc getCreateHmc(EntityManager em, String hmcSerialNumber) {
		Hmc hmc = Database2.select(em, Hmc.class).whereEQ("serialNumber", hmcSerialNumber).getResultStream().findFirst()
				.orElse(null);
		if (hmc != null)
			return hmc;

		hmc = new Hmc(hmcSerialNumber, null);
		em.persist(hmc);
		database.incrementTableVersion(Hmc.class);

		msgLogger.add(null, Severity.INFO, "МГЦ " + hmcSerialNumber + " автоматически добавлен");

		return hmc;
	}


	
}
