package ru.aoit.hmcserver.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.nppcrts.common.app.AppData;
import ru.nppcrts.common.gwt.app.server.App;
import ru.nppcrts.common.gwt.app.server.Database2;
import ru.nppcrts.common.gwt.app.server.SettingsHolder;
import ru.nppcrts.common.gwt.app.server.WorkingDir;
import ru.nppcrts.common.gwt.app.server.logger.MsgLoggerDataProvider;
import ru.nppcrts.common.gwt.app.server.logger.MsgLoggerDataProviderDB;
import ru.nppcrts.common.gwt.auth.server.UserService;
import ru.nppcrts.common.gwt.auth.server.UserServiceFile;

@Configuration
public class ContextConfiguration {
	@Bean
	public SettingsHolder<Settings, SettingsRO> settingsHolder() {
		return new SettingsHolder<Settings, SettingsRO>(Settings.class, SettingsRO.class);
	}

	@Bean
	public App app() {
		return new App("HMC", true);
	}

	@Bean
	public MsgLoggerDataProvider msgLoggerDataProvider() {
		return new MsgLoggerDataProviderDB();
		// return new MsgLoggerDataProviderRAM(5000);
	}

	@Bean
	public UserService userService() {
		return new UserServiceFile(workingDir().workingDir);
	}

	@Bean
	public WorkingDir workingDir() {
		return new WorkingDir(AppData.getWorkingDir("HMC"));
	}

	@Bean
	public Database2 database2() {
		String unit = "HTEST";

		return new Database2(unit) {
			@Override
			protected void createDatabase() {
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
		};

	}

}
