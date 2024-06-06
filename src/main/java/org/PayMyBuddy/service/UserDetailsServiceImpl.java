package org.PayMyBuddy.service;

import java.util.Optional;

import org.PayMyBuddy.exception.PasswordNotFoundException;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository dbUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = dbUserRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email " + username + " does not match any user.");
        }

// Check if the password is null or empty
        if (user.get().getPassword() == null || user.get().getPassword().isEmpty()) {
            throw new PasswordNotFoundException("Wrong password");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.get().getPassword())
                .roles(user.get().getRole())
                .build();

        return userDetails;
    }
}