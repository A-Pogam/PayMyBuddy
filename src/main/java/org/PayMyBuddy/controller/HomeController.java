package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        logger.info("Received request to access home page.");

        // Récupérer l'utilisateur connecté
        String userEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Ajouter les informations de l'utilisateur au modèle
        model.addAttribute("user", user);

        return "home";
    }
}
