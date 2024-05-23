package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHome() {
        // Given
        String userEmail = "user@example.com";
        User user = new User();  // Assurez-vous que le constructeur et les setters de User sont accessibles
        user.setEmail(userEmail);

        // Simuler l'authentification
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        // Simuler le service utilisateur
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // When
        String actualView = homeController.home(model);

        // Then
        assertEquals("home", actualView);
        verify(userService, times(1)).findByEmail(userEmail);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    void testHomeUserNotFound() {
        // Given
        String userEmail = "user@example.com";

        // Simuler l'authentification
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        // Simuler le service utilisateur
        when(userService.findByEmail(userEmail)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> homeController.home(model));

        // Then
        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).findByEmail(userEmail);
        verify(model, never()).addAttribute(anyString(), any());
    }
}
