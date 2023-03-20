package com.carla.erp_senseve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
public class ErpSenseveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpSenseveApplication.class, args);
	}

}
