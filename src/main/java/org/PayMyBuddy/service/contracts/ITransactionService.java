package org.PayMyBuddy.service.contracts;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.model.Transaction;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {

    void transferMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount, Model model);

    List<Transaction> getUserTransactions(Integer userId);

    List<Contact> getUserConnections(String userEmail);

    User getCurrentUser();

    Integer getCurrentUserId();



}