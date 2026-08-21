package com.projoker.joker_studio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class JokerStudioApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokerStudioApplication.class, args);
	}

}
