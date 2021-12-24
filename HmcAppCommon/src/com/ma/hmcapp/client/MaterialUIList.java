package com.ma.hmcapp.client;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;
import com.ma.common.gwtapp.client.ui.ContextMenu;
import com.ma.common.gwtapp.client.ui.panel.HorPanel;
import com.ma.commonui.shared.cd.CDObject;

public class MaterialUIList extends CommonListPanel {

	static MyDataGridResources resource = GWT.create(MyDataGridResources.class);
//	static MyContextMenuResources menuResource = GWT.create(MyContextMenuResources.class);

	public MaterialUIList(String title) {
		super(title, resource);
		resource.customization().ensureInjected();
	}

	@Override
	protected void placeAdditionals() {
		HorPanel footer = getFooter();
		footer.alignHor(HasHorizontalAlignment.ALIGN_RIGHT);
		Button button = new Button("\uF067", (ClickHandler) event -> getPanelWrapper().newObject());
		button.setStyleName("add-button");
		footer.add1(button);
		footer.setWidth("100%");
		setAddButton(button);
	}

	@Override
	protected void prepareContextMenu(ContextMenu menu, Set<CDObject> set) {
		if (!set.isEmpty())
			menu.addItem("Удалить", () -> {
				deleteItems(set);
			});
	}

	// TODO: заделка на будущую кастомизацию внешнего вида
	protected void addResource() {
	};

}
