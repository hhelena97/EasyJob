package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.services.JobService;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.UnternehmenService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest
class StellenanzeigeControllerTest {
    // Controller
    @Autowired
    private StellenanzeigeController stellenanzeigeController;

    // Services
    @Autowired
    private JobService jobService;
    @Autowired
    private PersonService personService;
    @Autowired
    private UnternehmenService unternehmenService;

    // Repositories
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobKategorieRepository jobKategorieRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private StudienfachRepository studienfachRepository;

    // Entities
    private Job job1, job2;
    private Unternehmen unternehmen1, unternehmen2;
    private Unternehmensperson unternehmensperson1, unternehmensperson2;
    private Ort ort1, ort2;
    private JobKategorie jobKategorie1, jobKategorie2;
    private Studienfach studienfach1, studienfach2;

    private String titel1, titel2;
    private String freitext1, freitext2;
    private Date eintritt1, eintritt2;
    private List<Integer> toDelete = new ArrayList<>();


    @BeforeEach
    void setUp() {
        // Job 1
        job1 = new Job();

        titel1 = "Werkstudent (m/w/d) Java-Entwicklung";
        freitext1 = "Wir suchen einen Werkstudenten (m/w/d) im Bereich Java-Entwicklung.";
        eintritt1 = Date.from(new Date().toInstant().plus(java.time.Duration.ofDays(30)));
        unternehmen1 = unternehmenService.findByID(2);
        unternehmensperson1 = unternehmen1.getUnternehmensperson();
        ort1 = ortRepository.findById(1).orElseThrow(NullPointerException::new);
        jobKategorie1 = jobKategorieRepository.findById(1).orElseThrow(NullPointerException::new);
        studienfach1 = studienfachRepository.findById(1).orElseThrow(NullPointerException::new);

        job1.setTitel(titel1);
        job1.setFreitext(freitext1);
        job1.setEintritt(eintritt1);
        job1.setUnternehmen(unternehmen1);
        job1.setPerson(unternehmensperson1);
        job1.setOrt(ort1);
        job1.setJobKategorie(jobKategorie1);
        job1.setStudienfacher(Set.of(studienfach1));
        job1.setAktiv(true);


        // Job 2
        job2 = new Job();

        titel2 = "Werkstudent (m/w/d) Python-Entwicklung";
        freitext2 = "Wir suchen einen Werkstudenten (m/w/d) im Bereich Python-Entwicklung.";
        eintritt2 = Date.from(new Date().toInstant().plus(java.time.Duration.ofDays(60)));
        unternehmen2 = unternehmenService.findByID(3);
        unternehmensperson2 = unternehmen2.getUnternehmensperson();
        ort2 = ortRepository.findById(2).orElseThrow(NullPointerException::new);
        jobKategorie2 = jobKategorieRepository.findById(2).orElseThrow(NullPointerException::new);
        studienfach2 = studienfachRepository.findById(2).orElseThrow(NullPointerException::new);

        job2.setUnternehmen(unternehmen2);
        job2.setTitel(titel2);
        job2.setFreitext(freitext2);
        job2.setEintritt(eintritt2);
        job2.setOrt(ort2);
        job2.setJobKategorie(jobKategorie2);
        job2.setPerson(unternehmensperson2);
        job2.setStudienfacher(Set.of(studienfach2));
        job2.setAktiv(true);
    }

    @Test
    @DisplayName("Gültige Stellenanzeige erstellen")
    void stelleAnzeigeErstellen() {
        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertEquals(job1.getAktiv(), actual.getAktiv());
        Assertions.assertEquals(job1.getEintritt(), actual.getEintritt());
        Assertions.assertEquals(job1.getFreitext(), actual.getFreitext());
        Assertions.assertEquals(job1.getJobKategorie(), actual.getJobKategorie());
        Assertions.assertEquals(job1.getOrt(), actual.getOrt());
        Assertions.assertEquals(job1.getPerson(), actual.getPerson());
        Assertions.assertEquals(job1.getStudienfacher(), actual.getStudienfacher());
        Assertions.assertEquals(job1.getTitel(), actual.getTitel());
        Assertions.assertEquals(job1.getUnternehmen(), actual.getUnternehmen());

        // Prepare for tearDown
        toDelete.add(actual.getId_Job());
    }

