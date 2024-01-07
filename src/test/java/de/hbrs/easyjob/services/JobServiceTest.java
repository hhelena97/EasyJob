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
    private final static OrtRepository ortRepo = mock(OrtRepository.class);
    private final static JobKategorieRepository joKaRepo = mock(JobKategorieRepository.class);
    private final static StudienfachRepository stuFaRepo = mock(StudienfachRepository.class);
    private final static PersonRepository persRepo = mock(PersonRepository.class);
    private final static UnternehmenRepository unterRepo = mock(UnternehmenRepository.class);
    private final static JobRepository jobRepo = mock(JobRepository.class);

    // Services
    private JobService joSe;

    // Entities
    private static Job job1;
    private final static Studienfach stuFa1 = new Studienfach();
    private final static JobKategorie joKa1 = new JobKategorie();
    private final static Ort ort1 = new Ort();
    private final static Unternehmen u1 = new Unternehmen();
    private final static Unternehmensperson p1 = new Unternehmensperson();

    /**
     * setzt die Daten für das Job-Objekt auf
     */
    @BeforeAll
    static void setUp() {
        // Daten für Job aufsetzen
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        u1.setUnternehmensperson(p1);
        u1.setName("EasyQube");
        joKa1.setKategorie("Masterarbeit");
        Set<Studienfach> tmp = new HashSet<>();
        tmp.add(stuFa1);

        // Daten in Job einsetzen
        job1 = new Job();
        job1.setUnternehmen(u1);
        job1.setPerson(p1);
        job1.setOrt(ort1);
        job1.setJobKategorie(joKa1);
        job1.setStudienfacher(tmp);
    }

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUpEach() {
        joSe = new JobService(jobRepo, stuFaRepo, joKaRepo, persRepo, unterRepo, ortRepo);
    }

    /**
     * resettet die Repositories
     */
    @AfterEach
    void tearDown() {
        Mockito.reset(jobRepo);
        Mockito.reset(stuFaRepo);
        Mockito.reset(joKaRepo);
        Mockito.reset(persRepo);
        Mockito.reset(unterRepo);
        Mockito.reset(ortRepo);
    }

    /**
     * testet saveJob() mit einem korrekten Job-Objekt
     */
    @Test
    @DisplayName("Testet, ob JobsUebersichtView gespeichert werden")
    void saveJob() {
        // ********* Arrange *********
        Job expected = job1;
        when(ortRepo.findByPLZAndOrt(anyString(),anyString())).thenReturn(ort1);
        when(unterRepo.findByName(anyString())).thenReturn(u1);
        when(persRepo.findByEmail(anyString())).thenReturn(p1);
        when(joKaRepo.findByKategorie(anyString())).thenReturn(joKa1);
        when(jobRepo.save(any(Job.class))).thenReturn(job1);

        // *********** Act ***********
        Job actual = joSe.saveJob(job1);

        // ********* Assert **********
        assertEquals(expected, actual);

        // ********* Verify **********
        verify(ortRepo, times(1)).findByPLZAndOrt(anyString(),anyString());
        verify(unterRepo, times(1)).findByName(anyString());
        verify(persRepo, times(1)).findByEmail(anyString());
        verify(joKaRepo, times(1)).findByKategorie(anyString());
        verify(jobRepo, times(1)).save(any(Job.class));
    }
}