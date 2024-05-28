package org.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.ContactRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.PayMyBuddy.service.contracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ContactService implements IContactService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ContactRepository contactRepository;

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

        Contact newConnection = new Contact();
        newConnection.setFirstUser(initializer);
        newConnection.setSecondUser(contact);


        if (contactRepository.existsByFirstUserAndSecondUser(initializer, contact)) {
            System.out.println("Contact already exists between users.");
            return null;
        }

        try {
            return contactRepository.save(newConnection);
        } catch (DataIntegrityViolationException e) {
            // Log the exception if necessary
            System.out.println("Contact already exists: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> getUserConnections(String userEmail) {
        User currentUser = iUserService.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Integer userId = currentUser.getId();
        List<User> myContacts = new ArrayList<>();

        for (Contact contact : contactRepository.getContactsByUser(userId)) {
            if (contact.getFirstUser().getId().equals(userId)) {
                myContacts.add(contact.getSecondUser());
            } else {
                myContacts.add(contact.getFirstUser());
            }
        }

        return myContacts;
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.contains("@");
    }
}
