package com.ma.canisterrfid.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwtapp.client.AppEntryPoint;
import com.ma.hmcapp.client.LoginPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CanisterRfidEntryPoint extends AppEntryPoint {

	@Override
	public void onModuleLoad1() {
		RootLayoutPanel.get().add(new MainPage(appConfig, appState));
	}

	@Override
	protected Widget getLoginPage() {
		return new LoginPanel("Модуль записи меток", null);
	}

}
