package org.PayMyBuddy.service;


import java.math.BigDecimal;
import java.util.Optional;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;



@SpringBootTest
@Transactional
public class UserServiceIT {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;




    @Test
    void testFindById() {
        // Given
        User user = new User();
        user.setEmail("testX@example.com");
        user.setPassword("password123");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);
        user = userRepository.save(user);

        // When
        Optional<User> foundUser = userService.findById(user.getId().intValue());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void testRegisterNewUser() {
        // Given
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password123");
        user.setFirstname("Jane");
        user.setLastname("Smith");
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        // When
        userService.registerNewUser(user);

        // Then
        Optional<User> foundUser = userRepository.findByEmail("newuser@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("newuser@example.com");
        assertThat(foundUser.get().getPassword()).isEqualTo("encodedPassword123");
        assertThat(foundUser.get().getRole()).isEqualTo("USER");
        assertThat(foundUser.get().getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }


    @Test
    void testFindByEmail() {
        // Given
        User user = new User();
        user.setEmail("test4@example.com");
        user.setPassword("password123");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userService.findByEmail("test4@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test4@example.com");
    }

    @Test
    void testRegisterUser() {
        // Given
        User user = new User();
        user.setEmail("unique_registeruser@example.com");
        user.setPassword("password123");
        user.setFirstname("Steve");
        user.setLastname("Rogers");
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        // When
        boolean isRegistered = userService.registerUser(user);

        // Then
        assertThat(isRegistered).isTrue();
        Optional<User> foundUser = userRepository.findByEmail("unique_registeruser@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("unique_registeruser@example.com");
        assertThat(foundUser.get().getPassword()).isEqualTo("encodedPassword123");
    }

    @Test
    void testRegisterUser_EmailExists() {
        // Given
        User existingUser = new User();
        existingUser.setEmail("unique_existinguser@example.com");
        existingUser.setPassword("password123");
        existingUser.setFirstname("Bruce");
        existingUser.setLastname("Wayne");
        existingUser.setRole("USER");
        existingUser.setBalance(BigDecimal.ZERO);
        userRepository.save(existingUser);

        User newUser = new User();
        newUser.setEmail("unique_existinguser@example.com");
        newUser.setPassword("newpassword123");
        newUser.setFirstname("Clark");
        newUser.setLastname("Kent");
        newUser.setRole("USER");
        newUser.setBalance(BigDecimal.ZERO);

        // When
        boolean isRegistered = userService.registerUser(newUser);

        // Then
        assertThat(isRegistered).isFalse();
    }
}
