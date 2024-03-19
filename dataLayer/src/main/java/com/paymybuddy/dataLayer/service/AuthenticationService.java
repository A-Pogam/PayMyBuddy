package com.paymybuddy.dataLayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public boolean authenticate(String email, String enteredPassword) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            return false; // L'utilisateur n'existe pas
        }

        // Récupérer les informations de l'utilisateur
        String encodedPassword = userDetails.getPassword(); // Mot de passe encodé
        GrantedAuthority userRole = userDetails.getAuthorities().iterator().next(); // Rôle de l'utilisateur

        // Comparer les mots de passe
        return userDetailsService.passwordsMatch(enteredPassword, encodedPassword);
    }
}
