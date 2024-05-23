package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Controller
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return "register";
        }

        boolean isRegistered = userService.registerUser(user);

        if (!isRegistered) {
            logger.warn("Email already exists: {}", user.getEmail());
            return "redirect:/register?error=emailExists";
        }

        logger.info("Utilisateur enregistré avec succès : {}", user.getEmail());
        return "redirect:/home";
    }
}
