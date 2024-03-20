package com.paymybuddy.dataLayer;

import model.DBUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.DBUserRepository;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataLayerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DataLayerApplication.class, args);

		// Récupère les références aux beans nécessaires
		DBUserRepository userRepository = context.getBean(DBUserRepository.class);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// Crée un nouvel utilisateur avec mot de passe encodé
		DBUser newUser = new DBUser();
		newUser.setEmail("newuser@example.com");
		newUser.setPassword(passwordEncoder.encode("new")); // Encodez le mot de passe
		newUser.setRole("USER");

		// Enregistre l'utilisateur avec mot de passe encodé
		userRepository.save(newUser); // Utiliser la méthode save standard
	}
}
