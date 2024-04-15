package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.repository.DBUserRepository;
import org.PayMyBuddy.service.ConnectionService;
import org.PayMyBuddy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class TransferController {

    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private TransferService transferService;

    @Autowired
    private DBUserRepository dbUserRepository;

    @GetMapping("/transfer")
    public String transfer(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Récupère l'ID de l'utilisateur à partir de son e-mail
        Optional<DBUser> userOptional = dbUserRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            throw new RuntimeException("User not found");
        }
        Long userId = userOptional.get().getId().longValue(); // Convertir l'ID en Long

        List<Contact> connections = connectionService.getUserConnections(userEmail);
        model.addAttribute("contacts", connections);

        // Appel de la méthode getUserTransactions avec l'ID de l'utilisateur
        List<Transaction> transactions = transferService.getUserTransactions(userId);
        model.addAttribute("transactions", transactions);

        return "transfer";
    }



    @PostMapping("/transfer")
    public String performTransfer(@RequestParam Integer transaction_id,
                                  @RequestParam Integer sender_id,
                                  @RequestParam Integer receiver_id,
                                  @RequestParam String description,
                                  @RequestParam LocalDateTime date,
                                  @RequestParam BigDecimal amount,
                                  @RequestParam String receiver_first_name,
                                  @RequestParam String receiver_last_name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = authentication.getName();

        // Appeler la méthode transferMoney avec les identifiants et les autres paramètres
        transferService.transferMoney(transaction_id, sender_id, receiver_id, description, date, amount, receiver_first_name, receiver_last_name);
        return "redirect:/transfer?success=Transfer successful";
    }






}
