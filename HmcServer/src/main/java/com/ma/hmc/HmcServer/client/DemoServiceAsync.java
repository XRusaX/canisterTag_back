package com.ma.hmc.HmcServer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DemoServiceAsync {
	void hello(String s, AsyncCallback<String> callback);
}
