package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.controllers.ValidationController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.*;
import org.springframework.stereotype.Controller;

/**
 * Controller für die Registrierung von Studenten.
 */
@Controller
public class StudentRegistrierenController extends PersonRegistrierenController implements ValidationController {
    // Repositories
    private final BerufsFeldRepository berufsFeldRepository;
    private final BrancheRepository brancheRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final OrtRepository ortRepository;
    private final StudienfachRepository studienfachRepository;

    /**
     * Konstruktor des Controllers für die Registrierung von Studenten.
     *
     * @param berufsFeldRepository   Repository für Berufsfelder
     * @param brancheRepository      Repository für Branchen
     * @param jobKategorieRepository Repository für JobKategorien
     * @param ortRepository          Repository für Orte
     * @param personRepository       Repository für Personen
     * @param studienfachRepository  Repository für Studienfächer
     */
    public StudentRegistrierenController(
            BerufsFeldRepository berufsFeldRepository,
            BrancheRepository brancheRepository,
            JobKategorieRepository jobKategorieRepository,
            OrtRepository ortRepository,
            PersonRepository personRepository,
            StudienfachRepository studienfachRepository
    ) {
        super(personRepository);
        this.berufsFeldRepository = berufsFeldRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortRepository = ortRepository;
        this.studienfachRepository = studienfachRepository;
    }

    /**
     * Legt einen neuen Studenten an.
     *
     * @param student        Student, der angelegt werden soll
     * @param hasAcceptedAGB ob die AGB akzeptiert wurden
     * @return ob der Student angelegt werden konnte
     */
    public boolean createStudent(Student student, boolean hasAcceptedAGB) {
        //Account "aktivieren"
        student.setAktiv(true);
        student.setGesperrt(false);
        return ValidationController.isValidJobKategorie(student.getJobKategorien(), jobKategorieRepository) &&
                ValidationController.isValidBranche(student.getBranchen(), brancheRepository) &&
                ValidationController.isValidBerufsFeld(student.getBerufsFelder(), berufsFeldRepository) &&
                ValidationController.isValidOrt(student.getOrte(), ortRepository) &&
                ValidationController.isValidStudienfach(student.getStudienfach(), studienfachRepository) &&
                super.createPerson(student, hasAcceptedAGB);
    }
}
