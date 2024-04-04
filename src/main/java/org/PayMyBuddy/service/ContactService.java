package org.PayMyBuddy.service;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void addContact(String userEmail, String contactEmail) {
        Contact existingContact = contactRepository.findByUserEmailAndContactEmail(userEmail, contactEmail);
        if (existingContact != null) {
            throw new IllegalArgumentException("Ce contact existe déjà.");
        } else {
            Contact newContact = new Contact();
            newContact.setUserEmail(userEmail);
            newContact.setContactEmail(contactEmail);
            contactRepository.save(newContact);
        }
    }
}
