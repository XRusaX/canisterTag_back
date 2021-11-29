package com.ma.hmc.HmcServer.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtservice/demo")
public interface DemoService  extends RemoteService{
	String hello(String s);
}
