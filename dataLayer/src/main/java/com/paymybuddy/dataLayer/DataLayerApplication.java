package com.paymybuddy.dataLayer;

import model.DBUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.DBUserRepository;
import configuration.ThymeleafConfiguration;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataLayerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DataLayerApplication.class, args);
	}
}
