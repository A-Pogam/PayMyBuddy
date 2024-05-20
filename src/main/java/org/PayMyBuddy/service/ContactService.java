package org.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.ContactKey;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IContactRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ContactService implements IContactService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IContactRepository iContactRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Contact createConnectionBetweenTwoUsers(String initializerEmail, String contactEmail) {
        if (!isValidEmail(initializerEmail) || !isValidEmail(contactEmail)) {
            throw new IllegalArgumentException("Les emails fournis sont invalides.");
        }

        User initializer = iUserService.findByEmail(initializerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur initiateur n'a pas été trouvé."));

        User contact = iUserService.findByEmail(contactEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur de contact n'a pas été trouvé."));

        ContactKey contactKey = new ContactKey();
        contactKey.setFirstUser(initializer);
        contactKey.setSecondUser(contact);

        Contact newConnection = new Contact();
        newConnection.setId(contactKey);

        return iContactRepository.save(newConnection);
    }

    @Override
    public List<User> getUserConnections(String userEmail) {
        User currentUser = iUserService.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Integer userId = currentUser.getId();
        List<User> myContacts = new ArrayList<>();

        for (Contact contact : iContactRepository.getContactsByUser(userId)) {
            if (contact.getId().getFirstUser().getId().equals(userId)) {
                myContacts.add(contact.getId().getSecondUser());
            } else {
                myContacts.add(contact.getId().getFirstUser());
            }
        }

        return myContacts;
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.contains("@");
    }
}
