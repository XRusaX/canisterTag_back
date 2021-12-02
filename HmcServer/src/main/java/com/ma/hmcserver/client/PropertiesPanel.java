package com.ma.hmcserver.client;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

public class PropertiesPanel extends VerticalPanel {
	private CDClass cdClass;
	public final Filter filter = new Filter();
	private Widget propPanel;
	private ObjectEditor objectEditor;
	private Panel additionalsPanel;
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	public PropertiesPanel(EventBus eventBus, String className) {
		FontAwesomeBundle.INSTANCE.fontAwesome().ensureInjected();
		eventBus.registerListener(SelChangeEvent.class, sce -> {
			clear();
			showPropertiesPanel(eventBus, className, sce);
		});

//		});

	}

	private void showPropertiesPanel(EventBus eventBus, String className, SelChangeEvent sce) {

		if (sce.clazz.getName().equals(className) && sce.selectedSet.size() == 1) {
			GWT.log(sce.clazz.getName() + " item selected");
			cdClass = sce.cdClass;
			objectEditor = new ObjectEditor(new GwtUIBuilder()) {
				@Override
				protected void getLookup(String className, Consumer<List<CDObject>> asyncCallback) {
					service.loadRange(className, new Filter(), null,
							new AlertAsyncCallback<DataRange<CDObject>>(result -> asyncCallback.accept(result.range)));
				}
			}.create(cdClass, true);
			CDObject cdObject = sce.selectedSet.iterator().next();
			objectEditor.toUI(cdObject);
			if (objectEditor.readOnly)
				objectEditor.setStyle("enum-field");
			propPanel = (Widget) objectEditor.asPlatfomWidget();
			propPanel.getElement().getStyle().setMargin(10, Unit.PX);
			propPanel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			VerticalPanel container = new VerticalPanel();

			Button editModeBtn = new Button();
			editModeBtn.setTitle("Редактировать");
			editModeBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					updateReadOnlyMode(!objectEditor.readOnly);
				}

			});

			Button okBtn = new Button("OK");
			okBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					service.store(cdClass.name, objectEditor.fromUI(), new AlertAsyncCallback<>(v -> {
						eventBus.post(new DataChangeEvent(sce.clazz));
					}));
					updateReadOnlyMode(true);
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

			additionalsPanel = getAdditionals(cdObject);
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
			if (additionalsPanel != null)
				container.add(additionalsPanel);
			container.add(propPanel);
			container.add(okCancelContainerPanel);
//				container.add(editButton);

			container.setStyleName("prop-panel");
//				setWidget(container);
			setWidth("100%");
//				setHeight("100%");
			setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			add(container);
		} else {
			GWT.log("other selected ");
			if (propPanel != null)
				propPanel.setStyleName("prop-panel-hide");
		}
//					panel.createColumns(cdClass);
//					panel.refresh();
	}

	protected Panel getAdditionals(CDObject o) {
		return null;
	}


	protected void updateReadOnlyMode(boolean editMode) {
		objectEditor.setRO(editMode);
	}


}
