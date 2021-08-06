package ru.aoit.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import ru.aoit.appcommon.shared.AppConfig;
import ru.aoit.appcommon.shared.AppState;
import ru.aoit.appcommon.shared.auth.UserData;
import ru.aoit.hmcdb.shared.Agent;
import ru.aoit.hmcdb.shared.Company.CompanyType;
import ru.aoit.hmcdb.shared.Permissions;
import ru.aoit.hmcdb.shared.rfid.Hmc;
import ru.aoit.hmcdb.shared.rfid.Quota;
import ru.aoit.hmcdb.shared.rfid.Report;
import ru.aoit.hmcdb.shared.rfid.RfidLabel;
import ru.aoit.hmcdb.shared.rfid.RfidLabelGroup;
import ru.aoit.hmcdb.shared.test.TestReport;
import ru.nppcrts.common.gwt.client.AppMainPage;
import ru.nppcrts.common.gwt.client.AppService;
import ru.nppcrts.common.gwt.client.AppServiceAsync;
import ru.nppcrts.common.gwt.client.CommonSettingsPanel;
import ru.nppcrts.common.gwt.client.MainToolbar;
import ru.nppcrts.common.gwt.client.SettingsValues;
import ru.nppcrts.common.gwt.client.auth.Login;
import ru.nppcrts.common.gwt.client.auth.UsersPage;
import ru.nppcrts.common.gwt.client.commondata.CommonListPanelX;
import ru.nppcrts.common.gwt.client.commondata.PageEventBus;
import ru.nppcrts.common.gwt.client.connections.ConnectionsPage;
import ru.nppcrts.common.gwt.client.logger.LoggerPanel;
import ru.nppcrts.common.gwt.client.ui.panel.DockLayoutPanelX;
import ru.nppcrts.common.gwt.client.ui.toolbar.StatusBar;

public class MainPage extends AppMainPage {
	// private final CommonDataServiceAsync commonDataService =
	// GWT.create(CommonDataService.class);
	private final AppServiceAsync settingsService = GWT.create(AppService.class);

	private AppState appState;

	public static SettingsValues settingsValues;

	public MainPage(AppConfig appConfig, AppState appState) {
		super(appConfig.loginNeeded, false);
		this.appState = appState;

		settingsService.getSettingsValues(new AsyncCallback<SettingsValues>() {
			@Override
			public void onSuccess(SettingsValues result) {
				settingsValues = result;
				create();
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
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

		CompanyType companyType = null;
		if (!Login.user.hasPermission(UserData.PERMISSIONS_ALL)) {
			if (Login.user.hasPermission(Permissions.PERMISSION_TEST))
				companyType = CompanyType.TEST;
			if (Login.user.hasPermission(Permissions.PERMISSION_WRITE_RFID))
				companyType = CompanyType.CANISTER;
		}

		// CompanyType companyType =
		// Lookup.get(Company.class.getName()).getById(Login.user.company);

		// commonDataService.loadRange(className, filter, range, callback);

		PageEventBus eventBus;

		if (Login.user.company == null) {
			eventBus = new PageEventBus();
			tabPanel.add(new CompaniesPanel(eventBus, null), "Производство");
		}

		eventBus = new PageEventBus();
		tabPanel.add(new CommonListPanelX("МГЦ", Hmc.class, eventBus), "МГЦ");

		if (Login.user.hasPermission(Permissions.PERMISSION_TEST)) {
			eventBus = new PageEventBus();
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
					.addW(new CommonListPanelX("Тесты", TestReport.class, eventBus), 90), "Тесты");
		}

		if (Login.user.hasPermission(Permissions.PERMISSION_WRITE_RFID)) {
			eventBus = new PageEventBus();
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
					.addW(new CommonListPanelX("Группы", RfidLabelGroup.class, eventBus).setEditable(false), 50)//
					.addW(new CommonListPanelX("Метки", RfidLabel.class, eventBus).setEditable(false), 25)//
					.addX(new CommonListPanelX("Отчеты", Report.class, eventBus).setEditable(false))//
					, "Метки");

			eventBus = new PageEventBus();
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
					.addW(new CommonListPanelX("Средства", Agent.class, eventBus), 90), "Средства");

			eventBus = new PageEventBus();
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
					.addW(new CommonListPanelX("Квоты", Quota.class, eventBus), 90), "Квоты");
		}

		if (Login.user.company == null)
			tabPanel.add(new LoggerPanel(true), "Журнал");

		if (Login.user.hasPermission(UserData.PERMISSION_USERS)) {
//			tabPanel.add(new UsersPage(true), "Пользователи");
			eventBus = new PageEventBus();
			tabPanel.add(new CommonListPanelX("Пользователи", UserData.class, eventBus), "Пользователи");
		}

		if (Login.user.company == null)
			tabPanel.add(new ConnectionsPage(), "Подключения");

		if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
		// 10.6.150.102

	}

}
