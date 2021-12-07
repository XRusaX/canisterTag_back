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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ma.appcommon.SettingsHolder;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.Hmc;
import com.ma.hmcdb.entity.rfid.Report;

@Component
public class HmcDatabase extends Database2 {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SettingsHolder<Settings, SettingsRO> settingsHolder;

	@PostConstruct
	private void init() {
//		createDatabase();
	}

//	private void createDatabase() {
//
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		String params = "?allowPublicKeyRetrieval=true"//
//				+ "&serverTimezone=UTC"//
//				+ "&useUnicode=yes"//
//				+ "&characterEncoding=UTF8"//
//				+ "&userstat=1"//
//				+ "&useLegacyDatetimeCode=false"//
//				+ "&useSSL=false";
//
//		SettingsRO settings = settingsHolder.getSettingsRO();
//		String appName = applicationContext.getApplicationName().isEmpty() ? "HMC_dev"
//				: applicationContext.getApplicationName();
//		String dbname = appName;
//		String user = appName;
//		String databaseHost = settings.databaseHost;
//		String password = settings.password;
//
//		try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + databaseHost + params, user, password);
//				Statement stmt = conn.createStatement();) {
//			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbname + " CHARACTER SET utf8 COLLATE utf8_bin");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

}

//CREATE DATABASE IF NOT EXISTS HMC_dev CHARACTER SET utf8 COLLATE utf8_bin;
//CREATE USER 'HMC_dev'@'%' IDENTIFIED BY 'HTEST';
//GRANT ALL PRIVILEGES ON HMC_dev . * TO 'HMC_dev'@'%';
//FLUSH PRIVILEGES;

// CREATE USER 'HMC'@'%' IDENTIFIED BY 'HMC';
// GRANT ALL PRIVILEGES ON HMC . * TO 'HMC'@'%';

// CREATE USER 'HTEST'@'%' IDENTIFIED BY 'HTEST';
// GRANT ALL PRIVILEGES ON HTEST . * TO 'HTEST'@'%';
