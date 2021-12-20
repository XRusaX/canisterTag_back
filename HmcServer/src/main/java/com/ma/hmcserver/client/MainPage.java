package com.ma.hmcserver.client;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.ma.common.gwtapp.client.ui.panel.HorPanel;
import com.ma.common.gwtapp.client.ui.toolbar.StatusBar;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcapp.client.MaterialUIList;
import com.ma.hmcapp.client.MyDataGridResources;
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.Hmc;
import com.ma.hmcapp.entity.Operator;
import com.ma.hmcapp.entity.rfid.Quota;
import com.ma.hmcapp.entity.rfid.Report;
import com.ma.hmcapp.entity.rfid.RfidLabel;
import com.ma.hmcapp.entity.test.TestReport;
import com.ma.hmcapp.shared.CompanyType;
import com.ma.hmcapp.shared.Permissions;
import com.ma.hmcserver.client.customwidgets.PlacementPanel;

public class MainPage extends AppMainPage {
	// private final CommonDataServiceAsync commonDataService =
	// GWT.create(CommonDataService.class);
	private final AppServiceAsync settingsService = GWT.create(AppService.class);
	static MyDataGridResources resource = GWT.create(MyDataGridResources.class);

	private AppState appState;

	public static SettingsValues settingsValues;

	public MainPage(AppConfig appConfig, AppState appState) {
		super(appConfig.loginNeeded, false);
		this.appState = appState;
//		resource.verticalTabPanelStyles().ensureInjected();
		resource.horizontalTabPanelStyles().ensureInjected();
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
			tabPanel.add(new Label(appState.error), "Ошибка!!!");
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
					.addW(new CompaniesPanel(eventBus, "Клиенты", CompanyType.CUSTOMER, Arrays.asList("name")), 15)
					.addX(tabPanel2), "Клиенты");
		}
		if (Login.user.hasPermission(Permissions.PERMISSION_CUSTOMER)) {

			MaterialUIList reportsList = new MaterialUIList("Отчеты");
			reportsList.setEditable(Login.user.company == null);

			CommonListPanelWrapper reportsTab = new CommonListPanelWrapper(reportsList, Report.class, eventBus);

//			CommonListPanelWrapper reportsTab = new CommonListPanelWrapper(
//					new CommonListPanel("Отчеты").setEditable(Login.user.company == null), Report.class,
//					eventBus);

//			CommonListPanelWrapper reportsTab = new CommonListPanelWrapper(
//					new OperatorList(null, 2000).setEditable(Login.user.company == null), Report.class,
//					eventBus);

			DockLayoutPanelX panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new Label(), 5);

			PropertiesPanel hmcPropertiesPanel = new PropertiesPanel(eventBus, Hmc.class.getName());

			panel.addW(new CommonListPanelWrapper(new HmcFlowList() {
				@Override
				void showReports() {
					if (Login.user.company != null) {
						tabPanel.selectTab(reportsTab.getParent());
					}
				}
			}, Hmc.class, eventBus), 70);
			panel.addW(hmcPropertiesPanel, 30);

			tabPanel2.add(panel, "Оборудование");

