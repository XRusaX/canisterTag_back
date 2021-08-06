package ru.aoit.hmcrfidserver.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aoit.appcommon.App;
import ru.aoit.appcommon.Auth;
import ru.aoit.appcommon.CommonApiAdapter;
import ru.aoit.appcommon.CommonData;
import ru.aoit.appcommon.MsgLogger;
import ru.aoit.appcommon.SettingsHolder;
import ru.aoit.appcommon.SettingsHolderImpl;

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
