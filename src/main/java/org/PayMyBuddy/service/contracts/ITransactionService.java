package org.PayMyBuddy.service.contracts;

import java.math.BigDecimal;
import java.util.List;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.springframework.ui.Model;

public interface ITransactionService {

    void transferMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount, Model model);

    List<Transaction> getUserTransactions(Integer userId);
    List<User> getUserConnections(String userEmail);

    User getCurrentUser();
    Integer getCurrentUserId();
}