package org.PayMyBuddy.controller;

import java.math.BigDecimal;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransferController {

    @Autowired
    private ITransactionService iTransactionService;

    //Display transfer page
    @GetMapping("/transfer")
    public String transfer(Model model) {
        User currentUser = iTransactionService.getCurrentUser();
        model.addAttribute("contacts", iTransactionService.getUserConnections(currentUser.getEmail()));
        model.addAttribute("transactions", iTransactionService.getUserTransactions(currentUser));
        return "transfer";
    }

    //Doing the internal money transfer
    @PostMapping("/transfer")
    public String performTransfer(@RequestParam int transaction_receiver,
                                  @RequestParam String description,
                                  @RequestParam BigDecimal amount,
                                  Model model) {
        // Récupérer l'identifiant de l'utilisateur connecté à partir du service
        int senderId = iTransactionService.getCurrentUserId();
        // Appeler la méthode transferMoney avec les identifiants d'utilisateur et les autres paramètres nécessaires
        iTransactionService.transferMoney(senderId, transaction_receiver, description, amount, model);
        return "redirect:/transfer?success=Transfer successful";
    }
}
