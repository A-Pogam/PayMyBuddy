package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private ContactService connectionService;


    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Récupérer les informations de contact de l'utilisateur
        List<Contact> connections = connectionService.getUserConnections(userEmail);

        // Transmettre les informations de contact au modèle
        model.addAttribute("contacts", connections);

        return "profile";
    }
}