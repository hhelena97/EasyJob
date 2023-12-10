package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

//@Component
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private Person person = null;

    private final PersonRepository repository;

    @Autowired
    public LoginController(PersonRepository personRepository){
        this.repository = personRepository;
    }


    /**
     * Authentifiziert einen Benutzer über E-Mail und Passwort.
     * Die Methode überprüft die Datenbankverbindung, sucht nach der Person mit der angegebenen E-Mail
     * und vergleicht dann das eingegebene Passwort mit dem in der Datenbank gespeicherten Passwort.
     *
     * @param email    Die E-Mail des Benutzers und damit auch sein/ihr Anmeldename.
     * @param password Das Passwort des Benutzers.
     * @return false, wenn die Authentifizierung fehlschlägt (das soll dann im View ein Popup auslösen)
     * Damit man sieht, was die Methode gerade macht, gibt es noch die vielen Ausgaben in der Konsole. Die lösche ich später, wieder raus.
     */
    public boolean authenticate(String email, String password) {

        try {
            this.person = repository.findByEmail(email);
            UserDetails user = loadUserByUsername(email);
            //Verknüpft die Person mit der Person der Datenbank, die zu dieser E-Mail-Adresse gespeichert ist
            System.out.println("Datenbankverbindung erfolgreich.");
        } catch ( org.springframework.dao.DataAccessResourceFailureException e ) {
            System.out.println("Problem mit der Datenbankverbindung.");
            return false;
        }
        if (this.person == null){
            System.out.println("Person kann nicht gefunden werden.");
            return false;
        }

        if (password == null){
            System.out.println("Passwort fehlt.");
            return false;
        }
        String eingabePW = password;
        //Wenn das Passwort in der Registrierung gehasht wird,
        //TODO: das eingegebene Passwort ebenfalls hashen
        String dbPW = this.person.getPasswort();

        if (eingabePW.equals(dbPW)){
            //prüft, ob das Passwort zum gespeicherten Passwort passt
            System.out.println("Passwort stimmt.");
            System.out.println(this.person.toString());
            return true;
        }
        //Sonstiges Problem
        return false;
    }

    //Zur Zurückgabe der gefundenen Person an die View
    public Person getPerson(){
        return this.person;
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = repository.findByEmail(username);

        if (person == null) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }

        return User.withUsername(person.getEmail())
                .password(person.getPasswort()) // hier sollte die verschlüsselte Form stehen, wenn die Passwörter verschlüsselt sind
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}
