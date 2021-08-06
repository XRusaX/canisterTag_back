package ru.aoit.hmcrfidwriter;

import ru.nppcrts.common.shared.cd.UILabel;

public class CanisterSettings {
	@UILabel(label = "Средство")
	public String CANISTER_NAME;
	@UILabel(label = "Объем (ml)")
	public int CANISTER_VOLUME_ML;
}
