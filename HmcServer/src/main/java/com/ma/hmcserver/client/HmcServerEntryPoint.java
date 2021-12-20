package com.ma.hmcserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.AppEntryPoint;
import com.ma.common.gwtapp.client.ui.FlexTableX;
import com.ma.common.gwtapp.client.ui.dialog.OkCancelDialogBox;
import com.ma.hmcapp.client.LoginPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HmcServerEntryPoint extends AppEntryPoint {
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	@Override
	public void onModuleLoad1() {
		RootLayoutPanel.get().add(new MainPage(appConfig, appState));
	}

	@Override
	protected Widget getLoginPage() {

		return new LoginPanel("Платформа дезинфекции", () -> {
			registerNewUser();
		});
	}

	private void registerNewUser() {
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
