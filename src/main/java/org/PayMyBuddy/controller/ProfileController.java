package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {



    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }



}