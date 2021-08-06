package ru.aoit.hmcapp;

import javax.servlet.annotation.WebServlet;

import org.springframework.stereotype.Component;

import ru.aoit.appcommon.SettingsHolderImpl;

@SuppressWarnings("serial")
@Component
@WebServlet("/settingsholder")
public class HmcSettingsHolder extends SettingsHolderImpl<Settings, SettingsRO> {

	public HmcSettingsHolder() {
		super(Settings.class, SettingsRO.class);
	}

}
