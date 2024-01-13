package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProfilSperrenController {

    //TODO: immer wenn Profil aufgerufen wird muss geprüft werden, ob die Spalte Aktiv false oder true ist:
    // nur aufrufen, wenn true ist.

    private final PersonRepository personRepository;
@Autowired
    private UnternehmenRepository unternehmenRepository;

    @Autowired
    private JobRepository jobRepository;

    /**
     * Konstruktor
     * @param pR Repository Person
     */
    public ProfilSperrenController(PersonRepository pR) {
        this.personRepository = pR;
    }


    /**
     * Diese Methode sperrt eine Person.
     * Das Profil ist nur noch für den Admin sichtbar.
     * @param person das Profil welches gesperrt werden soll
     * @return true, wenn der Account erfolgreich deaktiviert wurde, wenn irgendein Fehler aufgetreten ist false
     */
    public boolean personSperren(Person person) {

        //Todo: prüfen, ob die Person eine Unternehmensperson ist und wenn ja, prüfen ob sie
        // Manager des Unternehmens ist. Wenn ja, weitere Person suchen,
        //die dadurch Manager wird, ansonsten Unternehmen mit deaktivieren.

        if (person == null) {
            return false;
        }
        //person.isGesperrt(true);
        //boolean gesperrt = personRepository.save(person).isGesperrt();
        return true;
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
            if (personSperren(p)) {
                // Setze success auf false, falls eine Deaktivierung fehlschlägt
                success = false;
            }
        }
        return success;
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
}
