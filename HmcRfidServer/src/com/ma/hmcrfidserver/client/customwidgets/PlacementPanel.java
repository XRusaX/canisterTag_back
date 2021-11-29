package com.ma.hmcrfidserver.client.customwidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.ui.panel.DockLayoutPanelX;

public class PlacementPanel extends DockLayoutPanelX {
	public PlacementPanel(PageEventBus eventBus) {
		super(Unit.PCT);


		ScrollPanel scPanel = new ScrollPanel();
		scPanel.getVerticalScrollPosition();
		LayersPanel layersPanel = new LayersPanel(eventBus, scPanel);
		scPanel.add(layersPanel);
		addW(scPanel, 20);
		addW(new RoomsPanel(eventBus), 20);
		
		ScrollPanel scrollMapPanel = new ScrollPanel();
		LayerMapPanel layerMapPanel = new LayerMapPanel(eventBus, scrollMapPanel);
		scrollMapPanel.add(layerMapPanel);
		
		scrollMapPanel.addDomHandler(new MouseWheelHandler() {
			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				DOM.eventPreventDefault(DOM.eventGetCurrentEvent()); //prevent vertical scroll
			}
		}, MouseWheelEvent.getType());
		addW(scrollMapPanel, 60);
	}
}
