package com.paymybuddy.dataLayer;

import com.paymybuddy.dataLayer.model.DBUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.paymybuddy.dataLayer.repository.DBUserRepository;

@SpringBootApplication
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
