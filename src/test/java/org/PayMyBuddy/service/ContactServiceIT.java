package org.PayMyBuddy.service;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ContactRepository;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ContactServiceIT {

    @Autowired
    private ContactService contactService;

    @MockBean
    private IUserService userService;

    @MockBean
    private ContactRepository contactRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");
    }

    @Test
    void testCreateConnectionBetweenTwoUsers_ValidEmails() {
        // Given
        when(userService.findByEmail("user1@example.com")).thenReturn(java.util.Optional.of(user1));
        when(userService.findByEmail("user2@example.com")).thenReturn(java.util.Optional.of(user2));
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
            Contact contact = invocation.getArgument(0);
            return contact;
        });

        // When
        Contact createdConnection = contactService.createConnectionBetweenTwoUsers("user1@example.com", "user2@example.com");

        // Then
        assertNotNull(createdConnection.getFirstUser());
        assertNotNull(createdConnection.getSecondUser());
        assertEquals(user1.getId(), createdConnection.getFirstUser().getId());
        assertEquals(user2.getId(), createdConnection.getSecondUser().getId());
        verify(userService, times(1)).findByEmail("user1@example.com");
        verify(userService, times(1)).findByEmail("user2@example.com");
        verify(contactRepository, times(1)).save(any(Contact.class));
    }


    @Test
    void testCreateConnectionBetweenTwoUsers_UserNotFound() {
        // Given
        when(userService.findByEmail("user1@example.com")).thenReturn(java.util.Optional.empty());

        // When, Then
        assertThrows(UsernameNotFoundException.class, () -> contactService.createConnectionBetweenTwoUsers("user1@example.com", "user2@example.com"));
        verify(userService, times(1)).findByEmail("user1@example.com");
        verify(userService, never()).findByEmail("user2@example.com");
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void testGetUserConnections() {
        // Given
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setFirstUser(user1);
        contact1.setSecondUser(user2);
        contacts.add(contact1);

        when(userService.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
        when(contactRepository.getContactsByUser(1)).thenReturn(contacts);

        // When
        List<User> userConnections = contactService.getUserConnections("user1@example.com");

        // Then
        assertEquals(1, userConnections.size());
        assertEquals(user2, userConnections.get(0));
        verify(userService, times(1)).findByEmail("user1@example.com");
        verify(contactRepository, times(1)).getContactsByUser(1);
    }

}
