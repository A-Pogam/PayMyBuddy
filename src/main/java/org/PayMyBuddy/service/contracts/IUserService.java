package org.PayMyBuddy.service.contracts;

import java.util.Optional;
import org.PayMyBuddy.model.User;

public interface IUserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);

    void updateUser(User user);

}
