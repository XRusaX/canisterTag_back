package com.ma.hmc.iface.boardtest.shared;

import com.ma.commonui.shared.annotations.UILabel;

public enum HmcTestStatus {
	@UILabel(label="Успешно")
	SUCSESS, //
	@UILabel(label="Неуспешно")
	FAILURE, //
}