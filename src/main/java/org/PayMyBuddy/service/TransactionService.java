package org.PayMyBuddy.service;

import java.math.BigDecimal;
import java.util.List;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.TransactionRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IContactService iContactService;

    @Override
    public List<Transaction> getUserTransactions(User currentUser) {
        return transactionRepository.findBySenderOrReceiver(currentUser, currentUser);
    }

    @Override
    public List<User> getUserConnections(String userEmail) {
        return iContactService.getUserConnections(userEmail);
    }

    @Override
    public User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return iUserService.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // Dans TransactionService
    @Override
    @Transactional
    public void transferMoney(int transactionSender, int transactionReceiver, String description, BigDecimal amount, Model model) {
        // Récupérer l'utilisateur expéditeur et le destinataire à partir de leur ID
        User sender = iUserService.findById(transactionSender).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = iUserService.findById(transactionReceiver).orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Calculez le montant total prélevé (montant + frais)
        BigDecimal totalAmount = calculateTotalAmount(amount);

        BigDecimal senderBalance = sender.getBalance();
        if (senderBalance.compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Mettez à jour les soldes des utilisateurs
        updateSenderBalance(sender, totalAmount); // Soustrayez le montant total du solde de l'expéditeur
        updateReceiverBalance(receiver, amount); // Ajoutez le montant initial au solde du destinataire

        // Sauvegardez la transaction
        saveTransaction(sender, receiver, description, amount); // Enregistrez le montant initial de la transaction
        model.addAttribute("senderFirstName", sender.getFirstname());
        model.addAttribute("senderLastName", sender.getLastname());
    }


    private BigDecimal calculateTotalAmount(BigDecimal amount) {
        BigDecimal feePercentage = BigDecimal.valueOf(5); // 5% fee
        BigDecimal feeAmount = amount.multiply(feePercentage).divide(BigDecimal.valueOf(100));
        return amount.add(feeAmount);
    }

    private void updateSenderBalance(User sender, BigDecimal amount) {
        BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
        sender.setBalance(newSenderBalance);
        iUserService.updateUser(sender);
    }

    private void updateReceiverBalance(User receiver, BigDecimal amount) {
        BigDecimal newReceiverBalance = receiver.getBalance().add(amount);
        receiver.setBalance(newReceiverBalance);
        iUserService.updateUser(receiver);
    }

    private void saveTransaction(User sender, User receiver, String description, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount); // Enregistrez le montant initial de la transaction
        transactionRepository.save(transaction);
    }
}