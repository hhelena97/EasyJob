package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
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
    void vollTextSuche() throws Exception {
        // ********* Arrange *********
        Optional<Job>[] job_op = new Optional[2];
        Job[] jobs = new Job[2];
        job_op[0] = jobRepo.findById(1);
        job_op[1] = jobRepo.findById(5);

        int i = 0;
        for (Optional<Job> job : job_op) {
            if (job.isEmpty()) {
                throw new Exception("Kein Objekt gefunden in der Datenbank!");
            } else {
                jobs[i] = job.get();
                i++;
            }
        }

        List<Job> expected = List.of(jobs);

        // *********** Act ***********
        List<Job> actual = joSu.vollTextSuche("NetSolutions");

        // ********* Assert **********
        assertEquals(expected, actual);
    }

    /**
     * testet teilZeichenSuche(String teilZeichen) mit Teilzeichen
     */
    @Test
    @DisplayName("Testet die Teilzeichensuche")
    @Transactional
    void teilZeichenSuche() throws Exception {
        // ********* Arrange *********
        Optional<Job>[] job_op = new Optional[3];
        Job[] jobs = new Job[3];
        job_op[0] = jobRepo.findById(1);
        job_op[1] = jobRepo.findById(7);
        job_op[2] = jobRepo.findById(5);

        int i = 0;
        for (Optional<Job> job : job_op) {
            if (job.isEmpty()) {
                throw new Exception("Kein Objekt gefunden in der Datenbank!");
            } else {
                jobs[i] = job.get();
                i++;
            }
        }

        List<Job> expected = List.of(jobs);

        // *********** Act ***********
        List<Job> actual = joSu.teilZeichenSuche("Solu");

        // ********* Assert **********
        assertEquals(expected, actual);
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