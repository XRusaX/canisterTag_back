package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;

public class OperatorList extends CommonListPanel {

	static MyDataGridResources resource = GWT.create(MyDataGridResources.class);
	static MyContextMenuResources menuResource = GWT.create(MyContextMenuResources.class);

	public OperatorList(String title) {
		super(title, resource);
		resource.customization().ensureInjected();
		setAddButtonStyle("custom-button", true, "\uF067");
	}

}
