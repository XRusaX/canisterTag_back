package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.AppEntryPoint;
import com.ma.common.gwtapp.client.ui.FlexTableX;
import com.ma.common.gwtapp.client.ui.dialog.OkCancelDialogBox;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;
import com.ma.common.shared.MD5;

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
