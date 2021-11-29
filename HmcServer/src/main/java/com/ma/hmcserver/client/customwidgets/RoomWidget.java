package com.ma.hmcserver.client.customwidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;
import com.ma.commonui.shared.cd.CDObject;

public class RoomWidget {
	private VertPanel panel;
	private CDObject room;

	public RoomWidget(CDObject room, MouseWheelHandler zoomHandler) {
		this.setRoom(room);
		Label label = new Label(room.get("name"));
		setPanel(new VertPanel());
		getPanel().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		getPanel().add1(label);
		getPanel().add1(new Label("10.02.2021"));
		getPanel().setBorderWidth(1);
		getPanel().getElement().getStyle().setBackgroundColor("cyan");
		
		getPanel().addDomHandler(zoomHandler, MouseWheelEvent.getType());
	}

	public CDObject getRoom() {
		return room;
	}

	public void setRoom(CDObject room) {
		this.room = room;
	}

	public VertPanel getPanel() {
		return panel;
	}

	public void setPanel(VertPanel panel) {
		this.panel = panel;
	}
}
