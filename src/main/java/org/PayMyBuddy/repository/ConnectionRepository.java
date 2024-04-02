package org.PayMyBuddy.repository;

import org.PayMyBuddy.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    /*List<Connection> findByInitializerOrReceiver(User initializer, User receiver);

    Optional<Connection> findById(Integer id); in case where i have to transfer to someone for real*/
}
