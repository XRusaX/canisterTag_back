package com.ma.hmc.test.rpcinterface;

import com.ma.common.cd.CDUtils;
import com.ma.hmc.test.shared.HmcTestType;

public class Test {
	public static void main(String[] args) throws NoSuchFieldException {
		System.out.println(CDUtils.getLabel(HmcTestType.BUZZ));
	}

}
