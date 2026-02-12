package com.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class EmployeeManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagmentSystemApplication.class, args);
	}

}
