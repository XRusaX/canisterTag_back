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
import com.ma.common.gwtapp.client.AppMainPage;
import com.ma.common.gwtapp.client.AppService;
import com.ma.common.gwtapp.client.AppServiceAsync;
import com.ma.common.gwtapp.client.CommonSettingsPanel;
import com.ma.common.gwtapp.client.MainToolbar;
import com.ma.common.gwtapp.client.SettingsValues;
import com.ma.common.gwtapp.client.auth.Login;
import com.ma.common.gwtapp.client.auth.UsersPage;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;
import com.ma.common.gwtapp.client.commondata.CommonListPanelWrapper;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.connections.ConnectionsPage;
import com.ma.common.gwtapp.client.logger.LoggerPanel;
import com.ma.common.gwtapp.client.ui.panel.DockLayoutPanelX;
import com.ma.common.gwtapp.client.ui.toolbar.StatusBar;
import com.ma.hmcdb.shared.Agent;
import com.ma.hmcdb.shared.Company.CompanyType;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Permissions;
import com.ma.hmcdb.shared.Room;
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
//				create();
				create("flat-button");
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
		if (Login.user.hasPermission(Permissions.PERMISSION_CUSTOMER)) {

			CommonListPanelWrapper reportsTab = new CommonListPanelWrapper(
					new CommonListPanel("Отчеты", 2000).setEditable(Login.user.company == null), Report.class,
					eventBus);

			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new Label(), 5);
			
//			HmcFlowList testFlowList = new HmcFlowList() {
//				@Override
//				void showReports() {
//					if (Login.user.company != null)
//						tabPanel.selectTab(reportsTab.getParent());
//				}
//			};
			
			panel.addW(new CommonListPanelWrapper(new HmcFlowList() {
				@Override
				void showReports() {
					if (Login.user.company != null) {
						tabPanel.selectTab(reportsTab.getParent());
//						testFlowList.setVisible(!testFlowList.isVisible());
					}
				}
			}, Hmc.class, eventBus), 70);
			
//			panel.addW(new CommonListPanelWrapper(testFlowList, Hmc.class, eventBus), 25);

			tabPanel2.add(panel, "Оборудование");

			panel = new DockLayoutPanelX(Unit.PCT);
			CommonListPanel roomList = new CommonListPanel(null, 2000);
			panel.addW(new CommonListPanelWrapper(roomList, Room.class, eventBus), 30);
			panel.add(new MapPanel(eventBus));
			tabPanel2.add(panel, "Помещения");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Операторы");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Контроль дезинфекции");

			panel = new DockLayoutPanelX(Unit.PCT);

			panel.add(reportsTab);
			tabPanel2.add(panel, "Отчёты об обработке");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Журнал операций");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Статистика");

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
			panel.addW(new CommonListPanelWrapper(
					new CommonListPanel("Квоты", 2000).setEditable(Login.user.company == null), Quota.class, eventBus),
					50);
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
			panel.add(
					new CommonListPanelWrapper(new CommonListPanel(null, 2000).setEditable(Login.user.company == null),
							TestReport.class, eventBus));
			tabPanel2.add(panel, "Тесты");
		}

		eventBus = new PageEventBus();
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
				.addX(new CommonListPanelWrapper(
						new CommonListPanel(null, 2000).setEditable(Login.user.company == null), Agent.class,
						eventBus)),
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
