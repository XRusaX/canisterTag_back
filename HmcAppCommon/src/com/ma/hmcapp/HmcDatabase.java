package com.ma.hmcapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.Database2;
import com.ma.appcommon.SettingsHolder;

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
//		databaseHost = "192.168.56.10";

		properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + databaseHost + "/" + dbname);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);

		create(properties);
	}

	// private final static String dbName = "HMC";
	// private final static String userName = "HMC";
	// private final static String password = "HMC";

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
		//databaseHost = "192.168.56.10";


		try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + databaseHost + params, user, password);
				Statement stmt = conn.createStatement();) {
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbname + " CHARACTER SET utf8 COLLATE utf8_bin");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// @Autowired
	// private SettingsHolderImpl<Settings, SettingsRO> settingsHolder;
	//
	// @PostConstruct
	// private void init() {
	// connectionProvider = new
	// MysqlConnectionProvider(settingsHolder.getSettingsRO().databaseHost,
	// dbName, userName,
	// password);
	// try {
	// Class<?>[] classes = { //
	// Company.class, //
	// Agent.class, //
	// UserData.class, //
	// MsgLoggerItem.class, //
	//
	// RfidLabel.class, //
	// RfidLabelGroup.class, //
	// Quota.class, //
	// Report.class, //
	//
	// TestReport.class, //
	// };
	//
	// SchemaConverter.updateSchema(connectionProvider, classes);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

}
// CREATE USER 'HMC'@'%' IDENTIFIED BY 'HMC';
// GRANT ALL PRIVILEGES ON HMC . * TO 'HMC'@'%';

// CREATE USER 'HTEST'@'%' IDENTIFIED BY 'HTEST';
// GRANT ALL PRIVILEGES ON HTEST . * TO 'HTEST'@'%';
