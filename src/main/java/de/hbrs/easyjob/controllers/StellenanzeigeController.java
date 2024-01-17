package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Controller für die Stellenanzeigen
 */
@Component
public class StellenanzeigeController {
    private static final Logger logger = LoggerFactory.getLogger(StellenanzeigeController.class);
    private final JobService jobService;

    /**
     * Konstruktor des Controllers für die Stellenanzeigen
     *
     * @param jobService Repository für die Stellenanzeigen
     */
    public StellenanzeigeController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Erstellt eine neue Stellenanzeige mit aktuellem Datum als Erstellungsdatum
     *
     * @param job     Stellenanzeige, die erstellt werden soll
     * @param creator Unternehmensperson, die die Stellenanzeige erstellt
     * @return Die erstellte Stellenanzeige
     */
    public Job stellenanzeigeErstellen(@NotNull Job job, @NotNull Unternehmensperson creator) {
        if (Boolean.TRUE.equals(job.getAktiv() && !isValid(job, creator))) {
            // Return null if any of the required fields is null
            return null;
        }

        job.setErstellt_am(new Date());
        job.setGesperrt(false);


        logger.info(
                "Job '{}' von '{}' erstellt. %n",
                job.getTitel(),
                creator.getEmail()
        );

        return jobService.saveJob(job);
    }

    /**
     * Aktualisiert eine Stellenanzeige
     *
     * @param job    Stellenanzeige, die aktualisiert werden soll
     * @param editor Unternehmensperson, die die Stellenanzeige aktualisiert
     * @return Die aktualisierte Stellenanzeige
     */
    public Job stellenanzeigeAktualisieren(@NotNull Job job, @NotNull Unternehmensperson editor) {
        if (Boolean.TRUE.equals(job.getAktiv())) {
            if (!isValid(job, editor)) return null;
        } else {
            if (!canEdit(job, editor)) return null;
        }

        logger.info(
                "Job '{}' von '{}' aktualisiert. %n",
                job.getTitel(),
                editor.getEmail()
        );

        return jobService.saveJob(job);
    }

    /**
     * Gibt alle Stellenanzeigen des Unternehmens mit der übergebenen ID zurück
     *
     * @param unternehmenId ID des Unternehmens
     * @return Liste der Stellenanzeigen des Unternehmens
     */
    public List<Job> stellenanzeigenEinesUnternehmens(int unternehmenId) {
        return jobService.findAllByUnternehmenId(unternehmenId);
    }

    /**
     * Prüft, ob alle Felder der Stellenanzeige gesetzt sind und ob der Editor der Stellenanzeige Teil des Unternehmens ist
     *
     * @param job    Stellenanzeige, die geprüft werden soll
     * @param editor Unternehmensperson, die die Stellenanzeige erstellt oder aktualisiert
     * @return true, wenn alle Felder gesetzt sind und der Editor Teil des Unternehmens ist, sonst false
     */
    private boolean isValid(@NotNull Job job, @NotNull Unternehmensperson editor) {
        if (
                job.getTitel() == null ||
                        job.getFreitext() == null ||
                        job.getEintritt() == null ||
                        job.getUnternehmen() == null ||
                        job.getPerson() == null ||
                        job.getOrt() == null ||
                        job.getJobKategorie() == null ||
                        job.getStudienfacher() == null
        ) {
            return false;
        }
        return job.getUnternehmen().getId_Unternehmen().equals(editor.getUnternehmen().getId_Unternehmen());
    }

    /**
     * Prüft, ob die Stellenanzeige von der übergebenen Unternehmensperson bearbeitet werden kann.
     * Die Stellenanzeige kann bearbeitet werden, wenn die Unternehmensperson Teil des Unternehmens ist und weder die
     * Unternehmensperson noch die Stellenanzeige gesperrt sind.
     *
     * @param job    Stellenanzeige
     * @param editor Unternehmensperson
     * @return true, wenn die Stellenanzeige von der Unternehmensperson bearbeitet werden kann, sonst false
     */
    public boolean canEdit(Job job, Unternehmensperson editor) {
        return Boolean.TRUE.equals(
                job.getUnternehmen().getId_Unternehmen().equals(editor.getUnternehmen().getId_Unternehmen()) &&
                        !job.getGesperrt() &&
                        !editor.getGesperrt()
        );
    }
}
