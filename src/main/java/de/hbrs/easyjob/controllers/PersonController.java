package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.stereotype.Controller;

import static de.hbrs.easyjob.security.SecurityConfig.getPasswordEncoder;

@Controller
public class PersonController {
    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /** Ändert das Passwort einer Person, wenn das alte Passwort bekannt ist
     * @param email E-Mail der Person
     * @param oldPassword altes Passwort
     * @param newPassword neues Passwort
     * @return true, wenn das Passwort geändert wurde, sonst false
     */
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Person person = personRepository.findByEmail(email);
        if (
                person == null ||
                !ValidationController.isValidPassword(newPassword) ||
                !getPasswordEncoder().matches(oldPassword, person.getPasswort())
        ) {
            return false;
        }

        person.setPasswort(getPasswordEncoder().encode(newPassword));
        personRepository.save(person);
        return true;
    }

    /** Ändert das Passwort einer Person, ohne das alte Passwort zu kennen
     * @param email E-Mail der Person
     * @param newPassword neues Passwort
     * @return true, wenn das Passwort geändert wurde, sonst false
     */
    public boolean changePassword(String email, String newPassword) {
        Person person = personRepository.findByEmail(email);
        if (
                person == null ||
                !ValidationController.isValidPassword(newPassword)
        ) {
            return false;
        }

        person.setPasswort(getPasswordEncoder().encode(newPassword));
        personRepository.save(person);
        return true;
    }

    /** Ändert die E-Mail einer Person
     * @param oldEmail alte E-Mail
     * @param newEmail neue E-Mail
     * @return true, wenn die E-Mail geändert wurde, sonst false
     */
    public boolean changeEmail(String oldEmail, String newEmail) {
        Person person = personRepository.findByEmail(oldEmail);
        if (person == null || !ValidationController.isValidEmail(newEmail)) {
            return false;
        }
        person.setEmail(newEmail);
        personRepository.save(person);
        return true;
    }
}
