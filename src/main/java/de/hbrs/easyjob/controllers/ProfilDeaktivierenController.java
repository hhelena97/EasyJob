package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProfilDeaktivierenController {

    private final JobRepository jobRepository;
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;

    /**
     * Konstruktor
     * @param pR Repository Person
     * @param uR Repository Unternehmen
     * @param jR Repository Job
     */
    public ProfilDeaktivierenController(PersonRepository pR, UnternehmenRepository uR, JobRepository jR) {
        this.jobRepository = jR;
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

        if (person == null) {
            return false;
        }

        if (person instanceof Unternehmensperson) {

            for (Job j: jobRepository.findAllJobs(person.getId_Person())) {
                j.setAktiv(false); //deaktivieren
                jobRepository.save(j);
            }

            person.setAktiv(false);
        } else {
            person.setAktiv(false);
        }
        return !personRepository.save(person).getAktiv();
    }

    /**
     * Der Manager kann das Profil des unternehmens deaktivieren. Sein Account, sowie der Account aller zugehörigen
     * Unternehmenspersonen wird ebenfalls deaktiviert.
     * @param manager Manager des Unternehmens
     * @return true, wenn alles fertig ist und geklappt hat
     */
    public boolean profilDeaktivierenUnternehmen(Unternehmensperson manager) {
        if(manager != null) { // Übergebene Unternehmensperson ist nicht null
            Unternehmen unternehmen = manager.getUnternehmen();
            if (unternehmen != null) { // Unternehmen von Unternehmensperson ist nicht null
                if (!manager.equals(unternehmen.getUnternehmensperson())) {
                    return false;
                }
                // Es ist wirklich der Manager des Unternehmens
                List<Person> unternehmenspersonen = personRepository.findAllByUnternehmenId(unternehmen.getId_Unternehmen());
                boolean mitarbeiterAktiv = true;
                for (Person p : unternehmenspersonen) { // Mitarbeiter deaktivieren
                    profilDeaktivierenPerson(p);
                    personRepository.save(p);
                    mitarbeiterAktiv = mitarbeiterAktiv && !p.getAktiv();
                }
                unternehmen.setAktiv(false); // Unternehmen deaktivieren
                manager.setAktiv(false); // Manager deaktivieren
                unternehmenRepository.save(unternehmen);
                personRepository.save(manager);
                return !unternehmen.isAktiv() && !manager.getAktiv() && mitarbeiterAktiv;
            }
        }
        return false;
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