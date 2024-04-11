package org.PayMyBuddy.repository;

import org.PayMyBuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserEmail(String userEmail);
    Contact findByUserEmailAndContactEmail(String userEmail, String contactEmail);


}
