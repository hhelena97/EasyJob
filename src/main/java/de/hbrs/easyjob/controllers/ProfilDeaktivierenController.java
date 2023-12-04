package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfilDeaktivierenController {

    //TODO: immer wenn Profil aufgerufen wird muss geprüft werden, ob die Spalte Aktiv false oder true ist:
    // nur aufrufen, wenn true ist.

    private PersonRepository personRepository;
    private UnternehmenRepository unternehmenRepository;

    /**
     * Konstruktor
     * @param pR Repository Person
     * @param uR Repository Unternehmen
     */
    public ProfilDeaktivierenController(PersonRepository pR, UnternehmenRepository uR) {
        this.personRepository = pR;
        this.unternehmenRepository = uR;
    }

    /**
     * Diese Methode deaktiviert den gesamten Account eines Studenten. Alle Informationen über diese Person bleiben
     * in der Datenbank. Das Profil ist aber für niemanden mehr sichtbar (Admin?)
     * @param person das Profil welches deaktiviert werden soll
     * @return true, wenn der Account erfolgreich deaktiviert wurde, wenn irgendein Fehler aufgetreten ist false
     */
    public boolean profilDeaktivierenPerson(Person person) {
        if (person == null) {
            return false;
        }
        person.setAktiv(false);
        return !personRepository.save(person).getAktiv();
    }

    /**
     * Der Manager kann das Profil des unternehmens deaktivieren. Sein Account, sowie der Account aller zugehörigen
     * Unternehmenspersonen wird ebenfalls deaktiviert.
     * @param manager Manager des Unternehmens
     * @return true, wenn alles fertig ist und geklappt hat
     */
    public boolean profilDeaktivierenUnternehmen(Unternehmensperson manager) {
        if(manager == null) {
            return false;
        }

        Unternehmen unternehmen = manager.getUnternehmen();

        // Unternehmensprofil deaktivieren
        unternehmen.setAktiv(false);
        if (unternehmenRepository.save(unternehmen).isAktiv()) {
            // Gebe false zurück, falls Unternehmen noch aktiv ist
            return false;
        }

        // alle unternehmenspersonen rausfiltern und über andere Methode deaktivieren
        boolean success = true;
        List<Person> mitarbeiter = personRepository.findAllByUnternehmenId(unternehmen.getId_Unternehmen());
        for (Person p: mitarbeiter) {
            if (!profilDeaktivierenPerson(p)) {
                // Setze success auf false, falls eine Deaktivierung fehlschlägt
                success = false;
            }
        }
        return success;
    }
}