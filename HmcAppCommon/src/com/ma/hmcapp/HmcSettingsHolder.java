package com.ma.hmcapp;

import org.springframework.stereotype.Component;

import com.ma.appcommon.SettingsHolder;

@Component
public class HmcSettingsHolder extends SettingsHolder<Settings, SettingsRO> {

	public HmcSettingsHolder() {
		super(Settings.class, SettingsRO.class);
	}

}
