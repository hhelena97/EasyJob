package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobServiceTest {
    // Repositories
    private final static OrtRepository ortRepository = mock(OrtRepository.class);
    private final static JobKategorieRepository jobKategorieRepository = mock(JobKategorieRepository.class);
    private final static StudienfachRepository studienfachRepository = mock(StudienfachRepository.class);
    private final static PersonRepository personRepository = mock(PersonRepository.class);
    private final static UnternehmenRepository unternehmenRepository = mock(UnternehmenRepository.class);
    private final static JobRepository jobRepository = mock(JobRepository.class);

    // Services
    private JobService jobService;

    // Entities
    private static Job job1;
    private final static Studienfach studienfach1 = new Studienfach();
    private final static JobKategorie jobKategorie1 = new JobKategorie();
    private final static Ort ort1 = new Ort();
    private final static Unternehmen unternehmen1 = new Unternehmen();
    private final static Unternehmensperson unternehmensperson1 = new Unternehmensperson();

    /**
     * setzt die Daten für das Job-Objekt auf
     */
    @BeforeAll
    static void setUp() {
        // Daten für Job aufsetzen
        unternehmensperson1.setVorname("Max");
        unternehmensperson1.setNachname("Mustermann");
        unternehmensperson1.setEmail("max.mustermann@example.de");
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        unternehmen1.setUnternehmensperson(unternehmensperson1);
        unternehmen1.setName("EasyQube");
        jobKategorie1.setKategorie("Masterarbeit");
        Set<Studienfach> tmp = new HashSet<>();
        tmp.add(studienfach1);

        // Daten in Job einsetzen
        job1 = new Job();
        job1.setUnternehmen(unternehmen1);
        job1.setPerson(unternehmensperson1);
        job1.setOrt(ort1);
        job1.setJobKategorie(jobKategorie1);
        job1.setStudienfacher(tmp);
    }

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUpEach() {
        jobService = new JobService(jobRepository, studienfachRepository, jobKategorieRepository, personRepository, unternehmenRepository, ortRepository);
    }

    /**
     * resettet die Repositories
     */
    @AfterEach
    void tearDown() {
        Mockito.reset(jobRepository);
        Mockito.reset(studienfachRepository);
        Mockito.reset(jobKategorieRepository);
        Mockito.reset(personRepository);
        Mockito.reset(unternehmenRepository);
        Mockito.reset(ortRepository);
    }

    /**
     * testet saveJob() mit einem korrekten Job-Objekt
     */
    @Test
    @DisplayName("Testet, ob Jobs gespeichert werden")
    void saveJob() {
        // ********* Arrange *********
        Job expected = job1;
        when(ortRepository.findByPLZAndOrt(anyString(),anyString())).thenReturn(ort1);
        when(unternehmenRepository.findByName(anyString())).thenReturn(unternehmen1);
        when(personRepository.findByEmail(anyString())).thenReturn(unternehmensperson1);
        when(jobKategorieRepository.findByKategorie(anyString())).thenReturn(jobKategorie1);
        when(jobRepository.save(any(Job.class))).thenReturn(job1);

        // *********** Act ***********
        Job actual = jobService.saveJob(job1);

        // ********* Assert **********
        assertEquals(expected, actual);

        // ********* Verify **********
        verify(ortRepository, times(1)).findByPLZAndOrt(anyString(),anyString());
        verify(unternehmenRepository, times(1)).findByName(anyString());
        verify(personRepository, times(1)).findByEmail(anyString());
        verify(jobKategorieRepository, times(1)).findByKategorie(anyString());
        verify(jobRepository, times(1)).save(any(Job.class));
    }
}