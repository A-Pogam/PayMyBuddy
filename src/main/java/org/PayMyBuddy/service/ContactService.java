package org.PayMyBuddy.service;

import jakarta.transaction.Transactional;
import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IContactRepository;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService implements IContactService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IContactRepository connectionRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Contact createConnectionBetweenTwoUsers(String initializerEmail, String contactEmail) {
        // Vérifier la validité des emails
        if (!isValidEmail(initializerEmail) || !isValidEmail(contactEmail)) {
            throw new IllegalArgumentException("Les emails fournis sont invalides.");
        }

        // Rechercher les utilisateurs correspondants dans la base de données
        User initializer = userRepository.findByEmail(initializerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur initiateur n'a pas été trouvé."));

        User contact = userRepository.findByEmail(contactEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur de contact n'a pas été trouvé."));

        // Récupérer le prénom et le nom des utilisateurs
        String contactFirstName = contact.getFirstname();
        String contactLastName = contact.getLastname();

        // Créer une nouvelle connexion entre les deux utilisateurs
        Contact newConnection = new Contact();
        newConnection.setUserEmail(initializer.getEmail());
        newConnection.setContactEmail(contact.getEmail());
        newConnection.setFirstname(contactFirstName);
        newConnection.setLastname(contactLastName);

        return connectionRepository.save(newConnection);
    }

    public List<Contact> getUserConnections(String userEmail) {
        return connectionRepository.findByUserEmail(userEmail);
    }

    public Optional<Contact> getConnectionById(Long id) {
        return connectionRepository.findById(id);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }

}