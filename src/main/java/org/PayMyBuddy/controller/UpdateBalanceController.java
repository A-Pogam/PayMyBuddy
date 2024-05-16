package org.PayMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class UpdateBalanceController {
    @GetMapping("/update-balance")
    public String updateBalance() {
        return "update-balance";
    }
}