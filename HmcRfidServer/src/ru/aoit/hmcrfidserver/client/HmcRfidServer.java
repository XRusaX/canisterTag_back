package ru.aoit.hmcrfidserver.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;

import ru.nppcrts.common.gwt.client.AppEntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HmcRfidServer extends AppEntryPoint {

	@Override
	public void onModuleLoad1() {
		RootLayoutPanel.get().add(new MainPage(appConfig, appState));
	}

}
