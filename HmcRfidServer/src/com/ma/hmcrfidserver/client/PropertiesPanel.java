package com.ma.hmcrfidserver.client;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.commonui.GwtUIBuilder;
import com.ma.common.shared.eventbus.EventBus;
import com.ma.commonui.shared.cd.CDClass;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.commonui.shared.cd.ObjectEditor;
import com.ma.hmcdb.shared.Hmc;

public class PropertiesPanel extends VerticalPanel {
	private CDClass cdClass;
	public final Filter filter = new Filter();
	private Widget propPanel;

	public PropertiesPanel(EventBus eventBus) {
		eventBus.registerListener(SelChangeEvent.class, sce -> {
//			CDObject refId = sce.selectedSet.size() == 1 ? sce.selectedSet.iterator().next() : null;
//			cdClass.fields.forEach((n, v) -> {
//			 GWT.log("field " + n);
			clear();
			if (sce.clazz.getName().equals(Hmc.class.getName()) && sce.selectedSet.size() == 1) {
//					if (refId != null) {
				GWT.log("hmc selected");
//				setWidget(refreshPanel());
				cdClass = sce.cdClass;
				ObjectEditor objectEditor = new ObjectEditor(new GwtUIBuilder()) {
					@Override
					protected void getLookup(String className, Consumer<List<CDObject>> asyncCallback) {
					}
				}.create(cdClass);
				objectEditor.toUI(sce.selectedSet.iterator().next());
				propPanel = (Widget) objectEditor.asPlatfomWidget();
				propPanel.getElement().getStyle().setMargin(20, Unit.PX);
				propPanel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				VerticalPanel container = new VerticalPanel();
//				Button editButton = new Button("edit");
				DivButtonWithText editButton = new DivButtonWithText("");
				editButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Window.alert("HELLO");
					}
				});
				editButton.setStyleName("button");
				container.add(propPanel);
				container.add(editButton);
				container.setCellHorizontalAlignment(editButton, ALIGN_RIGHT);
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
