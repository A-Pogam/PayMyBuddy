package org.PayMyBuddy.service.contracts;

import org.PayMyBuddy.model.Contact;

import java.util.List;
import java.util.Optional;

public interface IContactService {
    Contact createConnectionBetweenTwoUsers(String initializerEmail, String contactEmail);

    List<Contact> getUserConnections(String userEmail);

    Optional<Contact> getConnectionById(Long id);
}