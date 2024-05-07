package org.PayMyBuddy.service.contracts;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IDBUserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
