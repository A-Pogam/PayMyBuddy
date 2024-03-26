package com.paymybuddy.dataLayer;

import model.DBUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.DBUserRepository;
import service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthentificationServiceTest {

    @Mock
    private DBUserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSuccessfulAuthentication() {
        String email = "test@example.com";
        String password = "password123";
        DBUser user = new DBUser();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(user);

        assertTrue(authenticationService.authenticate(email, password));
    }
}