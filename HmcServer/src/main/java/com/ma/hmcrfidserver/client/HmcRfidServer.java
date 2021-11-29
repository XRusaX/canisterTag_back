package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwtapp.client.AppEntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HmcRfidServer extends AppEntryPoint {
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	@Override
	public void onModuleLoad1() {
		RootLayoutPanel.get().add(new MainPage(appConfig, appState));
	}

	@Override
	protected Widget getLoginPage() {

//		Label label = new Label("МГЦ");
//		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
//		label.getElement().getStyle().setFontSize(4,Unit.EM);
//		
//		
//		Button newUser = new Button("Новый пользователь", (ClickHandler) event -> newUser());
//		
//		VertPanel vertPanel = new VertPanel().setSp(10).add(label, newUser, super.getLoginPage());
//		vertPanel.getElement().getStyle().setMargin(4, Unit.EM);
//
//		FlowPanel fp = new FlowPanel();
//		fp.add(vertPanel);
//		fp.addStyleName("center");
//		fp.getElement().getStyle().setBackgroundColor("khaki");

		return new LoginPanel(hmcService);
	}


}
