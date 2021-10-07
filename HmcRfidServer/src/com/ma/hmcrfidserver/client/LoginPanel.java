package com.ma.hmcrfidserver.client;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ma.common.gwtapp.client.auth.Constants;
import com.ma.common.gwtapp.client.auth.LoginPageBase;
import com.ma.common.gwtapp.client.ui.FlexTableX;

public class LoginPanel extends LoginPageBase {
	private final Constants constants = GWT.create(Constants.class);

	private final Label err = new Label();
	private TextBox user = new TextBox();
	private PasswordTextBox password = new PasswordTextBox();

	@Override
	protected void onErr(String text) {
		GWT.log("LOGIN ERROR");
//		err.setText(text);
		user.addStyleName("myfirst");
		password.addStyleName("myfirst");
	}

	public LoginPanel() {

		CheckBox saveData = new CheckBox(constants.savePassword());
		saveData.getElement().setClassName("input");
		saveData.getElement().getStyle().clearBorderStyle();
//		saveData.setStylePrimaryName("custom-checkbox");

		if (getCookieUserName() != null && getCookiePassword() != null) {
			user.setText(getCookieUserName());
			password.setText(getCookiePassword());
		}

		FlexTableX grid = new FlexTableX();
		user.setStylePrimaryName("form-field");
		password.setStylePrimaryName("form-field");
		Label userNameLbl = new Label(constants.userName());
		userNameLbl.setStyleName("status-label");
		userNameLbl.addStyleName("simple");
		grid.addRow(userNameLbl, user);
		Label passwordLbl = new Label(constants.password());
		passwordLbl.setStyleName("status-label");
		passwordLbl.addStyleName("simple");
		grid.addRow(passwordLbl, password);

		Button loginButton = new Button(constants.login(),
				(ClickHandler) event -> login2(user.getText(), password.getText(), saveData.getValue()));

		HorizontalPanel hp = new HorizontalPanel();
		loginButton.setStyleName("button-green");
		hp.getElement().getStyle().setMarginTop(50, Unit.PX);
		hp.add(loginButton);
		hp.add(saveData);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(err);
		verticalPanel.add(grid);
		verticalPanel.add(hp);
		
//		Button restorePasswordBtn = new Button(constants.forgotPassword(), (ClickHandler) event -> restorePassword());
//		restorePasswordBtn.getElement().getStyle().setMarginTop(20, Unit.PX);
//		verticalPanel.add(restorePasswordBtn);

		verticalPanel.addStyleName("center");
		verticalPanel.getElement().getStyle().setBackgroundColor("khaki");

		initWidget(verticalPanel);
	}
}
