package org.PayMyBuddy.service;

import java.math.BigDecimal;
import java.util.List;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ITransactionRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITransactionRepository transactionRepository;

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

    @Override
    public void transferMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount, Model model) {
        User sender = iUserService.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = iUserService.findByEmail(receiverEmail).orElseThrow(() -> new RuntimeException("Receiver not found"));

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
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }
}