package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;

    //Display transfer page
    @GetMapping("/transfer")
    public String transfer(Model model) {
        DBUser currentUser = transferService.getCurrentUser();
        model.addAttribute("contacts", transferService.getUserConnections(currentUser.getEmail()));
        model.addAttribute("transactions", transferService.getUserTransactions(currentUser.getId()));
        return "transfer";
    }

    //Doing the internal money transfer
    @PostMapping("/transfer")
    public String performTransfer(@RequestParam String receiverEmail,
                                  @RequestParam String description,
                                  @RequestParam BigDecimal amount,
                                  Model model) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        transferService.transferMoney(senderEmail, receiverEmail, description, amount, model);
        return "redirect:/transfer?success=Transfer successful";
    }
}