    @Test
    @DisplayName("Stellenanzeige mit titel == null erstellen")
    void stellenAnzeigeMitUngueltigemTitel() {
        // Arrange
        job1.setTitel(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit freitext == null erstellen")
    void stellenAnzeigeMitUngueltigemFreitext() {
        // Arrange
        job1.setFreitext(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit eintrittsdatum == null erstellen")
    void stellenAnzeigeMitUngueltigemEintrittsdatum() {
        // Arrange
        job1.setEintritt(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit unternehmen == null erstellen")
    void stellenAnzeigeMitUngueltigemUnternehmen() {
        // Arrange
        job1.setUnternehmen(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit unternehmensperson == null erstellen")
    void stellenAnzeigeMitUngueltigerUnternehmensperson() {
        // Arrange
        job1.setPerson(unternehmensperson2);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson2);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit ort == null erstellen")
    void stellenAnzeigeMitUngueltigemOrt() {
        // Arrange
        job1.setOrt(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit jobKategorie == null erstellen")
    void stellenAnzeigeMitUngueltigerJobKategorie() {
        // Arrange
        job1.setJobKategorie(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit studienfaecher == null erstellen")
    void stellenAnzeigeMitUngueltigenStudienfaechern() {
        // Arrange
        job1.setStudienfacher(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Stellenanzeige mit allen Parametern == null erstellen")
    void stellenAnzeigeMitAllenUngueltigenParametern() {
        // Arrange
        job1.setTitel(null);
        job1.setFreitext(null);
        job1.setEintritt(null);
        job1.setUnternehmen(null);
        job1.setPerson(null);
        job1.setOrt(null);
        job1.setJobKategorie(null);
        job1.setStudienfacher(null);

        // Act
        Job actual = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Gültige Stellenanzeige aktualisieren")
    void stelleAnzeigeAktualisieren() {
        // Arrange
        String newTitle = "Werkstudent (m/w/d) C++-Entwicklung";
        Job newJob = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        job2.setId_Job(newJob.getId_Job());
        job2.setTitel(newTitle);
        job2.setUnternehmen(unternehmen1);
        job2.setPerson(unternehmensperson1);

        // Assert
        Assertions.assertEquals(titel1, newJob.getTitel());

        // Act
        Job editedJob = stellenanzeigeController.stellenanzeigeAktualisieren(job2, unternehmensperson1);

        // Assert
        Assertions.assertEquals(newTitle, editedJob.getTitel());

        // Prepare for tearDown
        toDelete.add(newJob.getId_Job());
    }

    @Test
    @DisplayName("Stellenanzeige mit falscher unternehmensperson aktualisieren")
    void stelleAnzeigeMitFalscherUnternehmenspersonAktualisieren() {
        // Arrange
        Job newJob = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);

        // Assert
        Assertions.assertEquals(titel1, newJob.getTitel());

        // Act
        Job editedJob = stellenanzeigeController.stellenanzeigeAktualisieren(newJob, unternehmensperson2);

        // Assert
        Assertions.assertNull(editedJob);
        Assertions.assertEquals(titel1, newJob.getTitel());

        // Prepare for tearDown
        toDelete.add(newJob.getId_Job());
    }

    @Test
    @DisplayName("Gebe alle Stellenanzeigen von Unternehmen 1 zurück")
    void stellenanzeigenEinesUnternehmens() {
        // Arrange
        Job newJob1 = stellenanzeigeController.stellenanzeigeErstellen(job1, unternehmensperson1);
        job2.setTitel("Werkstudent (m/w/d) C++-Entwicklung");
        job2.setUnternehmen(unternehmen1);
        job2.setPerson(unternehmensperson1);
        Job newJob2 = stellenanzeigeController.stellenanzeigeErstellen(job2, unternehmensperson1);

        // Act
        List<Job> actual = stellenanzeigeController.stellenanzeigenEinesUnternehmens(unternehmen1.getId_Unternehmen());

        // Assert
        Assertions.assertTrue(actual.stream().anyMatch(job -> newJob1.getId_Job().equals(job.getId_Job())));
        Assertions.assertTrue(actual.stream().anyMatch(job -> newJob2.getId_Job().equals(job.getId_Job())));

        // Prepare for tearDown
        toDelete.add(newJob1.getId_Job());
        toDelete.add(newJob2.getId_Job());
    }

    @AfterEach
    void tearDown() {
        try {
            for (Integer id : toDelete) {
                jobRepository.deleteJobSuchtStudienfach(id);
                jobRepository.deleteById(id);
            }
            toDelete.clear();
        } catch (Exception e) {
            System.out.println("Error while tearing down: " + e.getMessage());
            System.out.println("You might need to remove the entries manually.");
        }
    }
}