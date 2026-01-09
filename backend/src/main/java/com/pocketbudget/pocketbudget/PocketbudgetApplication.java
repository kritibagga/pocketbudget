package com.pocketbudget.pocketbudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.pocketbudget.pocketbudget.security.JwtProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class PocketbudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketbudgetApplication.class, args);
	}

}
