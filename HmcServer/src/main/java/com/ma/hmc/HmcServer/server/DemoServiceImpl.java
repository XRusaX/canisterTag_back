package com.ma.hmc.HmcServer.server;

import org.springframework.stereotype.Service;

import com.ma.hmc.HmcServer.client.DemoService;

@Service("demo")
public class DemoServiceImpl implements DemoService {

	@Override
	public String hello(String s) {
		return "Hello " + s + "!";
	}

}
