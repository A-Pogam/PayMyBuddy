package org.PayMyBuddy.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void testRegisterNewUser() {
        User user = new User();
        user.setEmail("testX@example.com");
        user.setPassword("password123");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setRole("USER");

        userService.registerNewUser(user);

        Optional<User> foundUser = userService.findByEmail("testX@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("testX@example.com", foundUser.get().getEmail());
        assertTrue(passwordEncoder.matches("password123", foundUser.get().getPassword()));
        assertEquals("USER", foundUser.get().getRole());
        assertEquals(BigDecimal.ZERO, foundUser.get().getBalance());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setEmail("unique_test2@example.com");
        user.setPassword("password123");
        user.setFirstname("Jane");
        user.setLastname("Doe");
        user.setRole("USER");
        userRepository.save(user);

        Optional<User> foundUser = userService.findById(user.getId().intValue());
        assertTrue(foundUser.isPresent());
        assertEquals("unique_test2@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("test3@example.com");
        user.setPassword("password123");
        user.setFirstname("Jim");
        user.setLastname("Beam");
        user.setRole("USER");
        userRepository.save(user);

        user.setBalance(BigDecimal.valueOf(100));
        userService.updateUser(user);

        Optional<User> updatedUser = userService.findByEmail("test3@example.com");
        assertTrue(updatedUser.isPresent());
        assertEquals(BigDecimal.valueOf(100), updatedUser.get().getBalance());
    }

    @Test
    public void testExistsByEmail() {
        User user = new User();
        user.setEmail("test4@example.com");
        user.setPassword("password123");
        user.setFirstname("Jack");
        user.setLastname("Daniels");
        user.setRole("USER");
        userRepository.save(user);

        assertTrue(userService.existsByEmail("test4@example.com"));
        assertFalse(userService.existsByEmail("nonexistent@example.com"));
    }
}
