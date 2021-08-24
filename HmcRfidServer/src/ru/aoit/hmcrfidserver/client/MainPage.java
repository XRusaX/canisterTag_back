package ru.aoit.hmcrfidserver.client;

import java.util.Arrays;

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
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.Operator;
import ru.aoit.hmcdb.shared.Permissions;
import ru.aoit.hmcdb.shared.Room;
import ru.aoit.hmcdb.shared.rfid.Quota;
import ru.aoit.hmcdb.shared.rfid.Report;
import ru.aoit.hmcdb.shared.rfid.RfidLabel;
import ru.aoit.hmcdb.shared.test.TestReport;
import ru.nppcrts.common.gwt.client.AppMainPage;
import ru.nppcrts.common.gwt.client.AppService;
import ru.nppcrts.common.gwt.client.AppServiceAsync;
import ru.nppcrts.common.gwt.client.CommonSettingsPanel;
import ru.nppcrts.common.gwt.client.MainToolbar;
import ru.nppcrts.common.gwt.client.SettingsValues;
import ru.nppcrts.common.gwt.client.auth.Login;
import ru.nppcrts.common.gwt.client.auth.UsersPage;
import ru.nppcrts.common.gwt.client.commondata.CommonDataFlowList;
import ru.nppcrts.common.gwt.client.commondata.CommonListPanel;
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

		PageEventBus eventBus;

		TabLayoutPanel tabPanel2 = tabPanel;

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
			panel.addW(new CommonListPanelX(new CommonListPanel("Квоты", 2000), Quota.class, eventBus), 50);
			panel.add(new CommonListPanelX(new CommonListPanel("Метки", 2000).setEditable(false), RfidLabel.class,
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
			panel.add(new CommonListPanelX(new CommonListPanel("Тесты", 2000), TestReport.class, eventBus));
			tabPanel2.add(panel, "Тесты");
		}

		eventBus = new PageEventBus();
		if (Login.user.company == null) {
			tabPanel2 = new TabLayoutPanel(2, Unit.EM);
			tabPanel.add(new DockLayoutPanelX(Unit.PCT)
					.addW(new CompaniesPanel(eventBus, "Клиенты", CompanyType.CUSTOMER, Arrays.asList("name")), 20)
					.addX(tabPanel2), "Клиенты");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_USER)) {
			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);

			panel.addW(new CommonListPanelX(new CommonDataFlowList(), Hmc.class, eventBus), 40);
			panel.add(new CommonListPanelX(new CommonListPanel("Отчеты", 2000), Report.class, eventBus));
			tabPanel2.add(panel, "МГЦ");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelX(new CommonListPanel("Объекты", 2000), Room.class, eventBus), 30);
			tabPanel2.add(panel, "Объекты");

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelX(new CommonListPanel("Операторы", 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Операторы");
		}

		eventBus = new PageEventBus();
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)//
				.addX(new CommonListPanelX(new CommonListPanel("Средства", 2000), Agent.class, eventBus)), "Средства");

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

		if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
		// 10.6.150.102
	}

}
