package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unchecked")
@SpringBootTest
class JobSucheControllerTest {
    // Repositories
    @Autowired
    private JobRepository jobRepo;

    // Controller
    @Autowired
    private JobSucheController jobSucheController;

    @Test
    @DisplayName("Testet die Volltextsuche")
    @Transactional
    void searchJobsVolltext() throws Exception {
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
        List<Job> actual = jobSucheController.searchJobs("NetSolutions");

        // ********* Assert **********
        assertEquals(expected, actual);
    }

    /**
     * testet teilZeichenSuche(String teilZeichen) mit Teilzeichen
     */
    @Test
    @DisplayName("Testet die Teilzeichensuche")
    @Transactional
    void searchJobsTeilwort() throws Exception {
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
        List<Job> actual = jobSucheController.searchJobs("Solu");

        // ********* Assert **********
        System.out.println(expected);
        System.out.println(actual);
        for(i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId_Job(), actual.get(i).getId_Job());
        }
    }
}