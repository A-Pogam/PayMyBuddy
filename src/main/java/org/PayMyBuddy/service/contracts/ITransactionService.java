package org.PayMyBuddy.service.contracts;

import java.math.BigDecimal;
import java.util.List;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.springframework.ui.Model;

public interface ITransactionService {

    List<Transaction> getUserTransactions(User currentUser);
    List<User> getUserConnections(String userEmail);

    User getCurrentUser();
    Integer getCurrentUserId();

    void transferMoney(int transactionSender, int transactionReceiver, String description, BigDecimal amount, Model model);
}