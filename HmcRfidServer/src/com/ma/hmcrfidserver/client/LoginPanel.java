package com.ma.hmcrfidserver.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.auth.Constants;
import com.ma.common.gwtapp.client.auth.LoginPageBase;
import com.ma.common.gwtapp.client.ui.FlexTableX;
import com.ma.common.gwtapp.client.ui.dialog.OkCancelDialogBox;

public class LoginPanel extends LoginPageBase {
	private final Constants constants = GWT.create(Constants.class);

	private final Label errorMesasgeLabel = new Label();
	private TextBox loginTextField = new TextBox();
	private PasswordTextBox passwordTextField = new PasswordTextBox();

	private HmcServiceAsync hmcService;

	@Override
	protected void onErr(String text) {
		GWT.log("LOGIN ERROR");
		// err.setText(text);
		loginTextField.addStyleName("myfirst");
		passwordTextField.addStyleName("myfirst");
	}

	public LoginPanel(HmcServiceAsync hmcService) {
		this.hmcService = hmcService;
		HorizontalPanel labelPanel = new HorizontalPanel();
		Label label = new Label("Платформа дезинфекции");
		label.setStyleName("login-label");
		labelPanel.add(label);

		CheckBox saveData = new CheckBox("Запомнить меня");
		// SimpleCheckBox saveData = new SimpleCheckBox();
		// saveData.getElement().setClassName("input");
		// saveData.getElement().getStyle().clearBorderStyle();
		// saveData.setStylePrimaryName("mycheckbox");

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

		Button newUserButton = new Button("Новый пользователь", (ClickHandler) event -> newUser());
		newUserButton.setStyleName("button-google");
		newUserButton.getElement().getStyle().setBackgroundColor("khaki");

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		// buttonsPanel.getElement().getStyle().setMarginTop(50, Unit.PX);
		buttonsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonsPanel.setWidth("100%");
		buttonsPanel.setSpacing(10);
		buttonsPanel.add(loginButton);
		buttonsPanel.add(saveData);
		buttonsPanel.add(newUserButton);

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

		verticalPanel.setStyleName("center");
		verticalPanel.addStyleName("container");
		// verticalPanel.addStyleName("grid");
		verticalPanel.getElement().getStyle().setBackgroundColor("khaki");

		initWidget(verticalPanel);
	}

	private void newUser() {
		TextBox companyName = new TextBox();
		TextBox adminName = new TextBox();
		TextBox userEmail = new TextBox();
		PasswordTextBox adminPassword = new PasswordTextBox();
		PasswordTextBox adminPassword2 = new PasswordTextBox();

		FlexTableX grid = new FlexTableX();
		grid.addRow(new Label("Название компании"), companyName);
		grid.addRow(new Label("Имя администратора"), adminName);
		grid.addRow(new Label("Пароль администратора"), adminPassword);
		grid.addRow(new Label("Пароль администратора"), adminPassword2);
		grid.addRow(new Label("Email"), userEmail);

		OkCancelDialogBox dialogBox = new OkCancelDialogBox("Новый пользователь", grid) {
			@Override
			protected boolean onOK() {
				if (!adminPassword.getText().equals(adminPassword2.getText())) {
					Window.alert("Ошибка при вводе нового пароля");
					return false;
				} else {
					String hashNew = AuthUtils.getPwdHash(adminName.getText(), adminPassword.getText());
					hmcService.addUserCompany(companyName.getText(), adminName.getText(), hashNew, userEmail.getText(),
							new AlertAsyncCallback<>("", (v) -> {
								Window.alert(
										"Новая компания зарегистрирована, пользователь с правами администратора создан.");
							}));
					return true;
				}

			}
		};
		dialogBox.center();
	}

}
