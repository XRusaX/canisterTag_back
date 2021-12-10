package com.ma.hmcserver.client.customwidgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.DataRange;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.GwtUtils;
import com.ma.common.gwtapp.client.GwtUtils.ContextMenuPreparer;
import com.ma.common.gwtapp.client.auth.Login;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.ObjectEditorDialog;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.ui.ContextMenu;
import com.ma.commonui.shared.cd.CDClass;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcapp.entity.Room;
import com.ma.hmcapp.entity.RoomLayer;

public class LayersPanel extends Composite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);
	private PageEventBus eventBus;
	private Map<String, CDObject> layersList = new HashMap<>();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private VerticalPanel layersPanel = new VerticalPanel();
	private VerticalPanel buttonPanel = new VerticalPanel();
	private Long company = Login.user.company;
	private Filter companyFilter = new Filter();
	private ScrollPanel parenPanel;

	public LayersPanel(PageEventBus eventBus, ScrollPanel parenPanel) {
		this.eventBus = eventBus;
		this.parenPanel = parenPanel;

		Label label = new Label("Этажи компании");
		label.setSize(125 + "px", 35 + "px");
//		label.setSize(90 + "%",90 + "%");
		label.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
		headerPanel.add(label);
//		headerPanel.add(createAddButton());
		buttonPanel.setWidth(100 + "%");

//		buttonPanel.getElement().getStyle().setBorderWidth(5, Unit.PX);
//		buttonPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
//		buttonPanel.getElement().getStyle().setBorderColor("red");

		layersPanel.add(headerPanel);
		layersPanel.add(buttonPanel);

		if (company != null) {
			companyFilter.addEQ("company", company + "");
		}
		createLayersButtonsPanel(companyFilter, buttonPanel);

		service.loadRange(Room.class.getName(), companyFilter, null, new AlertAsyncCallback<>(list -> {
			GWT.log(list.range.toString());
		}));
		this.initWidget(layersPanel);
	}

	private void createLayersButtonsPanel(Filter layerFilter, VerticalPanel layersPanel) {
		buttonPanel.clear();
		service.loadRange(RoomLayer.class.getName(), layerFilter, null, new AlertAsyncCallback<>(list -> {
			list.range.forEach(roomLayer -> {
				layersList.put(roomLayer.get("name"), roomLayer);
				ToggleButton button = new ToggleButton(roomLayer.get("name"));
				button.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						layersPanel.forEach(button -> ((ToggleButton) button).setDown(false));

						eventBus._post(new SelChangeEvent(RoomLayer.class, null, Arrays.asList(roomLayer)));
						button.setDown(true);
					}
				});

				layersPanel.add(button);
				createContextMenu(button);
			});
		}));

	}

	private Button createAddButton() {
		Button addButton = new Button("Добавить");
		addButton.addClickHandler(new ClickHandler() {
			CDClass roomClass;

			@Override
			public void onClick(ClickEvent event) {
				service.getCDClass(RoomLayer.class.getName(), new AlertAsyncCallback<>(a -> {
					roomClass = a;

					service.newCDObject(RoomLayer.class.getName(), new AlertAsyncCallback<>(cdObject -> {
						new ObjectEditorDialog("Создать", roomClass, cdObject) {

							@Override
							protected void onOK(CDObject values) {
								service.store(RoomLayer.class.getName(), values, new AlertAsyncCallback<Long>(id -> {
									values.set("id", id);
									createLayersButtonsPanel(companyFilter, buttonPanel);
								}));
							}

							@Override
							protected Widget getAdditionals() {
								return null;
							}

							@Override
							protected void getLookup(String className, AsyncCallback<DataRange<CDObject>> callback) {
//								Filter layerFilter = new Filter();
//								Long company = Login.user.company;
//								layerFilter.addEQ("company", company + "");
//								createLayersButtonsPanel(layerFilter, layersPanel);
							}
						}.center();
					}));
				}));
			}
		});
		return addButton;
	}

	private void createContextMenu(ToggleButton button) {
		GwtUtils.addContextMenu(button, new ContextMenuPreparer() {
			@Override
			public void prepareContextMenu(ContextMenu menu, int x, int y, Runnable onPrepared) {
				menu.addItem("Удалить", new ScheduledCommand() {
					@Override
					public void execute() {
						service.delete(RoomLayer.class.getName(),
								Arrays.asList(Long.valueOf(layersList.get(button.getText()).get("id"))),
								new AlertAsyncCallback<>(null));
					}
				});
				menu.addSeparator();
				
				menu.addItem("Добавить новый этаж..", new ScheduledCommand() {
					CDClass roomClass;
					@Override
					public void execute() {
						service.getCDClass(RoomLayer.class.getName(), new AlertAsyncCallback<>(a -> {
							roomClass = a;
							
						service.newCDObject(RoomLayer.class.getName(), new AlertAsyncCallback<>(cdObject -> {
							new ObjectEditorDialog("Создать", roomClass, cdObject) {

								@Override
								protected void onOK(CDObject values) {
									service.store(RoomLayer.class.getName(), values, new AlertAsyncCallback<Long>(id -> {
										values.set("id", id);
										createLayersButtonsPanel(companyFilter, buttonPanel);
									}));
								}

								@Override
								protected Widget getAdditionals() {
									return null;
								}

								@Override
								protected void getLookup(String className, AsyncCallback<DataRange<CDObject>> callback) {
								}
							}.center();
						}));
					}));
					}
				});
				
				onPrepared.run();
			}
			
		});
		
		GwtUtils.addContextMenu(parenPanel, new ContextMenuPreparer() {
			@Override
			public void prepareContextMenu(ContextMenu menu, int x, int y, Runnable onPrepared) {
				menu.addItem("Добавить новый этаж..", new ScheduledCommand() {
					CDClass roomClass;
					@Override
					public void execute() {
						service.getCDClass(RoomLayer.class.getName(), new AlertAsyncCallback<>(a -> {
							roomClass = a;
							
						service.newCDObject(RoomLayer.class.getName(), new AlertAsyncCallback<>(cdObject -> {
							new ObjectEditorDialog("Создать", roomClass, cdObject) {

								@Override
								protected void onOK(CDObject values) {
									service.store(RoomLayer.class.getName(), values, new AlertAsyncCallback<Long>(id -> {
										values.set("id", id);
										createLayersButtonsPanel(companyFilter, buttonPanel);
									}));
								}

								@Override
								protected Widget getAdditionals() {
									return null;
								}

								@Override
								protected void getLookup(String className, AsyncCallback<DataRange<CDObject>> callback) {
								}
							}.center();
						}));
					}));
					}
				});
				
				onPrepared.run();
			}
		});
	}
}
