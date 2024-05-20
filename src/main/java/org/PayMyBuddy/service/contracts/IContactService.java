package org.PayMyBuddy.service.contracts;

import java.util.List;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;

public interface IContactService {

    Contact createConnectionBetweenTwoUsers(String initializerEmail, String contactEmail);
    List<User> getUserConnections(String userEmail);
}
