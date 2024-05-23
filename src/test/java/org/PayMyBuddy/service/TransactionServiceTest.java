package org.PayMyBuddy.service;

import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ITransactionRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private IUserService userService;

    @MockBean
    private ITransactionRepository transactionRepository;

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
    void testGetCurrentUser() {
        // Given
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setBalance(new BigDecimal("1000"));

        // Configure the SecurityContext with a mock Authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Configure the UserService to return the user when findByEmail is called
        when(userService.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));

        // When
        User currentUser = transactionService.getCurrentUser();

        // Then
        assertNotNull(currentUser);
        assertEquals(user.getId(), currentUser.getId());
        assertEquals(user.getEmail(), currentUser.getEmail());
        assertEquals(user.getBalance(), currentUser.getBalance());
    }


    @Test
    void testGetCurrentUserId() {
        // Given
        String userEmail = "user@example.com";
        when(userService.findByEmail(userEmail)).thenReturn(java.util.Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(null);

        // When
        Integer currentUserId = transactionService.getCurrentUserId();

        // Then
        assertNotNull(currentUserId);
        assertEquals(user.getId(), currentUserId);
    }

    @Test
    void testTransferMoney_InsufficientFunds() {
        // Given
        BigDecimal amount = new BigDecimal("2000");
        String description = "Test transfer";
        Model model = mock(Model.class);

        // When, Then
        assertThrows(RuntimeException.class, () -> transactionService.transferMoney(1, 2, description, amount, model));
        verify(userService, never()).findById(anyInt());
        verify(userService, never()).updateUser(any());
        verify(transactionRepository, never()).save(any());
        verify(model, never()).addAttribute(anyString(), any());
    }

}
