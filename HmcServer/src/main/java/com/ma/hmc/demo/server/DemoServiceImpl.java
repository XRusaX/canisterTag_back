package com.ma.hmc.demo.server;

import org.springframework.stereotype.Service;

import com.ma.hmc.demo.client.DemoService;

@Service("demo")
public class DemoServiceImpl implements DemoService {

	@Override
	public String hello(String s) {
		return "Hello " + s + "!";
	}

}
