package com.paymybuddy.dataLayer;

import model.DBUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.DBUserRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"controller"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model")
public class DataLayerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DataLayerApplication.class, args);

		// Récupérer les références aux beans nécessaires
		DBUserRepository userRepository = context.getBean(DBUserRepository.class);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// Créer un nouvel utilisateur avec mot de passe encodé
		DBUser newUser = new DBUser();
		newUser.setEmail("newuser@example.com");
		newUser.setPassword(passwordEncoder.encode("new")); // Encodez le mot de passe
		newUser.setRole("USER");

		// Enregistrer l'utilisateur avec mot de passe encodé
		userRepository.save(newUser); // Utiliser la méthode save standard
	}
}
