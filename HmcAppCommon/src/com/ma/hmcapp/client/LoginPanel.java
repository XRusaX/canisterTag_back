package com.ma.hmcapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ma.common.gwtapp.client.auth.Constants;
import com.ma.common.gwtapp.client.auth.LoginPageBase;
import com.ma.common.gwtapp.client.ui.FlexTableX;

public class LoginPanel extends LoginPageBase{

	private final Constants constants = GWT.create(Constants.class);

	// private final Label errorMesasgeLabel = new Label();
	private TextBox loginTextField = new TextBox();
	private PasswordTextBox passwordTextField = new PasswordTextBox();

	@Override
	protected void onErr(String text) {
		GWT.log("LOGIN ERROR");
		// err.setText(text);
		loginTextField.addStyleName("myfirst");
		passwordTextField.addStyleName("myfirst");
	}

	public LoginPanel(String appName, Runnable addUser) {
		CSSBundle.css.myCss().ensureInjected();
		HorizontalPanel labelPanel = new HorizontalPanel();
		labelPanel.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		Label label = new Label(appName);
		label.setStyleName("login-label");
		labelPanel.add(label);

		CheckBox saveData = new CheckBox("Запомнить меня");

		if (getCookieUserName() != null && getCookiePassword() != null) {
			loginTextField.setText(getCookieUserName());
			passwordTextField.setText(getCookiePassword());
		}

		FlexTableX inputFieldsGrid = new FlexTableX();
		VerticalPanel inputsContainer = new VerticalPanel();
		HorizontalPanel loginPanel = new HorizontalPanel();
		HorizontalPanel passwordPanel = new HorizontalPanel();
		loginTextField.setStylePrimaryName("form-field");
		passwordTextField.setStylePrimaryName("form-field");
		Label loginLbl = new Label(constants.userName());
		loginLbl.setStyleName("status-label");
		loginLbl.addStyleName("simple");
		loginPanel.add(loginLbl);
		loginPanel.add(loginTextField);
		// inputFieldsGrid.addRow(labelPanel);
		inputFieldsGrid.addRow(loginLbl, loginTextField);
		Label passwordLbl = new Label(constants.password());
		passwordLbl.setStyleName("status-label");
		passwordLbl.addStyleName("simple");
		passwordPanel.add(passwordLbl);
		passwordPanel.add(passwordTextField);
		inputFieldsGrid.addRow(passwordLbl, passwordTextField);
		inputFieldsGrid.setStyleName("grid");

		inputsContainer.add(loginPanel);
		inputsContainer.add(passwordPanel);
		inputsContainer.setSpacing(10);
		Button loginButton = new Button(constants.login(), (ClickHandler) event -> login2(loginTextField.getText(),
				passwordTextField.getText(), saveData.getValue()));
		loginButton.setStyleName("button-green");

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		// buttonsPanel.getElement().getStyle().setMarginTop(50, Unit.PX);
		buttonsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonsPanel.setWidth("100%");
		buttonsPanel.setSpacing(10);
		buttonsPanel.add(loginButton);
		buttonsPanel.add(saveData);
		buttonsPanel.getElement().getStyle().setDisplay(Display.FLEX);

		if (addUser != null) {
			Button newUserButton = new Button("Новый пользователь", (ClickHandler) event -> addUser.run());
			newUserButton.setStyleName("button-google");
			newUserButton.getElement().getStyle().setBackgroundColor("khaki");
			buttonsPanel.add(newUserButton);
		}

		VerticalPanel verticalPanel = new VerticalPanel();
		// verticalPanel.add(errorMesasgeLabel);
		// verticalPanel.add(inputsContainer);
		verticalPanel.add(labelPanel);
		verticalPanel.add(inputFieldsGrid);
		verticalPanel.add(buttonsPanel);

		// Button restorePasswordBtn = new Button(constants.forgotPassword(),
		// (ClickHandler) event -> restorePassword());
		// restorePasswordBtn.getElement().getStyle().setMarginTop(20, Unit.PX);
		// verticalPanel.add(restorePasswordBtn);

		verticalPanel.setStyleName("center-new");
//		verticalPanel.addStyleName("container");
		// verticalPanel.addStyleName("grid");
//		verticalPanel.getElement().getStyle().setBackgroundColor("khaki");

		initWidget(verticalPanel);
	}

}
