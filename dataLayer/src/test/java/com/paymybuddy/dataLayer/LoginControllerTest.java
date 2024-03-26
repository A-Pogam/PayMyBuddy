package com.paymybuddy.dataLayer;

import model.DBUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repository.DBUserRepository;
import service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    private AuthenticationService authenticationService;

    @Mock
    private DBUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService(userRepository);
    }

    @Test
    public void testSuccessfulLogin() {
        String email = "user@example.com";
        String password = "password123";
        DBUser user = new DBUser();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(user);

        assertTrue(authenticationService.authenticate(email, password));
    }

    @Test
    public void testInvalidEmail() {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertFalse(authenticationService.authenticate(email, password));
    }

    @Test
    public void testInvalidPassword() {
        String email = "user@example.com";
        String correctPassword = "password123";
        String incorrectPassword = "wrongpassword";
        DBUser user = new DBUser();
        user.setEmail(email);
        user.setPassword(correctPassword);

        when(userRepository.findByEmail(email)).thenReturn(user);

        assertFalse(authenticationService.authenticate(email, incorrectPassword));
    }
}