package com.ma.hmc.demo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DemoServiceAsync {
	void hello(String s, AsyncCallback<String> callback);
}
