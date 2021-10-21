package com.ma.hmcrfidserver.client;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.ICommonListPanel;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.commonui.GwtUIBuilder;
import com.ma.common.shared.eventbus.EventBus;
import com.ma.commonui.shared.cd.CDClass;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.commonui.shared.cd.ObjectEditor;
import com.ma.hmcdb.shared.Hmc;

public class PropertiesPanel extends SimplePanel {
	private EventBus eventbus;
	private CDClass cdClass;
	public final Filter filter = new Filter();
	private ICommonListPanel panel;
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	public PropertiesPanel(EventBus eventBus) {
		this.eventbus = eventBus;
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
				setWidget(refreshPanel((Widget) objectEditor.asPlatfomWidget()));

			} else {
				GWT.log("other selected ");
			}
//					panel.createColumns(cdClass);
//					panel.refresh();
		});

//		});
	}

	private SimplePanel refreshPanel(Widget widget) {
		SimplePanel w = new SimplePanel();
		w.setWidth("100%");
		w.setHeight("20%");
		w.getElement().getStyle().setBackgroundColor("white");
		w.add(widget);
		return w;
	}
}
