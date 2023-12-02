package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.service.JobService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Controller für die Stellenanzeigen
 * TODO: Möglicherweise mit JobSucheService zusammenführen
 */
@Component
public class StellenanzeigeControl {
    private final JobService jobService;

    /**
     * Konstruktor des Controllers für die Stellenanzeigen
     * @param jobService Repository für die Stellenanzeigen
     */
    public StellenanzeigeControl(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Erstellt eine neue Stellenanzeige mit aktuellem Datum als Erstellungsdatum
     * @param titel Titel der Stellenanzeige
     * @param freitext Freitext der Stellenanzeige
     * @param eintrittsdatum Datum, an dem die Stelle angetreten werden soll
     * @param unternehmen Unternehmen, das die Stelle anbietet
     * @param unternehmensperson Ansprechpartner des Unternehmens
     * @param ort Ort, an dem die Stelle angeboten wird
     * @param jobKategorie Kategorie der Stelle
     * @param studienfaecher Studienfächer, die für die Stelle relevant sind
     * @return Die erstellte Stellenanzeige
     */
    public Job stellenanzeigeErstellen(
            String titel,
            String freitext,
            Date eintrittsdatum,
            Unternehmen unternehmen,
            Unternehmensperson unternehmensperson,
            Ort ort,
            JobKategorie jobKategorie,
            Set<Studienfach> studienfaecher,
            boolean homeOffice
    ) {
        // Return null if any of the parameters is null
        if (titel == null || freitext == null || eintrittsdatum == null || unternehmen == null || unternehmensperson == null || ort == null || jobKategorie == null || studienfaecher == null) {
            return null;
        }

        Job job = new Job();

        job.setTitel(titel);
        job.setFreitext(freitext);
        job.setErstellt_am(new Date());
        job.setEintritt(eintrittsdatum);
        job.setUnternehmen(unternehmen);
        job.setPerson(unternehmensperson);
        job.setOrt(ort);
        job.setJobKategorie(jobKategorie);
        job.setStudienfacher(studienfaecher);
        job.setHomeOffice(homeOffice);

        System.out.printf("Job '%s' von '%s' erstellt\n", job.getTitel(), job.getUnternehmen().getName());

        return jobService.saveJob(job);
    }

    /** Gibt alle Stellenanzeigen des Unternehmens mit der übergebenen ID zurück
     * @param unternehmenId ID des Unternehmens
     * @return Liste der Stellenanzeigen des Unternehmens
     */
    public List<Job> stellenanzeigenEinesUnternehmens(int unternehmenId) {
        return jobService.findAllByUnternehmenId(unternehmenId);
    }
}
