package org.PayMyBuddy.service;

import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.repository.DBUserRepository;
import org.PayMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.PayMyBuddy.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DBUserRepository userRepository;

    public void makeTransaction(String senderEmail, String receiverEmail, BigDecimal amount, String description) {
        // Récupérer les informations sur l'expéditeur et le destinataire
        DBUser sender = userRepository.findByEmail(senderEmail).orElseThrow(() -> new UsernameNotFoundException("Expéditeur introuvable"));
        DBUser receiver = userRepository.findByEmail(receiverEmail).orElseThrow(() -> new UsernameNotFoundException("Destinataire introuvable"));

        // Mettre à jour les soldes des expéditeurs et des destinataires
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Enregistrer la transaction
        Transaction transaction = new Transaction();
        transaction.setSenderEmail(senderEmail);
        transaction.setReceiverEmail(receiverEmail);
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setSenderFirstName(sender.getFirstname());
        transaction.setSenderLastName(sender.getLastname());

        transactionRepository.save(transaction);
    }
}
