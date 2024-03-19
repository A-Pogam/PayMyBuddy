package com.paymybuddy.dataLayer.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final CustomUserDetailsService userDetailsService;

    public AuthenticationService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean authenticate(String email, String enteredPassword) {
        // Utilise userDetailsService.loadUserByUsername(email) et le reste de votre logique d'authentification ici
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            return false; // L'utilisateur n'existe pas
        }

        // Récupère les informations de l'utilisateur
        String encodedPassword = userDetails.getPassword(); // Mot de passe encodé
        GrantedAuthority userRole = userDetails.getAuthorities().iterator().next(); // Rôle de l'utilisateur

        // Compare les mots de passe
        return userDetailsService.passwordsMatch(enteredPassword, encodedPassword);
    }
}
