package com.ma.hmcrfidserver.client;

import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwtapp.client.commondata.CommonDataFlowList;
import com.ma.common.gwtapp.client.ui.ContextMenu;
import com.ma.commonui.shared.cd.CDObject;

public abstract class HmcFlowList extends CommonDataFlowList {

	public HmcFlowList() {
		super("selection");
	}

	@Override
	protected Widget createWidget(CDObject t) {
		return new HmcTileWidget(t);
	}

	@Override
	protected void prepareContextMenu(ContextMenu menu, CDObject t, Runnable onPrepared) {
		super.prepareContextMenu(menu, t, onPrepared);
		if (t != null) {
			menu.addItem("Показать отчеты", () -> {
				showReports();
			});
		}

	}

	abstract void showReports();

}
