package com.ma.hmcserver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.ma.appcommon.SettingsHolder;
import com.ma.hmcapp.Settings;
import com.ma.hmcapp.SettingsRO;

@Configuration
public class Config {

	@Autowired
	private SettingsHolder<Settings, SettingsRO> settingsHolder;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		SettingsRO settings = settingsHolder.getSettingsRO();
		String dbname = settings.dbname;
		String user = settings.user;
		String databaseHost = settings.databaseHost;
		String password = settings.password;

		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + databaseHost + ":3306/" + dbname);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}

	
}
