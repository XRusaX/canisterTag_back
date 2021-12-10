package com.ma.hmcapp.shared;

import com.ma.commonui.shared.annotations.UILabel;

public enum CompanyType {
	@UILabel(label = "Производство канистр")
	CANISTER, //
	@UILabel(label = "Тестирование плат")
	TEST, //
	@UILabel(label = "Пользователи")
	CUSTOMER,//
}