package org.PayMyBuddy.repository.contracts;

import java.util.List;

import org.PayMyBuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IContactRepository extends JpaRepository<Contact, Long> {
    //List<Contact> findByUserEmail(String userEmail);

    @Query(value = "SELECT * FROM contact WHERE contact_first_user_id = ?1 OR contact_second_user_id = ?1", nativeQuery = true)
    public List<Contact> getContactsByUser(int userId);
}