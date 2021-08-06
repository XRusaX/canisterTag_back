package ru.aoit.hmcserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import ru.aoit.hmcserver.shared.Company;
import ru.aoit.hmcserver.shared.User;
import ru.nppcrts.common.gwt.app.client.AppMainPage;
import ru.nppcrts.common.gwt.app.client.CommonSettingsPanel;
import ru.nppcrts.common.gwt.app.client.MainToolbar;
import ru.nppcrts.common.gwt.app.client.logger.LoggerPanel;
import ru.nppcrts.common.gwt.client.AlertAsyncCallback;
import ru.nppcrts.common.gwt.client.AppService;
import ru.nppcrts.common.gwt.client.AppServiceAsync;
import ru.nppcrts.common.gwt.client.SettingsValues;
import ru.nppcrts.common.gwt.client.auth.Login;
import ru.nppcrts.common.gwt.client.auth.UsersPage;
import ru.nppcrts.common.gwt.client.commondata.CommonListPanelX;
import ru.nppcrts.common.gwt.client.commondata.PageEventBus;
import ru.nppcrts.common.gwt.client.ui.panel.DockLayoutPanelX;
import ru.nppcrts.common.gwt.client.ui.toolbar.StatusBar;
import ru.nppcrts.common.gwt.shared.AppConfig;
import ru.nppcrts.common.gwt.shared.AppState;
import ru.nppcrts.common.gwt.shared.auth.UserData;

public class MainPage extends AppMainPage {
	// private final CommonDataServiceAsync commonDataService =
	// GWT.create(CommonDataService.class);
	private final AppServiceAsync settingsService = GWT.create(AppService.class);

	private AppState appState;

	public SettingsValues settingsValues;

	public MainPage(AppConfig appConfig, AppState appState) {
		super(appConfig.loginNeeded, false);
		this.appState = appState;

		settingsService.getSettingsValues(new AlertAsyncCallback<SettingsValues>(settingsValues -> {
			this.settingsValues = settingsValues;
			create();
		}));
	}

	@Override
	protected void createStatusBar(StatusBar statusBar) {
	}

	@Override
	protected void createMainToolBar(MainToolbar mainToolbar) {
	}

	@Override
	protected void createTabs(TabLayoutPanel tabPanel) {
		if (appState.error != null) {
			tabPanel.add(new Label(appState.error), "Ошибка!");
			if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
				tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
			return;
		}

		PageEventBus eventBus;
		eventBus = new PageEventBus();
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
				.addW(new CommonListPanelX("User", User.class, eventBus), 50)
				.addX(new CommonListPanelX("Company", Company.class, eventBus)), "TEST");

		if (Login.user.company == null)
			tabPanel.add(new LoggerPanel(true), "Журнал");

		if (Login.user.hasPermission(UserData.PERMISSION_USERS))
			tabPanel.add(new UsersPage(true), "Пользователи");

		if (Login.user.company == null)
			tabPanel.add(new ConnectionsPage(), "Подключения");

		if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
		// 10.6.150.102

	}

}