//			panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null), RoomLayer.class, eventBus), 30);

			panel = new DockLayoutPanelX(Unit.PCT);
			panel.addW(new PlacementPanel(eventBus), 100);

			tabPanel2.add(panel, "Помещения");

			panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			MaterialUIList operatorList = new MaterialUIList(null);
			PropertiesPanel operatorPropPanel = new PropertiesPanel(eventBus, Operator.class.getName()) {
				private PushButton uploadImageButton;

				@Override
				protected Panel getAdditionals(CDObject operator) {

					Image img = new Image(operator.get("avatar") == null
							? new Image(ImageResources.IMAGE_RESOURCES.notFound()).getUrl()
							: operator.get("avatar"));

					uploadImageButton = new PushButton(img, (ClickHandler) event -> {
						new ImportDialog("Выберите файл", "api/images") {
							@Override
							protected void onComplete(SubmitCompleteEvent event) {
								String results = event.getResults();
								if (!results.isEmpty()) {
									operator.set("avatar", results);
									img.setUrl(operator.get("avatar"));
								}
							}
						}.center();
					});
					uploadImageButton.setStyleName("uploadTestBtn");
					uploadImageButton.setTitle("Загрузить изображение");
					HorPanel horPanel = new HorPanel(uploadImageButton);
					horPanel.setWidth("100%");
					horPanel.setCellHorizontalAlignment(uploadImageButton, HasHorizontalAlignment.ALIGN_CENTER);
					updateReadOnlyMode(true);
					return horPanel;
				}

				@Override
				protected void updateReadOnlyMode(boolean roMode) {
					super.updateReadOnlyMode(roMode);
					uploadImageButton.getElement().getStyle().setOpacity(roMode ? 0.2 : 1);
					uploadImageButton.setEnabled(!roMode);
				}
			};
			panel.addW(new Label(), 30);
			panel.addE(operatorPropPanel, 30);
			panel.addN(new Label(), 10);
			panel.addS(new Label(), 10);
			panel.addW(new CommonListPanelWrapper(operatorList, Operator.class, eventBus) {

				@Override
				protected String applyObjectEditorDialogStyle() {
					return "uploadDialog";
				}

				@Override
				protected String setStyleToEditorOkButton() {
					return "button-ok";
				}

				@Override
				protected String setStyleToEditorCancelButton() {
					return "button-cancel";
				}

				@Override
				protected Widget getAdditionals(CDObject operator) {
					Image img = new Image(operator.get("avatar") == null
							? new Image(ImageResources.IMAGE_RESOURCES.notFound()).getUrl()
							: operator.get("avatar"));

					PushButton uploadImageButton = new PushButton(img, (ClickHandler) event -> {
						new ImportDialog("Выберите файл", "api/images") {
							@Override
							protected void onComplete(SubmitCompleteEvent event) {
								String results = event.getResults();
								if (!results.isEmpty()) {
									operator.set("avatar", results);
									img.setUrl(operator.get("avatar"));
								}
							}
						}.center();
					});
					uploadImageButton.setStyleName("uploadTestBtn");
					uploadImageButton.setTitle("Загрузить изображение");
					HorPanel horPanel = new HorPanel(uploadImageButton);
					horPanel.setWidth("100%");
					horPanel.setCellHorizontalAlignment(uploadImageButton, HasHorizontalAlignment.ALIGN_CENTER);
					return horPanel;
				}

				@Override
				public void edit(CDObject item) {
					return;
				}
			}, 30);

			tabPanel2.add(panel, "Операторы");

			panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Контроль дезинфекции");

			panel = new DockLayoutPanelX(Unit.PCT);

			panel.add(reportsTab);
			tabPanel2.add(panel, "Отчёты об обработке");

			panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
			tabPanel2.add(panel, "Журнал операций");

			panel = new DockLayoutPanelX(Unit.PCT);
//			panel.addW(new CommonListPanelWrapper(new CommonListPanel(null, 2000), Operator.class, eventBus), 30);
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
			panel.addW(new CommonListPanelWrapper(new CommonListPanel("Квоты").setEditable(Login.user.company == null),
					Quota.class, eventBus), 50);

			panel.add(new CommonListPanelWrapper(new CommonListPanel("Метки").setEditable(false), RfidLabel.class,
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
			MaterialUIList testList = new MaterialUIList("");
			testList.setEditable(false);
			panel.add(new CommonListPanelWrapper(testList, TestReport.class, eventBus));
//			panel.add(new CommonListPanelWrapper(new CommonListPanel(null).setEditable(Login.user.company == null),
//					TestReport.class, eventBus));
			tabPanel2.add(panel, "Тесты");
		}

		eventBus = new PageEventBus();
		MaterialUIList disinfectantList = new MaterialUIList("");
		disinfectantList.setEditable(Login.user.company == null);
		tabPanel.add(new DockLayoutPanelX(Unit.PCT)
				.addX(new CommonListPanelWrapper(disinfectantList, Agent.class, eventBus)), "Средства");

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