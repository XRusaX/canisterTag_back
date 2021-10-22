package com.ma.hmcrfidserver.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ma.appcommon.db.Database2;
import com.ma.appcommon.db.Database2.DBSelect.Dir;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.rfid.Report;

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
