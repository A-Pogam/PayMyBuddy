package org.PayMyBuddy.service;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.TransactionRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceIT {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private IUserService userService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private IContactService contactService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setBalance(new BigDecimal("1000"));
    }

    @Test
    void testTransferMoney_InsufficientFunds() {
        // Given
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");
        sender.setBalance(new BigDecimal("1000"));

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        BigDecimal amount = new BigDecimal("2000");
        String description = "Test transfer";
        Model model = mock(Model.class);

        // Configure the UserService to return the sender user when findById is called with sender ID
        when(userService.findById(1)).thenReturn(java.util.Optional.of(sender));

        // When, Then
        assertThrows(RuntimeException.class, () -> transactionService.transferMoney(1, 2, description, amount, model));
        verify(userService, times(1)).findById(1);
        verify(userService, never()).updateUser(any());
        verify(transactionRepository, never()).save(any());
        verify(model, never()).addAttribute(anyString(), any());
    }


}
