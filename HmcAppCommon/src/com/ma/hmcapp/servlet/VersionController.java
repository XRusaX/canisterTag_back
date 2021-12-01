package com.ma.hmcapp.servlet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ma.hmc.iface.version.Version;
import com.ma.hmc.iface.version.VersionInfo;

@RestController
public class VersionController {
	@GetMapping(value = "/api/version")
	public synchronized String version() {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.version = Version.VERSION;
		return new Gson().toJson(versionInfo);
	}
}
