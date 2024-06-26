package org.PayMyBuddy.service.contracts;

import java.util.Optional;
import org.PayMyBuddy.model.User;

public interface IUserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    void updateUser(User user);
    void registerNewUser(User user);
    boolean existsByEmail(String email);
    boolean registerUser(User user);
    void save(User user); // Nouvelle méthode save
}
