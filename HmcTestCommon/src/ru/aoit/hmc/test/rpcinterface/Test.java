package ru.aoit.hmc.test.rpcinterface;

import ru.aoit.hmc.test.shared.HmcTestType;
import ru.nppcrts.common.cd.CDUtils;

public class Test {
	public static void main(String[] args) throws NoSuchFieldException {
		System.out.println(CDUtils.getLabel(HmcTestType.BUZZ));
	}

}
