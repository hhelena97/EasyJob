package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProfilSperrenController {

    private final PersonRepository personRepository;

    private final UnternehmenRepository unternehmenRepository;

    private final JobRepository jobRepository;

    /**
     * Konstruktor
     * @param pR Repository Person
     */
    public ProfilSperrenController(PersonRepository pR, UnternehmenRepository uR, JobRepository jR) {
        this.personRepository = pR;
        this.unternehmenRepository = uR;
        this.jobRepository = jR;
    }


    /**
     * Diese Methode sperrt eine Person.
     * Das Profil ist nur noch für den Admin sichtbar.
     * @param person das Profil welches gesperrt werden soll
     * @return true, wenn der Account erfolgreich deaktiviert wurde, wenn irgendein Fehler aufgetreten ist false
     */
    public boolean personSperren(Person person) {

        if (person == null){
            return false;
        }

        if (person instanceof Unternehmensperson){
            Unternehmen u = ((Unternehmensperson) person).getUnternehmen();
            /* Todo: suche das Unternehmen heraus,
            schaue nach, ob die Person Manager ist,
            wenn ja, schaue nach, ob es noch mehr Mitarbeiter gibt und mache den nächsten zum neuen Manager
            wenn nein, sperre das Unternehmen
*/
         /*
            int id_u = unternehmenRepository.findById(u.getId_Unternehmen());
            Unternehmensperson manager = u.getUnternehmensperson();
            if (manager.getId_Person() == person.getId_Person()){
                List<Person> mitarbeiter = personRepository.findAllByUnternehmenId(u.getId_Unternehmen());
                if (mitarbeiter.size() > 1){
                    //mache den nächsten Mitarbeiter zum neuen Manager
                    Unternehmensperson neuerManager = mitarbeiter.get(1);
                    unternehmenRepository.save(u).getUnternehmensperson(neuerManager);
                } else {
                    unternehmenSperren(u);
                }
            }
            */


            // Todo: alle Jobs des Mitarbeiters sperren
        }
        personRepository.save(person).setGesperrt(true);
        return personRepository.save(person).getGesperrt();
    }

    /**
     * Ein Unternehmen sperren, sperrt alle Mitarbeiter und Jobs des Unternehmens
     * @param unternehmen, das Unternehmen, dass gesperrt werden soll.
     * @return true, wenn alles fertig ist und geklappt hat
     */
    public boolean unternehmenSperren(Unternehmen unternehmen) {
        if(unternehmen == null) {
            return false;
        }

        // Unternehmensprofil deaktivieren
        //finde alle Mitarbeiter des Unternehmens und sperre sie
        boolean success = true;
        List<Person> mitarbeiter = personRepository.findAllByUnternehmenId(unternehmen.getId_Unternehmen());
        for (Person p: mitarbeiter) {
            if (! personSperren(p)) {
                // Setze success auf false, falls eine Sperrung fehlschlägt
                success = false;
            }
        }
        unternehmen.setGesperrt(true);
        return success;
    }

    /**
     * Diese Methode sperrrt einen Job
     * @param job   der Job der gesperrt werden soll
     * @return true, wenn der Job gesperrt ist
     */
    public boolean jobSperren(Job job) {
        if (job == null) {
            return false;
        }
        job.setGesperrt(true);
        return jobRepository.save(job).getGesperrt();
    }



    /**
     * Diese Methode entsperrt eine Person
     * @param person die Person, die entsperrt werden soll
     * @return true, wenn der Account erfolgreich entsperrt wurde
     */
    public boolean personEntsperren(Person person) {
        if (person == null) {
            return false;
        }
        person.setGesperrt(false);
        return personRepository.save(person).getGesperrt();
    }

}
