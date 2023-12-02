package de.hbrs.easyjob.service;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@SpringBootTest
class JobFilterServiceTest {

    // Repositories
    private final static OrtRepository ortRepo = mock(OrtRepository.class);
    private final static JobKategorieRepository joKaRepo = mock(JobKategorieRepository.class);
    private final static StudienfachRepository stuFaRepo = mock(StudienfachRepository.class);
    private final static PersonRepository persRepo = mock(PersonRepository.class);
    private final static UnternehmenRepository unterRepo = mock(UnternehmenRepository.class);
    private final static JobRepository jobRepo = mock(JobRepository.class);

    // Services
    @InjectMocks
    private JobFilterService joFiS;
    private static JobService joSe;

    // Datenobjekte
    private static Job job1;
    private static Job job2;
    private final static Studienfach stuFa1 = new Studienfach();
    private final static Studienfach stuFa2 = new Studienfach();
    private final static JobKategorie joKa1 = new JobKategorie();
    private final static JobKategorie joKa2 = new JobKategorie();
    private final static Ort ort1 = new Ort();
    private final static Ort ort2 = new Ort();
    private final static Unternehmen u1 = new Unternehmen();
    private final static Unternehmen u2 = new Unternehmen();
    private final static Unternehmensperson p1 = new Unternehmensperson();
    private final static Unternehmensperson p2 = new Unternehmensperson();

    /**
     * setzt die Daten für die Job-Objekte
     */
    @BeforeAll
    static void setUp() {

        // Daten für Jobs eingeben
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        ort2.setPLZ("50667");
        ort2.setOrt("Köln");
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p2.setVorname("Mareike");
        p2.setNachname("Musterfrau");
        p2.setEmail("mareike.musterfrau@example.de");
        u1.setUnternehmensperson(p1);
        u1.setName("EasyQube");
        u2.setUnternehmensperson(p2);
        u2.setName("BWI");
        joKa1.setKategorie("Masterarbeit");
        joKa2.setKategorie("Praktikum");

        Set<Studienfach> tmp1 = new HashSet<>();
        tmp1.add(stuFa1);
        Set<Studienfach> tmp2 = new HashSet<>();
        tmp2.add(stuFa1);
        tmp2.add(stuFa2);

        // Daten in Jobs einfügen
        job1 = new Job();
        job1.setOrt(ort1);
        job1.setPerson(p1);
        job1.setStudienfacher(tmp1);
        job1.setJobKategorie(joKa1);
        job1.setUnternehmen(u1);

        job2 = new Job();
        job2.setOrt(ort1);
        job2.setPerson(p2);
        job2.setStudienfacher(tmp2);
        job2.setJobKategorie(joKa2);
        job2.setUnternehmen(u2);

        // Jobs speichern
        joSe = new JobService(jobRepo, stuFaRepo, joKaRepo, persRepo, unterRepo, ortRepo);
        joSe.saveJob(job1);
        joSe.saveJob(job2);
    }

    /**
     * resettet die gemockten Repositories
     */
    @AfterAll
    static void tearDown() {
        Mockito.reset(jobRepo);
        Mockito.reset(stuFaRepo);
        Mockito.reset(joKaRepo);
        Mockito.reset(persRepo);
        Mockito.reset(unterRepo);
        Mockito.reset(ortRepo);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass keine Filterangaben gemacht wurden. Dabei sollte dann alle existierenden Datensätze ausgegeben werden.
     */
    @Test
    @DisplayName("Test für Jobsuche ohne Filter")
    void filterJobsWithoutFilter() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1,job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(null,null,null);

        // ********* Assert **********
        assertEquals(expected, actual);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nur nach einem Ort gefiltert wird
     */
    @Test
    @DisplayName("Test für Jobsuche nach Ort, an dem es einen Job gibt")
    void filterJobsAfterLocationWithJob() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1,job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(ort1,null,null);

        // ********* Assert **********
        assertEquals(expected, actual);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nur nach einem Ort gefiltert wird, and em es jedoch keinen Job gibt
     */
    @Test
    @DisplayName("Test für Jobsuche nach Ort, an dem es keinen Job gibt")
    void filterJobsAfterLocationWithoutJob() {
        // ********* Arrange *********
        List<Job> expected = List.of();
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *********** Act ***********
        List<Job> actual2 = joFiS.filterJobs(ort2,null,null);
        // ********* Assert **********
        assertEquals(0, actual2.size());
    }
    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nur nach einer JobKategorie gefiltert wird
     */
    @Test
    @DisplayName("Test für Jobsuche nach JobKategorie")
    void filterJobsAfterJobKategorie() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(null,joKa1,null);

        // ********* Assert **********
        assertEquals(expected, actual);


        // ********* Arrange *********
        List<Job> expected2 = List.of(job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected2);

        // *********** Act ***********
        List<Job> actual2 = joFiS.filterJobs(null,joKa2,null);

        // ********* Assert **********
        assertEquals(expected2, actual2);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nur nach einem Studienfach gefiltert wird
     */
    @Test
    @DisplayName("Test für Jobsuche nach Studienfach")
    void filterJobsAfterStudienfach() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1,job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(null,null,Set.of(stuFa1));
        List<Job> actual2 = joFiS.filterJobs(null,null,Set.of(stuFa1, stuFa2));

        // ********* Assert **********
        assertEquals(expected, actual);
        assertEquals(expected, actual2);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nach Studienfächern und dem Ort gefiltert wird
     */
    @Test
    @DisplayName("Test für Jobsuche nach Studienfach und Ort")
    void filterJobsAfterStudienfachAndLocation() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1,job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(ort1,null,Set.of(stuFa1));

        // ********* Assert **********
        assertEquals(expected, actual);


        // ********* Arrange *********
        List<Job> expected2 = List.of();
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected2);

        // *********** Act ***********
        List<Job> actual2 = joFiS.filterJobs(ort2,null,Set.of(stuFa1));

        // ********* Assert **********
        assertEquals(expected2, actual2);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nach einer JobKategorie und einem Ort gefiltert wird
     */
    @Test
    @DisplayName("Test für Jobsuche nach JobKategorie und Ort")
    void filterJobsAfterJobKategorieAndLocation() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(ort1,joKa1,null);

        // ********* Assert **********
        assertEquals(expected, actual);


        // ********* Arrange *********
        List<Job> expected2 = List.of(job2);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual2 = joFiS.filterJobs(ort1,joKa2,null);

        // ********* Assert **********
        assertEquals(expected2, actual2);
    }

    /**
     * testet die Funktion filterJobs(Ort ort, JobKategorie kategorie, Set<Studienfach> studienfacher) unter der Annahme,
     * dass nach allen möglichen Suchparametern gefiltert werden soll
     */
    @Test
    @DisplayName("Test für Jobsuche nach Studienfach, JobKategorie und Ort")
    void filterJobs() {
        // ********* Arrange *********
        List<Job> expected = List.of(job1);
        when(jobRepo.findAll(any(Specification.class))).thenReturn(expected);

        // *********** Act ***********
        List<Job> actual = joFiS.filterJobs(ort1,joKa1,Set.of(stuFa1));

        // ********* Assert **********
        assertEquals(expected, actual);
    }
}