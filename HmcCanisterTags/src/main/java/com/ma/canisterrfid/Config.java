package com.ma.canisterrfid;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.ma.appcommon.SettingsHolder;
import com.ma.hmcapp.HmcApp;
import com.ma.hmcapp.Settings;
import com.ma.hmcapp.SettingsRO;

@Configuration
public class Config {

	@Autowired
	private SettingsHolder<Settings, SettingsRO> settingsHolder;
	
//	@Autowired
//	private ApplicationContext applicationContext;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		SettingsRO settings = settingsHolder.getSettingsRO();
		
//		String appName = HmcApp.getAppName(applicationContext);
//		String dbname = appName;
//		String user = appName;
		String databaseHost = settings.databaseHost;
		String password = settings.password;
		String dbname = settings.dbname;
		String user = settings.user;

		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + databaseHost + ":3306/" + dbname);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}

	
}
