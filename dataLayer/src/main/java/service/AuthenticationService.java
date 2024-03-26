package service;

import model.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DBUserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private DBUserRepository userRepository;

    public boolean authenticate(String email, String enteredPassword) {
        // Récupère l'utilisateur depuis la base de données en fonction de l'email
        DBUser user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // L'utilisateur n'existe pas
        }

        // Compare le mot de passe entré avec le mot de passe en clair stocké dans la base de données
        return enteredPassword.equals(user.getPassword());
    }

    @Autowired
    public AuthenticationService(DBUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
