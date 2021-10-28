package com.ma.hmcapp;

import org.springframework.stereotype.Component;

import com.ma.appcommon.App;

@Component
public class HmcApp extends App {
	public HmcApp() {
		super("HMC", true);
	}
}
