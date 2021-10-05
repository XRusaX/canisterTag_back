package com.ma.hmcrfidserver.client;

import com.google.gwt.user.client.ui.Widget;
import com.ma.common.gwt.client.commondata.CommonDataFlowList;
import com.ma.common.shared.cd.CDObject;

public class HmcFlowList extends CommonDataFlowList {

	public HmcFlowList() {
	}

	@Override
	protected Widget createWidget(CDObject t) {
		return new HmcTileWidget(t);
	}

}
