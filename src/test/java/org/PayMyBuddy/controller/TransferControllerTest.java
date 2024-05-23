package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @Mock
    private ITransactionService iTransactionService;

    @Mock
    private Model model;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransfer() {
        // Given
        User currentUser = new User();
        currentUser.setEmail("user@example.com");

        List<User> contacts = List.of(new User(), new User());
        List<Transaction> transactions = List.of(new Transaction(), new Transaction());

        when(iTransactionService.getCurrentUser()).thenReturn(currentUser);
        when(iTransactionService.getUserConnections(currentUser.getEmail())).thenReturn(contacts);
        when(iTransactionService.getUserTransactions(currentUser)).thenReturn(transactions);

        // When
        String actualView = transferController.transfer(model);

        // Then
        assertEquals("transfer", actualView);
        verify(iTransactionService, times(1)).getCurrentUser();
        verify(iTransactionService, times(1)).getUserConnections(currentUser.getEmail());
        verify(iTransactionService, times(1)).getUserTransactions(currentUser);
        verify(model, times(1)).addAttribute("contacts", contacts);
        verify(model, times(1)).addAttribute("transactions", transactions);
    }

    @Test
    void testPerformTransfer() {
        // Given
        int transactionReceiver = 2;
        String description = "Test transfer";
        BigDecimal amount = BigDecimal.valueOf(100);
        int senderId = 1;

        when(iTransactionService.getCurrentUserId()).thenReturn(senderId);

        // When
        String actualView = transferController.performTransfer(transactionReceiver, description, amount, model);

        // Then
        assertEquals("redirect:/transfer?success=Transfer successful", actualView);
        verify(iTransactionService, times(1)).getCurrentUserId();
        verify(iTransactionService, times(1)).transferMoney(senderId, transactionReceiver, description, amount, model);
    }
}
