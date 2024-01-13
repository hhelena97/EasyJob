package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.repositories.UnternehmenspersonRepository;
import de.hbrs.easyjob.services.UnternehmenspersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class ProfilSperrenController {

    private final PersonRepository personRepository;

    private final UnternehmenRepository unternehmenRepository;

    private final JobRepository jobRepository;

    private final UnternehmenspersonRepository unternehmenspersonRepository;

    /**
     * Konstruktor
     * @param pR Repository Person
     */
    public ProfilSperrenController(PersonRepository pR, UnternehmenRepository uR, JobRepository jR, UnternehmenspersonRepository upR) {
        this.personRepository = pR;
        this.unternehmenRepository = uR;
        this.jobRepository = jR;
        this.unternehmenspersonRepository = upR;
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
            int u = ((Unternehmensperson) person).getUnternehmen().getId_Unternehmen();
            Unternehmensperson manager = ((Unternehmensperson) person).getUnternehmen().getUnternehmensperson();

            for (Job j: jobRepository.findAllJobs(person.getId_Person())) {
                j.setGesperrt(true); //sperren
                jobRepository.save(j);
            }

            if(person.equals(manager)) {
                for (Unternehmensperson p: unternehmenspersonRepository.findAllByUnternehmen(u)) {
                    for (Job j: jobRepository.findAllJobs(p.getId_Person())) {
                        jobSperren(j);
                    }
                    personRepository.save(p).setGesperrt(true);
                }
                unternehmenSperren(((Unternehmensperson) person).getUnternehmen());
            }
        }
        person.setGesperrt(true);
        System.out.println(person.getGesperrt());
        personRepository.save(person);

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
