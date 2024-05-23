package org.PayMyBuddy.service;

import java.util.Collections;
import java.util.Optional;

import org.PayMyBuddy.exception.PasswordNotFoundException;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository dbUserRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public UserDetailsServiceImpl(UserRepository dbUserRepository, ApplicationContext applicationContext) {
        this.dbUserRepository = dbUserRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

        Optional<User> user = dbUserRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email " + username + " does not match any user.");
        }
        // Check if the password is null or empty
        if (user.get().getPassword() == null || user.get().getPassword().isEmpty()) {
            throw new PasswordNotFoundException("Wrong password");
        }

        String encodedPassword = passwordEncoder.encode(user.get().getPassword());
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), encodedPassword, true, true,
                true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}