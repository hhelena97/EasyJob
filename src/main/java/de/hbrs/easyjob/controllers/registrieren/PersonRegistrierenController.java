package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.controllers.ValidationController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import static de.hbrs.easyjob.security.SecurityConfig.getPasswordEncoder;

/**
 * Controller für die Registrierung.
 * Erstellt Person und speichert diese in der Datenbank.
 * Prüft, ob Email, Passwort, Name und Telefonnummer gültig sind.
 */
@RestController
public abstract class PersonRegistrierenController implements ValidationController {
    // Repositories
    private final PersonRepository personRepository;

    /**
     * Konstruktor des Controllers für die Registrierung von Personen.
     * @param personRepository      Repository für Personen
     */
    public PersonRegistrierenController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Legt eine Person an.
     *
     * @param person Person, die angelegt werden soll
     * @param hasAcceptedAGB    Prüfung, ob AGB akzeptiert wurden
     * @return true, wenn Person angelegt wurde & false, wenn nicht
     */
    boolean createPerson(Person person, boolean hasAcceptedAGB) {
        // Prüfe Email und Passwort
        boolean isValidPerson = ValidationController.isValidEmail(person.getEmail()) &&
                ValidationController.isValidPassword(person.getPasswort()) &&
                hasAcceptedAGB &&
                ValidationController.isValidName(person.getVorname()) &&
                ValidationController.isValidName(person.getNachname());
        if (isValidPerson) {
            // Passwort als Argon2 Hash speichern
            Argon2PasswordEncoder encoder = getPasswordEncoder();
            person.setPasswort(encoder.encode(person.getPasswort()));

            // TODO: Prüfen ob Person in Datenbank gespeichert wurde
            personRepository.save(person);
        }
        return isValidPerson;
    }
}