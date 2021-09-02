package ru.aoit.hmc.test.shared;

import ru.nppcrts.common.shared.cd.UILabel;

public enum HmcTestStatus {
	@UILabel(label="Успешно")
	SUCSESS, //
	@UILabel(label="Неуспешно")
	FAILURE, //
}