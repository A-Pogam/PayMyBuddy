package org.PayMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddConnectionController {
    @GetMapping("/add-connection")
    public String addConnection() {
        return "add-connection";
    }
}
