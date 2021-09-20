package com.ma.hmcrfidwriter;

import com.ma.common.shared.cd.UILabel;

public class ClientSettings {
	@UILabel(label = "Сервер")
	public String serverUrl;
	@UILabel(label = "Симулятор библиотеки")
	public boolean simLib;
}
