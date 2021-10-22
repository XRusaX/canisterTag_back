package com.ma.hmcapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.SettingsHolder;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.rfid.Report;

@Component
public class HmcDatabase extends Database2 {

	private static final String dbname = "HTEST";
	private static final String user = "HTEST";
	private static final String password = "HTEST";

	public HmcDatabase() {
		super("HTEST");
	}

	@PostConstruct
	private void init() {
		createDatabase();
		Map<String, String> properties = new HashMap<>();

		String databaseHost = settingsHolder.getSettingsRO().databaseHost;

		properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + databaseHost + "/" + dbname);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);

		create(properties);
	}

	@Autowired
	private SettingsHolder<Settings, SettingsRO> settingsHolder;

	private void createDatabase() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String params = "?allowPublicKeyRetrieval=true"//
				+ "&serverTimezone=UTC"//
				+ "&useUnicode=yes"//
				+ "&characterEncoding=UTF8"//
				+ "&userstat=1"//
				+ "&useLegacyDatetimeCode=false"//
				+ "&useSSL=false";

		String databaseHost = settingsHolder.getSettingsRO().databaseHost;

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + databaseHost + params, user, password);
				Statement stmt = conn.createStatement();) {
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbname + " CHARACTER SET utf8 COLLATE utf8_bin");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void incrementTableVersion(EntityManager em, Object o) {
		if (o instanceof Report) {
			Company company = ((Report) o).company;
			_incrementTableVersion(em, Hmc.class, company == null ? null : company.id);
		}
		super.incrementTableVersion(em, o);
	}

}
// CREATE USER 'HMC'@'%' IDENTIFIED BY 'HMC';
// GRANT ALL PRIVILEGES ON HMC . * TO 'HMC'@'%';

// CREATE USER 'HTEST'@'%' IDENTIFIED BY 'HTEST';
// GRANT ALL PRIVILEGES ON HTEST . * TO 'HTEST'@'%';
