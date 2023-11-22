package de.hbrs.easyjob.control;

import de.hbrs.easyjob.dtos.impl.PersonDTOimpl;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Component
@RestController
@RequestMapping("/api/login")
public class LoginControl {

    private Person person = null;
    private Student student = null;
    private Unternehmensperson unternehmensperson = null;
    private PersonDTOimpl personDTO = null;

    private final PersonRepository repository;

    @Autowired
    public LoginControl(PersonRepository personRepository){
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
     * falls die Person authentifiziert werden kann, wird der Nutzer zur nächsten Seite weitergeleitet
     * (es wird ein True zurückgegeben, dass aber (hoffentlich) irrelevant ist).
     * Damit man auch ohne Weiterleitung sieht, was die Methode gerade macht, gibt es noch die vielen Ausgaben in der
     * Konsole. Die lösche ich später, wieder raus.
     */
    public boolean authenticate(String email, String password) {

        try {
            this.person = repository.findByEmail(email);
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
            //prüft ob das Passwort zum gespeicherten Passwort passt
            System.out.println("Passwort stimmt.");
            System.out.println(this.person.toString());
            this.personDTO = repository.findPersonByEmail(email);
            //erstellt ein DTO aus den Daten der Datenbank, die zu der E-Mail-Adresse gehören

            if (person instanceof Student){
                //wenn es ein Student ist, belege das stuendtDTO
                this.student = (Student) person;
                System.out.println("Es ist ein Student.");
                //TODO: weiter zur Studenten-Startseite
                return true;
            }

            if (person instanceof Unternehmensperson){
                //wenn es eine Unternehmensperson ist, belege das UnternehmenspersonDTO
                this.unternehmensperson = (Unternehmensperson) person;
                System.out.println("Es ist eine Unternehmensperson.");
                //TODO: weiter zur Unternehmer-Startseite
                return true;
            }
            //es ist eine Person, aber kein Student oder Unternehmensperson
            //hier kommt später die Weiterleitung zur Admin-Seite (Sprint 2)
            return true;
        }
        //Sonstiges Problem
        //deshalb kein weiteres Routing, sondern wir bleiben auf der Startseite
        return false;
    }

    /**
    Methode aus der Carlook-Vorlage
     */
    public PersonDTOimpl getCurrentPerson(){
        return this.personDTO;
    }


}
