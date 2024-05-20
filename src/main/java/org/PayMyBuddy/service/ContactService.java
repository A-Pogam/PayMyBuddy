package org.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
        Integer userId = iUserService.findByEmail(userEmail).get().getId();
        List<User> myContacts = new ArrayList<>();
        TreeMap<String, Integer> contacts = new TreeMap<>();

        for(Contact contact : iContactRepository.getContactsByUser(userId)) {
            if (contact.getId().getFirstUser().getId() != userId) {
                String contactName = contact.getId().getFirstUser().getFirstname() + contact.getId().getFirstUser().getLastname();
                contacts.put(contactName, contact.getId().getFirstUser().getId());
            } else {
                String contactName = contact.getId().getSecondUser().getFirstname() + contact.getId().getSecondUser().getLastname();
                contacts.put(contactName, contact.getId().getSecondUser().getId());
            }
        }

        Set<String> keys = contacts.keySet();

        for (String key : keys) {
            myContacts.add(iUserService.findById(contacts.get(key)).get());
        }

        return myContacts;
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }
}