package ru.aoit.hmcrfidserver.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.Database2.DBSelect.Dir;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.rfid.Report;

public class Test2 {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("HTEST");
		EntityManager em = emf.createEntityManager();

		Hmc hmc = em.find(Hmc.class, 3L);

		Report report = Database2.select(em, Report.class).whereEQ("hmc", hmc).orderBy("time", Dir.DESC)
				.getResultStream().findFirst().orElse(null);

		System.out.println();
		System.out.println(report);

		em.close();
		emf.close();
	}
}
