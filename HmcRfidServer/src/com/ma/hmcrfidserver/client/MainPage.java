package com.ma.hmcrfidserver.client;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.ma.appcommon.shared.AppConfig;
import com.ma.appcommon.shared.AppState;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.gwt.client.AppMainPage;
import com.ma.common.gwt.client.AppService;
import com.ma.common.gwt.client.AppServiceAsync;
import com.ma.common.gwt.client.CommonSettingsPanel;
import com.ma.common.gwt.client.MainToolbar;
import com.ma.common.gwt.client.SettingsValues;
import com.ma.common.gwt.client.auth.Login;
import com.ma.common.gwt.client.auth.UsersPage;
import com.ma.common.gwt.client.commondata.CommonDataFlowList;
import com.ma.common.gwt.client.commondata.CommonListPanel;
import com.ma.common.gwt.client.commondata.CommonListPanelWrapper;
import com.ma.common.gwt.client.commondata.PageEventBus;
import com.ma.common.gwt.client.connections.ConnectionsPage;
import com.ma.common.gwt.client.logger.LoggerPanel;
import com.ma.common.gwt.client.ui.panel.DockLayoutPanelX;
import com.ma.common.gwt.client.ui.toolbar.StatusBar;
import com.ma.hmcdb.shared.Agent;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Permissions;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.Company.CompanyType;
import com.ma.hmcdb.shared.rfid.Quota;
import com.ma.hmcdb.shared.rfid.Report;
import com.ma.hmcdb.shared.rfid.RfidLabel;
import com.ma.hmcdb.shared.test.TestReport;

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

		PageEventBus eventBus;

		TabLayoutPanel tabPanel2 = tabPanel;

		eventBus = new PageEventBus();
		if (Login.user.company == null) {
			tabPanel2 = new TabLayoutPanel(2, Unit.EM);
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)
					.addW(new CompaniesPanel(eventBus, "Клиенты", CompanyType.CUSTOMER, Arrays.asList("name")), 20)
					.addX(tabPanel2), "Клиенты");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_USER)) {
			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);

			panel.addW(new CommonListPanelWrapper(new CommonDataFlowList(), Hmc.class, eventBus), 40);
			panel.add(new CommonListPanelWrapper(new CommonListPanel("Отчеты", 2000).setEditable(Login.user.company == null),
					Report.class, eventBus));
			tabPanel2.add(panel, "МГЦ");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Room.class, eventBus), 30);
			panel.add(new MapPanel(eventBus));
			tabPanel2.add(panel, "Объекты");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Операторы");
		}

		eventBus = new PageEventBus();
		if (Login.user.company == null) {
			tabPanel2 = new TabLayoutPanel(2, Unit.EM);
			tabPanel.add(
					new DockLayoutPanelX(Unit.PCT).addW(new CompaniesPanel(eventBus, "Предприятия",
							CompanyType.CANISTER, Arrays.asList("name", "rfidBlockSize")), 20).addX(tabPanel2),
					"Производство канистр");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_WRITE_RFID)) {
			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel("Квоты", 2000).setEditable(Login.user.company == null),
					Quota.class, eventBus), 50);
			panel.add(new CommonListPanelWrapper(new CommonListPanel("Метки", 2000).setEditable(false), RfidLabel.class,
					eventBus));
			tabPanel2.add(panel, "Метки");
		}

		eventBus = new PageEventBus();
		if (Login.user.company == null) {
			tabPanel2 = new TabLayoutPanel(2, Unit.EM);
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)
					.addW(new CompaniesPanel(eventBus, "Предприятия", CompanyType.TEST, Arrays.asList("name")), 20)
					.addX(tabPanel2), "Тестирование плат");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_TEST)) {
			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);
			panel.add(new CommonListPanelWrapper(new CommonListPanel(null, 2000).setEditable(Login.user.company == null),
					TestReport.class, eventBus));
			tabPanel2.add(panel, "Тесты");
		}

		eventBus = new PageEventBus();
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
				.addX(new CommonListPanelWrapper(new CommonListPanel(null, 2000).setEditable(Login.user.company == null),
						Agent.class, eventBus)),
				"Средства");

		if (Login.user.company == null)
			tabPanel.add(new LoggerPanel(true), "Журнал");

		if (Login.user.hasPermission(UserData.PERMISSION_USERS)) {
			tabPanel.add(new UsersPage(true), "Пользователи");
			// eventBus = new PageEventBus();
			// tabPanel.add(new CommonListPanelX("Пользователи", UserData.class,
			// eventBus), "Пользователи");
		}

		if (Login.user.company == null)
			tabPanel.add(new ConnectionsPage(), "Подключения");

		if (Login.user.company == null && Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new FirmwarePage(), "Прошивки");

		if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
		// 10.6.150.102
	}

}
