package com.paymybuddy.dataLayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.paymybuddy.dataLayer.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Integer> {
    public DBUser findByEmail(String email);
}


