package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        // Given
        String expectedView = "register";

        // When
        String actualView = registrationController.showRegistrationForm(model);

        // Then
        assertEquals(expectedView, actualView);
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testRegisterUser_WithValidationErrors() {
        // Given
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        // When
        String actualView = registrationController.registerUser(user, bindingResult);

        // Then
        assertEquals("register", actualView);
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Given
        User user = new User();
        user.setEmail("existing@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(false);

        // When
        String actualView = registrationController.registerUser(user, bindingResult);

        // Then
        assertEquals("redirect:/register?error=emailExists", actualView);
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_Success() {
        // Given
        User user = new User();
        user.setEmail("new@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(true);

        // When
        String actualView = registrationController.registerUser(user, bindingResult);

        // Then
        assertEquals("redirect:/home", actualView);
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).registerUser(user);
    }
}
