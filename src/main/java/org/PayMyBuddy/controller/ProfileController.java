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


    @GetMapping("/profile")
    public String profil() {
        return "profile";
    }
}