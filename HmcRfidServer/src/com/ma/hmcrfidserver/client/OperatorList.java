package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.ma.common.gwtapp.client.commondata.CommonListPanel;

public class OperatorList extends CommonListPanel {

	static MyDataGridResources resource = GWT.create(MyDataGridResources.class);

	public OperatorList(String title, Integer refreshPeriod) {
		super(title, resource);
	}

}
