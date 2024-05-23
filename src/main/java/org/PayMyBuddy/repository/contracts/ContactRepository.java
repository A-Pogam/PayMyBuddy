
package org.PayMyBuddy.repository.contracts;

import java.util.List;

import org.PayMyBuddy.constant.SqlQueries;
import org.PayMyBuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(value = SqlQueries.allContactsByUser, nativeQuery = true)
    public List<Contact> getContactsByUser(Integer userId);
}
