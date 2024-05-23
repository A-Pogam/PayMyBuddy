package org.PayMyBuddy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterUser_NewUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // When
        boolean registered = userService.registerUser(user);

        // Then
        assertTrue(registered);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("USER", user.getRole());
        assertEquals(BigDecimal.ZERO, user.getBalance());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Given
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // When
        User user = new User();
        user.setEmail("existing@example.com");
        boolean registered = userService.registerUser(user);

        // Then
        assertFalse(registered);
        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository);
    }
}
