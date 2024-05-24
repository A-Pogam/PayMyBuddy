package org.PayMyBuddy.service;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ContactRepository;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.IUserService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ContactServiceIT {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private IUserService userService;

    @Autowired
    private IContactService contactService;

    @Test
    void testCreateConnectionBetweenTwoUsers_ValidEmails() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setRole("USER");
        user1.setBalance(BigDecimal.ZERO);
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password456");
        user2.setFirstname("Jane");
        user2.setLastname("Smith");
        user2.setRole("USER");
        user2.setBalance(BigDecimal.ZERO);
        user2 = userRepository.save(user2);

        when(userService.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
        when(userService.findByEmail("user2@example.com")).thenReturn(Optional.of(user2));

        // When
        Contact createdConnection = contactService.createConnectionBetweenTwoUsers("user1@example.com", "user2@example.com");

        // Then
        assertThat(createdConnection.getFirstUser()).isEqualTo(user1);
        assertThat(createdConnection.getSecondUser()).isEqualTo(user2);
        verify(userService, times(1)).findByEmail("user1@example.com");
        verify(userService, times(1)).findByEmail("user2@example.com");
    }

    @Test
    void testCreateConnectionBetweenTwoUsers_UserNotFound() {
        // Given
        when(userService.findByEmail("user1@example.com")).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UsernameNotFoundException.class, () -> contactService.createConnectionBetweenTwoUsers("user1@example.com", "user2@example.com"));
        verify(userService, times(1)).findByEmail("user1@example.com");
        verify(userService, never()).findByEmail("user2@example.com");
    }

    @Test
    void testGetUserConnections() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setRole("USER");
        user1.setBalance(BigDecimal.ZERO);
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password456");
        user2.setFirstname("Jane");
        user2.setLastname("Smith");
        user2.setRole("USER");
        user2.setBalance(BigDecimal.ZERO);
        user2 = userRepository.save(user2);

        Contact contact = new Contact();
        contact.setFirstUser(user1);
        contact.setSecondUser(user2);
        contactRepository.save(contact);

        when(userService.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));

        // When
        List<User> userConnections = contactService.getUserConnections("user1@example.com");

        // Then
        assertThat(userConnections).containsExactly(user2);
        verify(userService, times(1)).findByEmail("user1@example.com");
    }
}