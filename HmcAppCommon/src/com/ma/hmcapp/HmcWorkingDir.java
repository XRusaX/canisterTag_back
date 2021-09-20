package com.ma.hmcapp;

import org.springframework.stereotype.Component;

import com.ma.appcommon.WorkingDir;
import com.ma.common.app.AppData;

@Component
public class HmcWorkingDir extends WorkingDir{

	public HmcWorkingDir() {
		super(AppData.getWorkingDir("HMC"));
	}

}
