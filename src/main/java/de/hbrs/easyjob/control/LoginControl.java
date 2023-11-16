package de.hbrs.easyjob.control;

import de.hbrs.easyjob.dtos.PersonDTO;
import de.hbrs.easyjob.dtos.impl.StudentDTOimpl;
import de.hbrs.easyjob.dtos.impl.UnternehmenspersonDTOimpl;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequestMapping("/api/login")
public class LoginControl {

    @Autowired
    private PersonRepository repository;
    private PersonDTO personDTO = null;
    private StudentDTOimpl studentDTO = null;
    private UnternehmenspersonDTOimpl unternpersonDTO = null;

    public LoginControl(PersonRepository personRepository) {
        this.repository = personRepository;
    }

    // wenn authenticate ein false zurückgibt, soll im View ein Popup "Bitte eingegebene Daten überprüfen" (oder so ähnlich)
    // true führt direkt zur nächsten Seite
    public boolean authenticate(String email, String password) {
        Person person;
        try {
            person = repository.findByEmail(email);
            System.out.println("Datenbankverbindung erfolgreich.");
        } catch ( org.springframework.dao.DataAccessResourceFailureException e ) {
            System.out.println("Problem mit der Datenbankverbindung.");
            return false;
        }
        if (person == null){
            System.out.println("Person kann nicht gefunden werden.");
            return false;
        }
        if (password == null){
            System.out.println("Passwort fehlt.");
            return false;
        }
        String eingabePW = password;
        String dbPW = person.getPasswort();

        if (eingabePW.equals(dbPW)){
            System.out.println("Passwort stimmt.");
            this.personDTO = repository.findPersonByEmail(email);

            if (person instanceof Student){
                this.studentDTO = (StudentDTOimpl) personDTO;
                System.out.println("Es ist ein Student.");
                //TODO: weiter zur Studenten-Startseite
                return true;
            }

            if (person instanceof Unternehmensperson){
                this.unternpersonDTO = (UnternehmenspersonDTOimpl) personDTO;
                System.out.println("Es ist eine Unternehmensperson.");
                //TODO: weiter zur Unternehmer-Startseite
                return true;
            }
            //es ist eine Person, aber kein Student oder Unternehmensperson
            return true;
        }
        //Sonstiges Problem
        //bleibt auf der Startseite
        return false;
    }

    public PersonDTO getCurrentPerson(){
        return this.personDTO;

    }


/*
    private PersonDTO getPersonWithJPA( String email , String password ) throws DatabasePersonException {
        PersonDTO personTmp;
        try {
            personTmp = repository.findPersonByEmailAndPassword(email, password);
        } catch ( org.springframework.dao.DataAccessResourceFailureException e ) {
            throw new DatabasePersonException("Die E-Mail mit diesem Passwort konnten keiner Person in der Datenbank zugeordnet werden.");
        }
        return personTmp;
    }
*/



}
