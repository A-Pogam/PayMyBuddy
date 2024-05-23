package org.PayMyBuddy.controller;

import org.PayMyBuddy.service.contracts.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConnectionController {

    @Autowired
    IContactService iContactService;

    @GetMapping("/connection")
    public String addConnection() {
        return "connection";
    }

    @PostMapping("/connection")
    public String addContact(@RequestParam("contactEmail") String contactEmail, RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur actuellement authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String initializerEmail = authentication.getName();

        // Créer la connexion entre l'utilisateur actuel et le nouveau contact
        iContactService.createConnectionBetweenTwoUsers(initializerEmail, contactEmail);

        // Rediriger avec un message de succès
        redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully!");

        return "connection";
    }
}