package com.ma.canisterrfid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan(basePackages = "com.ma")
@ComponentScan(basePackages = "com.ma")
@EntityScan(basePackages = "com.ma")
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ma")
public class HmcCanisterTags {

	public static void main(String[] args) {
		SpringApplication.run(HmcCanisterTags.class, args);
	}

}
