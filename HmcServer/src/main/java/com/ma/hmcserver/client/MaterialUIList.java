package com.ma.hmcserver.client;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;
import com.ma.common.gwtapp.client.ui.ContextMenu;
import com.ma.commonui.shared.cd.CDObject;

public class MaterialUIList extends CommonListPanel {

	static MyDataGridResources resource = GWT.create(MyDataGridResources.class);
	static MyContextMenuResources menuResource = GWT.create(MyContextMenuResources.class);

	public MaterialUIList(String title) {
		super(title, resource);
		resource.customization().ensureInjected();
		setAddButtonStyle("custom-button", true, "\uF067");
	}
	
	@Override
	protected void prepareContextMenu(ContextMenu menu, Set<CDObject> set) {
		if (!set.isEmpty())
			menu.addItem("Удалить", () -> {
				deleteItems(set);
			});
	}

}