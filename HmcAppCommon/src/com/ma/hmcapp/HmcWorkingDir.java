package com.ma.hmcapp;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ma.appcommon.WorkingDir;
import com.ma.common.app.AppData;

@Component
public class HmcWorkingDir extends WorkingDir {

	public HmcWorkingDir(ApplicationContext context) {
		super(AppData.getWorkingDir(context.getApplicationName().isEmpty() ? "dev" : context.getApplicationName()));
	}

}
