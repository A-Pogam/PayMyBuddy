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
    public String performTransfer(@RequestParam String receiverEmail,
                                  @RequestParam String description,
                                  @RequestParam BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = authentication.getName();

        // Récupérer l'ID de l'utilisateur expéditeur à partir de son email
        Optional<DBUser> senderOptional = dbUserRepository.findByEmail(senderEmail);
        if (senderOptional.isEmpty()) {
            // Gérer le cas où l'utilisateur expéditeur n'est pas trouvé
            throw new RuntimeException("Sender not found");
        }
        Integer senderId = senderOptional.get().getId();

        // Récupérer l'ID de l'utilisateur destinataire à partir de son email
        Optional<DBUser> receiverOptional = dbUserRepository.findByEmail(receiverEmail);
        if (receiverOptional.isEmpty()) {
            // Gérer le cas où l'utilisateur destinataire n'est pas trouvé
            throw new RuntimeException("Receiver not found");
        }
        Integer receiverId = receiverOptional.get().getId();

        // Appeler la méthode transferMoney avec les identifiants et les autres paramètres
        transferService.transferMoney(senderId, receiverId, description, amount);
        return "redirect:/transfer?success=Transfer successful";
    }









}
