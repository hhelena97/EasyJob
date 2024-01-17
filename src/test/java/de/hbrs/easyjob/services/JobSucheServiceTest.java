package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.JobRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobSucheServiceTest {
    // Repositories
    @Autowired
    private JobRepository jobRepo;

    // Services
    private JobSucheService joSu;

    /**
     * erstellt zu testenden Service
     */
    @BeforeEach
    void setUp() {
        joSu = new JobSucheService(jobRepo);
    }

    /**
     * nullt zu testenden Services, damit pro Test ein neuer Services erstellt werden kann
     */
    @AfterEach
    void tearDown() {
        joSu = null;
    }

    /**
     * testet vollTextSuche(String vollText) mit Wörtern
     */
    @Test
    @DisplayName("Testet die Volltextsuche")
    @Transactional
    void vollTextSuche(){
        // ********* Arrange *********
        Job[] jobs = new Job[2];
        jobs[0] = jobRepo.findById(1).orElseThrow(NullPointerException::new);
        jobs[1] = jobRepo.findById(5).orElseThrow(NullPointerException::new);

        List<Job> expected = List.of(jobs);

        // *********** Act ***********
        List<Job> actual = joSu.vollTextSuche("NetSolutions");

        // ********* Assert **********
        assertTrue(actual.containsAll(expected));
    }

    /**
     * testet teilZeichenSuche(String teilZeichen) mit Teilzeichen
     */
    @Test
    @DisplayName("Testet die Teilzeichensuche")
    @Transactional
    void teilZeichenSuche() {
        // ********* Arrange *********
        Job[] jobs = new Job[3];
        jobs[0] = jobRepo.findById(1).orElseThrow(NullPointerException::new);
        jobs[1] = jobRepo.findById(7).orElseThrow(NullPointerException::new);
        jobs[2] = jobRepo.findById(5).orElseThrow(NullPointerException::new);

        List<Job> expected = List.of(jobs);

        // *********** Act ***********
        List<Job> actual = joSu.teilZeichenSuche("Solu");

        // ********* Assert **********
        assertTrue(actual.containsAll(expected));
    }

    /**
     * testet isVollTextSuche(String keyword) mit Wörtern und Teilwörtern
     */
    @Test
    @DisplayName("Testet die Funktion, die überprüft, ob etwas Volltext ist oder Teilzeichen")
    void isVollTextSuche() {
        // *********** Act ***********
        boolean actual = joSu.isVollTextSuche("Telekom");
        boolean actual2 = joSu.isVollTextSuche("T");
        boolean actual3 = joSu.isVollTextSuche("Easy");
        boolean actual4 = joSu.isVollTextSuche("EasyQube");
        boolean actual5 = joSu.isVollTextSuche("Bonn");
        boolean actual6 = joSu.isVollTextSuche("BioChemTech");
        boolean actual7 = joSu.isVollTextSuche("NetSolutions");

        // ********* Assert **********
        assertTrue(actual);
        assertFalse(actual2);
        assertTrue(actual3);
        assertTrue(actual4);
        assertTrue(actual5);
        assertTrue(actual6);
        assertTrue(actual7);
    }
}