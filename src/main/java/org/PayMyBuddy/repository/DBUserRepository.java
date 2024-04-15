package org.PayMyBuddy.repository;

import org.PayMyBuddy.model.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {
    Optional<DBUser> findByEmail(String email);
    Optional<DBUser> findById(Integer id);

}


