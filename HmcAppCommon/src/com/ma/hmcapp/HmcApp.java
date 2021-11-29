package com.ma.hmcapp;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ma.appcommon.App;

@Component
public class HmcApp extends App {
	@SuppressWarnings("unused")
	public HmcApp(ApplicationContext context) {
		super(context.getApplicationName().isEmpty() ? "dev" : context.getApplicationName(), true);
	}
}
