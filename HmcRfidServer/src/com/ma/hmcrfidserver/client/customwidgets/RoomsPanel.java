package com.ma.hmcrfidserver.client.customwidgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;
import com.ma.common.gwtapp.client.commondata.CommonListPanelWrapper;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.RoomLayer;

public class RoomsPanel extends ResizeComposite {

	private CommonListPanelWrapper commonListPanelWrapper;
	private PageEventBus eventBus;
	private CDObject room;
	private CDObject layer;
	
	public RoomsPanel(PageEventBus eventBus) {
		this.eventBus = eventBus;
		commonListPanelWrapper = new CommonListPanelWrapper(new CommonListPanel(null), Room.class, eventBus);
		
		config();
		initWidget(commonListPanelWrapper);
	}

	private void config() {

		
	}
}
