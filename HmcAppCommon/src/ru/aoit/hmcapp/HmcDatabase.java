package ru.aoit.hmcapp;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import ru.aoit.appcommon.Database2;

@Component
public class HmcDatabase extends Database2 {
	public HmcDatabase() {
		super("HTEST");
	}


//	private final static String dbName = "HMC";
//	private final static String userName = "HMC";
//	private final static String password = "HMC";

	
	@Override
	protected void createDatabase() {
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
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.56.10" + params, "HTEST",
				"HTEST"); Statement stmt = conn.createStatement();) {
			stmt.executeUpdate(
					"CREATE DATABASE IF NOT EXISTS " + "HTEST" + " CHARACTER SET utf8 COLLATE utf8_bin");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
//	@Autowired
//	private SettingsHolderImpl<Settings, SettingsRO> settingsHolder;
//
//	@PostConstruct
//	private void init() {
//		connectionProvider = new MysqlConnectionProvider(settingsHolder.getSettingsRO().databaseHost, dbName, userName,
//				password);
//		try {
//			Class<?>[] classes = { //
//					Company.class, //
//					Agent.class, //
//					UserData.class, //
//					MsgLoggerItem.class, //
//
//					RfidLabel.class, //
//					RfidLabelGroup.class, //
//					Quota.class, //
//					Report.class, //
//
//					TestReport.class, //
//			};
//
//			SchemaConverter.updateSchema(connectionProvider, classes);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
// CREATE USER 'HMC'@'%' IDENTIFIED BY 'HMC';
// GRANT ALL PRIVILEGES ON HMC . * TO 'HMC'@'%';

//CREATE USER 'HTEST'@'%' IDENTIFIED BY 'HTEST';
//GRANT ALL PRIVILEGES ON HTEST . * TO 'HTEST'@'%';
