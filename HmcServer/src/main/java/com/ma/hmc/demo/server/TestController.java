package com.ma.hmc.demo.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("api/zzz")
	public String fff() {
		return "zzz";
	}

}
