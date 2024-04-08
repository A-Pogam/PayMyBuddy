package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ConnectionController {
    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/connection")
    public String addConnection() {
        return "connection";
    }

    @PostMapping("/connection")
    public String addConnection(@RequestParam("userEmail") String userEmail,
                                @RequestParam("contactEmail") String contactEmail,
                                Model model) {
        try {
            Contact newConnection = connectionService.createConnectionBetweenTwoUsers(userEmail, contactEmail);
            model.addAttribute("message", "La connexion avec " + contactEmail + " a été ajoutée avec succès!");;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "La connexion avec " + contactEmail + " a rencontré un problème");
        }
        return "add-connection";
    }
}
