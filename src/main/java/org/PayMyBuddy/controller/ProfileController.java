package org.PayMyBuddy.controller;

import java.util.List;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private IContactService iContactService;

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Récupérer les informations de contact de l'utilisateur
        List<User> connections = iContactService.getUserConnections(userEmail);

        // Transmettre les informations de contact au modèle
        model.addAttribute("contacts", connections);

        return "profile";
    }
}