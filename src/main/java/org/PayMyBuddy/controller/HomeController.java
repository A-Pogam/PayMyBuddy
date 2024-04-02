package org.PayMyBuddy.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Controller
public class HomeController {

    private final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home() {
        logger.info("Received request to access home page.");
        return "home";
    }
}
