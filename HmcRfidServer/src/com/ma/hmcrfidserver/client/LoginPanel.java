package com.ma.hmcrfidserver.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
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
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.auth.Constants;
import com.ma.common.gwtapp.client.auth.LoginPageBase;
import com.ma.common.gwtapp.client.ui.FlexTableX;
import com.ma.common.gwtapp.client.ui.dialog.OkCancelDialogBox;
import com.ma.common.shared.MD5;

public class LoginPanel extends LoginPageBase {
	private final Constants constants = GWT.create(Constants.class);

	private final Label err = new Label();
	private TextBox user = new TextBox();
	private PasswordTextBox password = new PasswordTextBox();

	private HmcServiceAsync hmcService;

	@Override
	protected void onErr(String text) {
		GWT.log("LOGIN ERROR");
//		err.setText(text);
		user.addStyleName("myfirst");
		password.addStyleName("myfirst");
	}

	public LoginPanel(HmcServiceAsync hmcService) {
		this.hmcService = hmcService;
		HorizontalPanel labelPanel = new HorizontalPanel();
		Label label = new Label("МГЦ");
		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		label.getElement().getStyle().setFontSize(4, Unit.EM);
		label.getElement().getStyle().setMarginBottom(50, Unit.PX);
		labelPanel.add(label);

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
		grid.addRow(labelPanel);
		grid.addRow(userNameLbl, user);
		Label passwordLbl = new Label(constants.password());
		passwordLbl.setStyleName("status-label");
		passwordLbl.addStyleName("simple");
		grid.addRow(passwordLbl, password);
//		grid.setWidth("100%");
		Button loginButton = new Button(constants.login(),
				(ClickHandler) event -> login2(user.getText(), password.getText(), saveData.getValue()));
		loginButton.setStyleName("button-green");
		
		Button newUserButton = new Button("Новый пользователь", (ClickHandler) event -> newUser());
		newUserButton.setStyleName("button-google");
		newUserButton.getElement().getStyle().setBackgroundColor("khaki");
		
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.getElement().getStyle().setMarginTop(50, Unit.PX);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setWidth("100%");
		hp.setSpacing(10);
		hp.add(loginButton);
		hp.add(saveData);
		hp.add(newUserButton);

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
					String hashNew = MD5.calcMD5(adminName.getText() + adminPassword.getText());
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
