package org.PayMyBuddy.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return iUserRepository.findById((long) id);
    }

    @Override
    public void updateUser(User user) {
        iUserRepository.save(user);
    }

    @Override
    public void registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setBalance(BigDecimal.ZERO);
        iUserRepository.save(user);
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
}
