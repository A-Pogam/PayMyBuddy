package org.PayMyBuddy.service;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.repository.DBUserRepository;
import org.PayMyBuddy.repository.TransactionRepository;
import org.PayMyBuddy.service.contracts.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService implements ITransferService {

    @Autowired
    private DBUserRepository dbUserRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ConnectionService connectionService;


    public void transferMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount, Model model) {
        DBUser sender = dbUserRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Sender not found"));
        DBUser receiver = dbUserRepository.findByEmail(receiverEmail).orElseThrow(() -> new RuntimeException("Receiver not found"));

        BigDecimal senderBalance = sender.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal finalAmount = calculateFinalAmount(amount);
        updateSenderBalance(sender, finalAmount.negate());
        updateReceiverBalance(receiver, finalAmount);

        saveTransaction(sender.getId(), receiver.getId(), description, finalAmount);
        model.addAttribute("senderFirstName", sender.getFirstname());
        model.addAttribute("senderLastName", sender.getLastname());
    }

    private BigDecimal calculateFinalAmount(BigDecimal amount) {
        BigDecimal feePercentage = BigDecimal.valueOf(0.5);
        BigDecimal feeAmount = amount.multiply(feePercentage.divide(BigDecimal.valueOf(100)));
        return amount.subtract(feeAmount);
    }

    private void updateSenderBalance(DBUser sender, BigDecimal amount) {
        BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
        sender.setBalance(newSenderBalance);
        dbUserRepository.save(sender);
    }

    private void updateReceiverBalance(DBUser receiver, BigDecimal amount) {
        BigDecimal newReceiverBalance = receiver.getBalance().add(amount);
        receiver.setBalance(newReceiverBalance);
        dbUserRepository.save(receiver);
    }

    private void saveTransaction(Integer senderId, Integer receiverId, String description, BigDecimal amount) {
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

    public List<Contact> getUserConnections(String userEmail) {
        return connectionService.getUserConnections(userEmail);
    }

    public DBUser getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return dbUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }


    public String getReceiverName(Integer receiverId) {
        // Récupérer l'utilisateur (destinataire) correspondant à l'ID
        DBUser receiver = dbUserRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Retourner le prénom et le nom du destinataire
        return receiver.getFirstname() + " " + receiver.getLastname();
    }


}


