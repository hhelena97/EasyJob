package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

import static de.hbrs.easyjob.security.SecurityConfig.getPasswordEncoder;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final PersonRepository repository;

    public AdminController(PersonRepository repository) {
        this.repository = repository;
    }

    /**
     * Legt einen Admin an.
     *
     * @param admin Admin, der angelegt werden soll
     * @return true, wenn Person angelegt wurde & false, wenn nicht
     */
    public boolean createAdmin(Admin admin) {
        admin.setAktiv(true);
        // Prüfe Email und Passwort
        boolean ok = ValidationController.isValidEmail(admin.getEmail()) &&
                ValidationController.isValidPassword(admin.getPasswort());
        if (ok) {
            // Passwort als Argon2 Hash speichern
            Argon2PasswordEncoder encoder = getPasswordEncoder();
            admin.setPasswort(encoder.encode(admin.getPasswort()));

            //admin speichern:
            repository.save(admin);
            //Test ob der Admin in der Datenbank ist
            if (repository.findByEmail(admin.getEmail())== null){
                return false;
            } else {
                ok = repository.findByEmail(admin.getEmail()).getAktiv();
            }
        }
        return ok;
    }


    /** Deaktiviert einen Admin
     *
     * @param admin
     * @return true, wenn Admin in Datenbank gefunden
     */
    boolean deleteAdmin(Admin admin) {

        if (repository.findByEmail(admin.getEmail()) == null){
            return false;
        }
        boolean inDatenbank = repository.findByEmail(admin.getEmail()).getAktiv();

        admin.setAktiv(false);
        repository.save(admin);
        return inDatenbank;
    }

    /**
     * Gibt einem Admin ein neues Passwort
     *
     * @param admin Admin, dessen Passwort verändert wird
     * @return true, wenn Admin in Datenbank gefunden & false, wenn nicht
     */
    boolean newPasswordAdmin(Admin admin, String neuesPasswort) {

        //Prüfe, ob Admin in der Datenbank ist
        if (admin.getEmail() == null) {
            return false;
        }
        if(repository.findByEmail(admin.getEmail()) == null){
            return false;
        }

        boolean inDatenbank = repository.findByEmail(admin.getEmail()).getAktiv();

        // Prüfe Passwort
        boolean ok = ValidationController.isValidPassword(neuesPasswort);
        if (inDatenbank){
            if (ok) {
                // Passwort als Argon2 Hash speichern
                Argon2PasswordEncoder encoder = getPasswordEncoder();
                admin.setPasswort(encoder.encode(neuesPasswort));

                repository.save(admin);
            }
        }
        return inDatenbank && ok;
    }


    /** Passwort ändern für Personen (ohne altes Passwort)
     * @param email E-Mail der Person
     * @param newPassword neues Passwort
     * @return true, wenn das Passwort geändert wurde, sonst false
     */
    public boolean changePassword(String email, String newPassword) {
        Person person = repository.findByEmail(email);
        if (person == null || !ValidationController.isValidPassword(newPassword)) {
            return false;
        }
        person.setPasswort(getPasswordEncoder().encode(newPassword));
        repository.save(person);
        return true;
    }

    /**
     * Finde alle Admins
     */
    public List<Admin> getAllAdmins(){
        return repository.findAllAdmins();
    }



}
