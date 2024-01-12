package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfilDeaktivierenController {

    //TODO: immer wenn Profil aufgerufen wird muss geprüft werden, ob die Spalte Aktiv false oder true ist:
    // nur aufrufen, wenn true ist.

    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;
    @Autowired
    private JobRepository jobRepository;
    //private ChatRepository chatRepository;

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
     * in der Datenbank. Das Profil ist nur noch für den Admin sichtbar
     * @param person das Profil welches deaktiviert werden soll
     * @return true, wenn der Account erfolgreich deaktiviert wurde, wenn irgendein Fehler aufgetreten ist false
     */
    public boolean profilDeaktivierenPerson(Person person) {

        //Todo: prüfen, ob die Person Manager des Unternehmens ist. Wenn ja, weitere Person suchen,
        //die dadurch Manager wird, ansonsten Unternehmen mit deaktivieren.

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

    /**
     * Diese Methode sperrrt einen Job
     * //neue Variable gesperrt (true wenn gesperrt)
     * @param job   der Job der gesperrt werden soll
     */
    public boolean profilDeaktivierenJob(Job job) {
        if (job == null) {
            return false;
        }
        job.setAktiv(false);
        return true;
        // personRepository.save(job).getAktiv();
    }

    /**
     * Diese Methode reaktiviert den Account einer Person.
     * @param person das Profil welches reaktiviert werden soll
     * @return true, wenn der Account erfolgreich reaktiviert wurde, wenn irgendein Fehler aufgetreten ist false
     */
    public boolean profilReaktivierenPerson(Person person) {
        if (person == null) {
            return false;
        }
        person.setAktiv(true);
        return personRepository.save(person).getAktiv();
    }
}