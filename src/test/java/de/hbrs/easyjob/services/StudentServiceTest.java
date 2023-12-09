package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceTest {
    // Repositories
    private final static OrtRepository ortRepo = mock(OrtRepository.class);
    private final static JobKategorieRepository joKaRepo = mock(JobKategorieRepository.class);
    private final static StudienfachRepository stuFaRepo = mock(StudienfachRepository.class);
    private final static StudentRepository stuRepo = mock(StudentRepository.class);

    // Services
    private StudentService stuSe;

    // Datenobjekte
    private static Student p1;
    private final static Studienfach stuFa1 = new Studienfach();
    private final static JobKategorie joKa1 = new JobKategorie();
    private final static Ort ort1 = new Ort();

    /**
     * setzt Daten für das Studentenobjekt an
     */
    @BeforeAll
    static void setUp() {
        // Daten fuer Student aufsetzen
        joKa1.setKategorie("Masterarbeit");
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        stuFa1.setAbschluss("Master");
        stuFa1.setFach("Informatik");
        Set<JobKategorie> tmp = new HashSet<>();
        tmp.add(joKa1);
        Set<Ort> tmp2 = new HashSet<>();
        tmp2.add(ort1);

        // Daten in Student einsetzen
        p1 = new Student();
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p1.setStudienfach(stuFa1);
        p1.setJobKategorien(tmp);
        p1.setOrte(tmp2);
    }

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUpEach() {
        stuSe = new StudentService(stuRepo, stuFaRepo, joKaRepo, ortRepo);
    }

    /**
     * resettet die gemockten Repositorys
     */
    @AfterAll
    static void tearDown() {
        Mockito.reset(stuFaRepo);
        Mockito.reset(joKaRepo);
        Mockito.reset(stuRepo);
        Mockito.reset(ortRepo);
    }

    /**
     * testet die saveJob()-Methode, in die eine Person übergeben wird
     */
    @Test
    @DisplayName("Testet, ob JobsUebersichtView gespeichert werden")
    void saveStudentTest() {
        // *** Arrange ***
        Student expected = p1;
        when(ortRepo.findByPLZAndOrt(anyString(),anyString())).thenReturn(ort1);
        when(joKaRepo.findByKategorie(anyString())).thenReturn(joKa1);
        when(stuFaRepo.findByFachAndAbschluss(anyString(), anyString())).thenReturn(stuFa1);
        when(stuRepo.save(any(Student.class))).thenReturn(p1);

        // **** Act ****
        Student actual = stuSe.saveStudent(p1);

        // *** Assert ****
        assertEquals(expected, actual);

        // *** Verify ****
        verify(ortRepo, times(1)).findByPLZAndOrt(anyString(),anyString());
        verify(joKaRepo, times(1)).findByKategorie(anyString());
        verify(stuFaRepo, times(1)).findByFachAndAbschluss(anyString(),anyString());
        verify(stuRepo, times(1)).save(any(Student.class));
    }

    // TODO: andere Methoden testen
}