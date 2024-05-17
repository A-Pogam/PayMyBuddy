package org.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.model.ContactKey;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.repository.contracts.IContactRepository;
import org.PayMyBuddy.repository.contracts.IUserRepository;
import org.PayMyBuddy.service.contracts.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ContactService implements IContactService {

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IContactRepository iContactRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Contact createConnectionBetweenTwoUsers(String initializerEmail, String contactEmail) {
        // Vérifier la validité des emails
        if (!isValidEmail(initializerEmail) || !isValidEmail(contactEmail)) {
            throw new IllegalArgumentException("Les emails fournis sont invalides.");
        }

        // Rechercher les utilisateurs correspondants dans la base de données
        User initializer = iUserRepository.findByEmail(initializerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur initiateur n'a pas été trouvé."));

        User contact = iUserRepository.findByEmail(contactEmail)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur de contact n'a pas été trouvé."));

        ContactKey contactKey = new ContactKey();
        contactKey.setFirstUser(initializer);
        contactKey.setSecondUser(contact);

        // Créer une nouvelle connexion entre les deux utilisateurs
        Contact newConnection = new Contact();
        newConnection.setId(contactKey);

        return iContactRepository.save(newConnection);
    }

    public List<User> getUserConnections(String userEmail) {
        // On récupère l'id de l'utilisateur dont on passe l'email
        int userId = iUserRepository.findByEmail(userEmail).get().getId();

        // On crée la liste de contacts à retourner sous forme de liste d'utilisateurs
        // On créer une liste de contacts ordonnés.
        List<User> myContacts = new ArrayList<>();
        TreeMap<String, Integer> contacts = new TreeMap<>();

        // On parcourt la liste en appelant une requete JPQL : getContactsByUser(int
        // userId) qui récupère la liste des contacts dont l'un des membres possède l'id
        // passé en paramètre
        for(Contact contact : iContactRepository.getContactsByUser(userId)) {
            // On récupère les noms, prénoms et id des contacts de l'user ciblé d'un côté de la relation
            if (contact.getId().getFirstUser().getId() != userId) {
                String contactName = contact.getId().getFirstUser().getFirstname() + contact.getId().getFirstUser().getLastname();
                contacts.put(contactName, contact.getId().getFirstUser().getId());
            } else {
                // puis de l'autre côté
                String contactName = contact.getId().getSecondUser().getFirstname() + contact.getId().getSecondUser().getLastname();
                contacts.put(contactName, contact.getId().getSecondUser().getId());
            }
        }

        // On crée une collection d'id de user (qui sont les contacts de l'user cible) à
        // partir des valeurs de la treemap contacts. Implicitement, cette collection
        // est ordonnée par noms de contacts.
        Set<String> keys = contacts.keySet();

        // On parcourt la collection et on récupère les User dont les ids sont dans la
        // liste avant de les insérer dans la liste de contacts (User) à retourner.
        // Implicitement, ils sont ordonnés par nom.
        for (String key : keys) {
            myContacts.add(iUserRepository.findById(contacts.get(key)).get());
        }

        // On retourne la liste de contacts (User)
        return myContacts;
    }

    public Optional<Contact> getConnectionById(Long id) {
        return iContactRepository.findById(id);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }
}