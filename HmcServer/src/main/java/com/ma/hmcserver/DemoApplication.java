package com.ma.hmcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan(basePackages = "com.ma")
@ComponentScan(basePackages = "com.ma")
@EntityScan(basePackages = "com.ma")
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static String getAppName(ApplicationContext context) {
		return context.getApplicationName().isEmpty() ? "HMC_dev" : context.getApplicationName();
	}

}
