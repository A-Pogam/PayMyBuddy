package org.PayMyBuddy.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;




    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById((long) id);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean registerUser(User user) {
        // Vérifiez si l'e-mail est déjà utilisé
        if (existsByEmail(user.getEmail())) {
            return false;
        }

        // Enregistrer le nouvel utilisateur avec des valeurs par défaut
        registerNewUser(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
