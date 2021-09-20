package com.ma.hmc.test.shared;

import com.ma.common.shared.cd.UILabel;

public enum HmcTestStatus {
	@UILabel(label="Успешно")
	SUCSESS, //
	@UILabel(label="Неуспешно")
	FAILURE, //
}