package ru.aoit.hmcapp;

import javax.servlet.annotation.WebServlet;

import org.springframework.stereotype.Component;

import ru.aoit.appcommon.AppImpl;

@SuppressWarnings("serial")
@Component
@WebServlet("/app")
public class HmcApp extends AppImpl {

	public HmcApp() {
		super("HMC", true);
	}

}
