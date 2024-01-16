package de.hbrs.easyjob.services;

import com.vaadin.flow.component.notification.Notification;
import de.hbrs.easyjob.controllers.ValidationController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import static de.hbrs.easyjob.security.SecurityConfig.getPasswordEncoder;

@Service
public class PasswortService {

    private final PersonRepository repository;

    public PasswortService (PersonRepository personRepository){
        this.repository = personRepository;
    }

    /** Ändert das Passwort einer Person, wenn das alte Passwort bekannt ist
     * @param email E-Mail der Person
     * @param oldPassword altes Passwort
     * @param newPassword neues Passwort
     * @param repeatPassword das wiederholte neue Passwort
     * @return true, wenn das Passwort geändert wurde, sonst false
     */
    public boolean changePassword(String email, String oldPassword, String newPassword, String repeatPassword) {
        Person person = repository.findByEmail(email);
        //existiert die Person, ist das ihr Passwort, ist ihr Acount aktiv und nicht gesperrt
        if (person == null || !getPasswordEncoder().matches(oldPassword, person.getPasswort()) || ! person.getAktiv() /*|| person.isGesperrt()*/){
            Notification.show("Die Person konnte nicht gefunden werden oder das alte Passwort ist falsch.");
            return false;
        }
        // ist das neue Passwort valide
        if (!ValidationController.isValidPassword(newPassword)) {
            Notification.show("Das Passwort entspricht nicht den Anforderungen. \n" +
                    "Es muss mindestens 8 Zeichen lang sein,\n" +
                    "darf maximal 64 Zeichen lang sein\n" +
                    "muss Groß- und Kleinschreibung enthalten\n" +
                    "muss mindestens 1 Sonderzeichen enthalten");
            return false;
        }
        // ist das neue Passwort das gleiche, wie das wiederholte
        if (!newPassword.equals(repeatPassword)){
            Notification.show("Bitte wiederholen Sie das neue Passwort exakt.");
            return false;
        }
        person.setPasswort(getPasswordEncoder().encode(newPassword));
        repository.save(person);
        return true;
    }

    /** Ändert das Passwort einer Person, ohne das alte Passwort zu kennen
     * @param person die ein neues Passwort bekommen soll
     * @param newPassword neues Passwort
     * @param repeatPassword das wiederholte neue Passwort
     * @return true, wenn das Passwort geändert wurde, sonst false
     */
    public boolean newPassword(Person person, String newPassword, String repeatPassword) {

        //existiert die Person, ist ihr Acount aktiv und nicht gesperrt
        if (person == null || ! person.getAktiv() /*|| person.isGesperrt()*/){
            Notification.show("Die Person konnte nicht gefunden werden");
            return false;
        }
        // ist das neue Passwort valide
        if (!ValidationController.isValidPassword(newPassword)) {
            Notification.show("Das Passwort entspricht nicht den Anforderungen. \n" +
                    "Es muss mindestens 8 Zeichen lang sein,\n" +
                    "darf maximal 64 Zeichen lang sein\n" +
                    "muss Groß- und Kleinschreibung enthalten\n" +
                    "muss mindestens 1 Sonderzeichen enthalten");
            return false;
        }
        // ist das neue Passwort das gleiche, wie das wiederholte
        if (!newPassword.equals(repeatPassword)){
            Notification.show("Bitte wiederholen Sie das neue Passwort exakt.");
            return false;
        }

        person.setPasswort(getPasswordEncoder().encode(newPassword));
        repository.save(person);
        return true;
    }
}
