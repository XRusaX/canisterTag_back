package com.ma.hmcrfidserver.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.App;
import com.ma.appcommon.Auth;
import com.ma.appcommon.CommonApiAdapter;
import com.ma.appcommon.CommonData;
import com.ma.appcommon.MsgLogger;
import com.ma.appcommon.SettingsHolder;
import com.ma.appcommon.SettingsHolderImpl;

@Component
public class CommonApiAdapterImpl implements CommonApiAdapter {

	@Autowired
	private Auth authComponent;

	@Autowired
	private CommonData commonDataImpl;

	@Autowired
	private MsgLogger msgLogger;

	@Autowired
	private App app;

	@Autowired
	private SettingsHolderImpl<?, ?> settingsHolder;

	@Override
	public CommonData getCommonData() {
		return commonDataImpl;
	}

	@Override
	public Auth getAuth() {
		return authComponent;
	}

	@Override
	public MsgLogger getMsgLogger() {
		return msgLogger;
	}

	@Override
	public App getApp() {
		return app;
	}

	@Override
	public SettingsHolder<?, ?> getSettingsHolder() {
		return settingsHolder;
	}
}
