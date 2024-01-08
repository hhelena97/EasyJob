package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.StudentRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentProfilFilterControllerTest {
    // Repositories
    @Autowired
    private JobKategorieRepository jobKategorieRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudienfachRepository studienfachRepository;

    // Controller
    @Autowired
    private StudentProfilFilterController studentProfilFilterController;

    @Test
    @DisplayName("testet die Filter-Student Funktion")
    @Transactional
    void filterStudent() {
        // ********* Arrange *********
        Studienfach studienfach = studienfachRepository.findById((long) 1).get(); // Technikjournalismus
        Ort ort = ortRepository.findById(5).get(); // Bonn
        JobKategorie jobKategorie = jobKategorieRepository.findById(1).get(); // Praktikum
        List<Student> expected = List.of(studentRepository.findById(1).get());

        // ********* Act *********
        List<Student> actual = studentProfilFilterController.filterJobs(ort, Set.of(jobKategorie), Set.of(studienfach));

        // ********* Assert *********
        assertEquals(expected, actual);
    }
}