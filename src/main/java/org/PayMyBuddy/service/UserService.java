package org.PayMyBuddy.service;

import java.util.Optional;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return iUserRepository.findById(id);
    }

    @Override
    public void updateUser(User user) {
        iUserRepository.save(user);
    }
}
