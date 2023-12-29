package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.hbrs.easyjob.security.SecurityConfig.getPasswordEncoder;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * Legt einen Admin an.
     *
     * @param admin Admin, der angelegt werden soll
     * @param adminRepository das PersonRepository zum Speichern des Admins
     * @return true, wenn Person angelegt wurde & false, wenn nicht
     */
    boolean createAdmin(Admin admin, PersonRepository adminRepository) {
        admin.setAktiv(true);
        // Prüfe Email und Passwort
        boolean isValidPerson = ValidationController.isValidEmail(admin.getEmail()) &&
                ValidationController.isValidPassword(admin.getPasswort());
        if (isValidPerson) {
            // Passwort als Argon2 Hash speichern
            Argon2PasswordEncoder encoder = getPasswordEncoder();
            admin.setPasswort(encoder.encode(admin.getPasswort()));

            // TODO: Prüfen ob Person in Datenbank gespeichert wurde
            adminRepository.save(admin);
        }
        return isValidPerson;
    }

    /** aktualisiert einen Admin
     */
    @PostMapping
    public ResponseEntity<Admin> createOrUpdateAdmin(@RequestBody Admin admin) {
        //Account aktivieren
        admin.setAktiv(true);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    /** Deaktiviert einen Admin
     *
     * @param admin
     * @param adminRepository
     * @return true, wenn Admin in Datenbank gefunden
     */
    boolean deleteAdmin(Admin admin, PersonRepository adminRepository) {

        boolean inDatenbank = true;
        // TODO: Prüfe ob Admin in Datenbank
        //inDatenbank = ...

        admin.setAktiv(false);
        adminRepository.save(admin);
        return inDatenbank;
    }

    /**
     * Gibt einem Admin ein neues Passwort
     *
     * @param admin Admin, der angelegt werden soll
     * @param adminRepository das PersonRepository zum Speichern des Admins
     * @return true, wenn Admin in Datenbank gefunden & false, wenn nicht
     */
    boolean newPasswordAdmin(Admin admin, String neuesPasswort, PersonRepository adminRepository) {
        boolean inDatenbank = true;
        // TODO: Prüfe ob Admin in Datenbank
        //inDatenbank = ...

        // Prüfe Passwort
        boolean isValidPasswort = ValidationController.isValidPassword(neuesPasswort);
        if (isValidPasswort) {
            // Passwort als Argon2 Hash speichern
            Argon2PasswordEncoder encoder = getPasswordEncoder();
            admin.setPasswort(encoder.encode(neuesPasswort));

            adminRepository.save(admin);
        }
        return inDatenbank;
    }
}
