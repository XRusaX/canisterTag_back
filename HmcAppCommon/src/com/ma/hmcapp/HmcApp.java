package com.ma.hmcapp;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ma.appcommon.App;

@Component
public class HmcApp extends App {
	public HmcApp() {
		super(true);
	}
	
	public static String getAppName(ApplicationContext context) {
		String applicationName = context.getApplicationName().replaceAll("/", "");
		System.out.println("************************** applicationName '" + applicationName + "'");
		return applicationName.isEmpty() ? "HMC_dev" : applicationName;
	}
}
