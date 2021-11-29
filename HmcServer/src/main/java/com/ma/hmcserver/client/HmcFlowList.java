package com.ma.hmcserver.client;

import java.util.ArrayList;

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

		if (t == null)
			menu.addItem("Создать", () -> newItem());

		if (t != null) {
			menu.addItem("Удалить", () -> {
				ArrayList<CDObject> list = new ArrayList<>();
				list.add(t);
				deleteItems(list);
			});
		}
		if (t != null)
			menu.addItem("Показать отчеты", () -> {
				showReports();
			});
		
		onPrepared.run();
	}

	@Override
	protected void onDoubleClick(CDObject t) {
		return;
	}

	abstract void showReports();

}
