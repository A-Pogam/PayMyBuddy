package org.PayMyBuddy.service;

import java.util.Collections;
import java.util.Optional;

import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.repository.DBUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DBUserDetailsService implements UserDetailsService {

    private final DBUserRepository dbUserRepository;

    public DBUserDetailsService(DBUserRepository dbUserRepository) {
        this.dbUserRepository = dbUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<DBUser> user = dbUserRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email " + username + " does not match any user.");
        }
        // Check if the password is null or empty
        if (user.get().getPassword() == null || user.get().getPassword().isEmpty()) {
            throw new PasswordNotFoundException("Wrong password");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(), true, true,
                true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
