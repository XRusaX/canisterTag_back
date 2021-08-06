package ru.aoit.hmcapp;

import org.springframework.stereotype.Component;

import ru.aoit.appcommon.WorkingDir;
import ru.nppcrts.common.app.AppData;

@Component
public class HmcWorkingDir extends WorkingDir{

	public HmcWorkingDir() {
		super(AppData.getWorkingDir("HMC"));
	}

}
