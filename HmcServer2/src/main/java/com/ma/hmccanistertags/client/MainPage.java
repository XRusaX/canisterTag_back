package com.ma.hmccanistertags.client;


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
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.rfid.Quota;
import com.ma.hmcapp.entity.rfid.RfidLabel;
import com.ma.hmcapp.shared.CompanyType;
import com.ma.hmcapp.shared.Permissions;

public class MainPage extends AppMainPage {
	private final AppServiceAsync settingsService = GWT.create(AppService.class);
	private static MyDataGridResources resource = GWT.create(MyDataGridResources.class);

	private AppState appState;

	public static SettingsValues settingsValues;

	public MainPage(AppConfig appConfig, AppState appState) {
		super(appConfig.loginNeeded, false);
		this.appState = appState;
		FontAwesomeBundle.INSTANCE.fontAwesome().ensureInjected();
		resource.horizontalTabPanelStyles().ensureInjected();
		
		settingsService.getSettingsValues(new AsyncCallback<SettingsValues>() {
			@Override
			public void onSuccess(SettingsValues result) {
				settingsValues = result;
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
			tabPanel.add(new Label(appState.error), "Ошибка!!!");
			if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
				tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
			return;
		}

		PageEventBus eventBus = new PageEventBus();

		TabLayoutPanel tabPanel2 = tabPanel;
		if (Login.user.company == null) {
			tabPanel2 = new TabLayoutPanel(2, Unit.EM);
			tabPanel.add(
					new DockLayoutPanelX(Unit.PCT).addW(new CompaniesPanel(eventBus, "Предприятия",
							CompanyType.CANISTER, Arrays.asList("name", "rfidBlockSize")), 20).addX(tabPanel2),
					"Производство канистр");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_WRITE_RFID)) {
			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new CommonListPanelWrapper(new CommonListPanel("Квоты").setEditable(Login.user.company == null),
					Quota.class, eventBus), 50);

			panel.add(new CommonListPanelWrapper(new CommonListPanel("Метки").setEditable(false), RfidLabel.class,
					eventBus));
			tabPanel2.add(panel, "Метки");
		}

		eventBus = new PageEventBus();
		eventBus = new PageEventBus();
		
		MaterialUIList disinfectantList = new MaterialUIList("");
		disinfectantList.setEditable(Login.user.company == null);
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)
				.addX(new CommonListPanelWrapper(disinfectantList, Agent.class, eventBus)), "Средства");

		if (Login.user.company == null)
			tabPanel.add(new LoggerPanel(true), "Журнал");

		if (Login.user.hasPermission(UserData.PERMISSION_USERS)) {
			tabPanel.add(new UsersPage(true), "Пользователи");
		}

		if (Login.user.company == null)
			tabPanel.add(new ConnectionsPage(), "Подключения");

		if (Login.user.hasPermission(UserData.PERMISSION_SETTINGS))
			tabPanel.add(new CommonSettingsPanel(null, null), "Настройки");
		// 10.6.150.102
	}

}