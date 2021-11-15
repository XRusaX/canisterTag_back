package com.ma.hmcrfidserver.client;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.DataRange;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.DataChangeEvent;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.commonui.GwtUIBuilder;
import com.ma.common.gwtapp.client.ui.panel.HorPanel;
import com.ma.common.shared.eventbus.EventBus;
import com.ma.commonui.shared.cd.CDClass;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.commonui.shared.cd.ObjectEditor;
import com.ma.hmcdb.shared.Hmc;

public class PropertiesPanel extends VerticalPanel {
	private CDClass cdClass;
	public final Filter filter = new Filter();
	private Widget propPanel;
	private boolean editable;
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	public PropertiesPanel(EventBus eventBus) {
		FontAwesomeBundle.INSTANCE.fontAwesome().ensureInjected();
		eventBus.registerListener(SelChangeEvent.class, sce -> {
			clear();
			if (sce.clazz.getName().equals(Hmc.class.getName()) && sce.selectedSet.size() == 1) {
				GWT.log("hmc selected");
				cdClass = sce.cdClass;
				ObjectEditor objectEditor = new ObjectEditor(new GwtUIBuilder()) {
					@Override
					protected void getLookup(String className, Consumer<List<CDObject>> asyncCallback) {
						service.loadRange(className, new Filter(), null, new AlertAsyncCallback<DataRange<CDObject>>(
								result -> asyncCallback.accept(result.range)));
					}
				}.create(cdClass, !editable);
				objectEditor.toUI(sce.selectedSet.iterator().next());
				objectEditor.setStyle("enum-field");
				propPanel = (Widget) objectEditor.asPlatfomWidget();
				propPanel.getElement().getStyle().setMargin(20, Unit.PX);
				propPanel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				VerticalPanel container = new VerticalPanel();

				Button editModeBtn = new Button();
				editModeBtn.setTitle("Редактировать");
				editModeBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						editable = !editable;
						objectEditor.setRO(!editable);
					}

				});

				Button okBtn = new Button("OK");
				okBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						service.store(cdClass.name, objectEditor.fromUI(), new AlertAsyncCallback<>(v -> {
							eventBus.post(new DataChangeEvent(sce.clazz));
						}));
						objectEditor.setRO(true);
					}

				});

				Button cancelBtn = new Button("ОТМЕНА");
				cancelBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						clear();
					}

				});

				HorPanel editButtonContainerPanel = new HorPanel();
				editButtonContainerPanel.setSp(2);
				editButtonContainerPanel.setWidth("100%");
				editButtonContainerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				editModeBtn.setStyleName("button-edit");
				editButtonContainerPanel.add(editModeBtn);

//				HorPanel okCancelContainerPanel = new HorPanel();
				Panel okCancelContainerPanel = new HorizontalPanel();
				okCancelContainerPanel.setWidth("100%");
//				okCancelContainerPanel.setSpacing(0);
//				okCancelContainerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				okBtn.setStyleName("button-ok");
				cancelBtn.setStyleName("button-cancel");
				okCancelContainerPanel.add(cancelBtn);
				okCancelContainerPanel.add(okBtn);
				okCancelContainerPanel.setStyleName("okCancelPanel");
				okBtn.setWidth("85px");

				container.add(editButtonContainerPanel);
				container.add(propPanel);
				container.add(okCancelContainerPanel);
//				container.add(editButton);

				container.setStyleName("prop-panel");
//				setWidget(container);
				add(container);

			} else {
				GWT.log("other selected ");
				if (propPanel != null)
					propPanel.setStyleName("prop-panel-hide");
			}
//					panel.createColumns(cdClass);
//					panel.refresh();
		});

//		});
	}

}
