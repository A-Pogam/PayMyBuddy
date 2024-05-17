package org.PayMyBuddy.service;

import java.math.BigDecimal;
import java.util.List;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ITransactionRepository;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private IUserRepository dbUserRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IContactService iContactService;

    public void transferMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount, Model model) {
        User sender = dbUserRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = dbUserRepository.findByEmail(receiverEmail).orElseThrow(() -> new RuntimeException("Receiver not found"));

        BigDecimal senderBalance = sender.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal finalAmount = calculateFinalAmount(amount);
        updateSenderBalance(sender, finalAmount.negate());
        updateReceiverBalance(receiver, finalAmount);

        saveTransaction(sender, receiver, description, finalAmount);
        model.addAttribute("senderFirstName", sender.getFirstname());
        model.addAttribute("senderLastName", sender.getLastname());
    }

    private BigDecimal calculateFinalAmount(BigDecimal amount) {
        BigDecimal feePercentage = BigDecimal.valueOf(0.5);
        BigDecimal feeAmount = amount.multiply(feePercentage.divide(BigDecimal.valueOf(100)));
        return amount.subtract(feeAmount);
    }

    private void updateSenderBalance(User sender, BigDecimal amount) {
        BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
        sender.setBalance(newSenderBalance);
        dbUserRepository.save(sender);
    }

    private void updateReceiverBalance(User receiver, BigDecimal amount) {
        BigDecimal newReceiverBalance = receiver.getBalance().add(amount);
        receiver.setBalance(newReceiverBalance);
        dbUserRepository.save(receiver);
    }

    private void saveTransaction(User senderId, User receiverId, String description, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSender_id(senderId);
        transaction.setReceiver_id(receiverId);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(Integer userId) {
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    public List<User> getUserConnections(String userEmail) {
        return iContactService.getUserConnections(userEmail);
    }

    public User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return dbUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }


    public String getReceiverName(Integer receiverId) {
        // Récupérer l'utilisateur (destinataire) correspondant à l'ID
        User receiver = dbUserRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Retourner le prénom et le nom du destinataire
        return receiver.getFirstname() + " " + receiver.getLastname();
    }
}