package org.PayMyBuddy.repository.contracts;

import org.PayMyBuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserEmail(String userEmail);
    Contact findByUserEmailAndContactEmail(String userEmail, String contactEmail);


}