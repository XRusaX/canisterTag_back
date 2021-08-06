package ru.aoit.hmcserver.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ru.aoit.hmcserver.shared.Company;
import ru.aoit.hmcserver.shared.User;

public class JpaTest {

	public static void main(String[] args) {
		createDatabase();
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HTEST");
		
		///////////////////////////////////////////
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();

		Company company = new Company("cccc");
		User user = new User("tttt");
		
		user.company = company;
		
		entityManager.persist(user);
		
		entityManager.getTransaction().commit();
		entityManager.close();

		///////////////////////////////////////////

		entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		
		Company company2 = entityManager.find(Company.class, 1L);
		entityManager.remove(company2);
		
		entityManager.getTransaction().commit();
		entityManager.close();
		
		
		try {
			
			
			
//			User user = new User("tttt");
//			entityManager.getTransaction().begin();
//			entityManager.persist(user);
//			entityManager.createNativeQuery("insert into User set name='rrr'").executeUpdate();
//			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		entityManager.close();
		entityManagerFactory.close();
	}
	
	static void createDatabase() {
		String params = "?allowPublicKeyRetrieval=true"//
				+ "&serverTimezone=UTC"//
				+ "&useUnicode=yes"//
				+ "&characterEncoding=UTF8"//
				+ "&userstat=1"//
				+ "&useLegacyDatetimeCode=false"//
				+ "&useSSL=false";
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.56.10" + params, "HTEST",
				"HTEST"); Statement stmt = conn.createStatement();) {
			stmt.executeUpdate(
					"CREATE DATABASE IF NOT EXISTS " + "HTEST" + " CHARACTER SET utf8 COLLATE utf8_bin");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
