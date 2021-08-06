package ru.aoit.hmcrfidwriter;

import ru.nppcrts.common.shared.cd.UILabel;

public class ClientSettings {
	@UILabel(label = "Сервер")
	public String serverUrl;
	@UILabel(label = "Симулятор библиотеки")
	public boolean simLib;
}
