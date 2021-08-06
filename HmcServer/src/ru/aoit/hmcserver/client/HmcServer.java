package ru.aoit.hmcserver.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;

import ru.nppcrts.common.gwt.app.client.AppEntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HmcServer extends AppEntryPoint {

	@Override
	public void onModuleLoad1() {
		RootLayoutPanel.get().add(new MainPage(appConfig, appState));
	}

}
