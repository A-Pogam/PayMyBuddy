package org.PayMyBuddy.controller;

import org.PayMyBuddy.service.contracts.IContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ConnectionControllerTest {

    @Mock
    private IContactService iContactService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ConnectionController connectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddConnection() {
        // Given
        String expectedView = "connection";

        // When
        String actualView = connectionController.addConnection();

        // Then
        assertEquals(expectedView, actualView);
    }

    @Test
    void testAddContact() {
        // Given
        String contactEmail = "test@example.com";
        String initializerEmail = "user@example.com";
        String expectedRedirectView = "redirect:/connection";

        // Simuler l'authentification
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(initializerEmail);
        SecurityContextHolder.setContext(securityContext);

        // When
        String actualRedirectView = connectionController.addContact(contactEmail, redirectAttributes);

        // Then
        assertEquals(expectedRedirectView, actualRedirectView);
        verify(iContactService, times(1)).createConnectionBetweenTwoUsers(initializerEmail, contactEmail);
        verify(redirectAttributes, times(1)).addFlashAttribute("successMessage", "Connection added successfully!");
    }
}
