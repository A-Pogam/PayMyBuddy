package com.paymybuddy.dataLayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("controller")
public class DataLayerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

}
