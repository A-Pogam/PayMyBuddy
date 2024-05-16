package org.PayMyBuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home() {
        logger.info("Received request to access home page.");
        return "home";
    }
}