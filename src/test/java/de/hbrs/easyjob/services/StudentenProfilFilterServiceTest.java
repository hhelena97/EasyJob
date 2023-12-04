package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.StudentRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.junit.jupiter.api.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@SpringBootTest
class StudentenProfilFilterServiceTest {

    // Repositories
    private final static OrtRepository ortRepo = mock(OrtRepository.class);
    private final static JobKategorieRepository joKaRepo = mock(JobKategorieRepository.class);
    private final static StudienfachRepository stuFaRepo = mock(StudienfachRepository.class);
    private final static StudentRepository stuRepo = mock(StudentRepository.class);

    // Services
    @InjectMocks
    private StudentenProfilFilterService stuFiS;
    private static StudentService stuSe;

    // Datenobjekte
    private static Student p1;
    private static Student p2;
    private final static Studienfach stuFa1 = new Studienfach();
    private final static JobKategorie joKa1 = new JobKategorie();
    private final static Ort ort1 = new Ort();
    private final static Studienfach stuFa2 = new Studienfach();
    private final static JobKategorie joKa2 = new JobKategorie();
    private final static Ort ort2 = new Ort();

    /**
     * setzt die Datenobjekte, die im späteren Verlauf des Tests verwendet werden, auf
     */
    @BeforeAll
    static void setUp() {
        // Daten für Studenten aufsetzen
        joKa1.setKategorie("Masterarbeit");
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        stuFa1.setAbschluss("Master");
        stuFa1.setFach("Informatik");
        Set<JobKategorie> tmp = new HashSet<>();
        tmp.add(joKa1);
        Set<Ort> tmp2 = new HashSet<>();
        tmp2.add(ort1);

        joKa2.setKategorie("Praktikum");
        ort2.setPLZ("50667");
        ort2.setOrt("Köln");
        stuFa2.setAbschluss("Bachelor");
        stuFa2.setFach("Informatik");
        Set<JobKategorie> tmp3 = new HashSet<>();
        tmp3.add(joKa1);
        tmp3.add(joKa2);
        Set<Ort> tmp4 = new HashSet<>();
        tmp4.add(ort2);

        // Daten in Studenten einsetzen
        p1 = new Student();
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p1.setStudienfach(stuFa1);
        p1.setJobKategorien(tmp);
        p1.setOrte(tmp2);

        p2 = new Student();
        p2.setVorname("Mareike");
        p2.setNachname("Musterfrau");
        p2.setEmail("mareike.musterfrau@example.de");
        p2.setStudienfach(stuFa1);
        p2.setJobKategorien(tmp3);
        p2.setOrte(tmp4);

        // Studenten speichern
        stuSe = new StudentService(stuRepo, stuFaRepo, joKaRepo, ortRepo);
        stuSe.saveStudent(p1);
        stuSe.saveStudent(p2);
    }

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUpEach() {
        stuFiS = new StudentenProfilFilterService();
        stuFiS.studentRepository = stuRepo;
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
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für den Fall,
     * dass keine Filterangaben gemacht werden. Da sollten dann alle vorhandenen Datensätze ausgegeben werden.
     */
    @Test
    @DisplayName("Test nach Studentensuche, wenn keine Filterangaben gemacht sind")
    void filterStudentsWithoutFilter() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1, p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(null,null, null);
        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nur nach Studienfächern filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über nur Studienfächer")
    void filterStudentsAfterStudienfaecher() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1, p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(null,null, Set.of(stuFa1));
        List<Student> actual2 = stuFiS.filterStudents(null,null, Set.of(stuFa1,stuFa2));
        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected, actual2);

        // ************* Arrange ************* 2
        when(stuRepo.findAll(any(Specification.class))).thenReturn(List.of());
        // *************** Act *************** 2
        List<Student> actual3 = stuFiS.filterStudents(null,null, Set.of(stuFa2));
        // ************* Assert ************** 2
        assertEquals(0, actual3.size());
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nur nach JobKategorien filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über nur JobKategorien")
    void filterStudentsAfterJobKategorie() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1, p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(null,Set.of(joKa1), null);
        List<Student> actual2 = stuFiS.filterStudents(null,Set.of(joKa1, joKa2), null);
        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected, actual2);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nur nach Orten filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über nur Orte")
    void filterStudentsAfterLocation() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(ort1,null, null);
        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nach Studienfächern und JobKategorien filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über Studienfächer & JobKategorien")
    void filterStudentsAfterStudienFaehcerUndJobKategorie() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(null,Set.of(joKa1), Set.of(stuFa1));
        List<Student> actual2 = stuFiS.filterStudents(null,Set.of(joKa1, joKa2), Set.of(stuFa1));
        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected, actual2);

        // ************* Arrange *************
        List<Student> expected2 = List.of(p1, p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual3 = stuFiS.filterStudents(null,Set.of(joKa1, joKa2), Set.of(stuFa1, stuFa2));
        // ************* Assert **************
        assertEquals(expected2, actual3);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nach Studienfächern und Orten filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über Studienfächer & Orte")
    void filterStudentsAfterLocationUndStudienFaecher() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(ort1,Set.of(joKa1), null);
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        List<Student> expected2 = List.of(p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual2 = stuFiS.filterStudents(ort2,Set.of(joKa1, joKa2), null);
        // ************* Assert **************
        assertEquals(expected, actual2);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nach Orten und JobKategorien filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über Orte & JobKategorien")
    void filterStudentsAfterLocationUndJobKategorie() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(ort1,null, Set.of(stuFa1, stuFa2));
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        when(stuRepo.findAll(any(Specification.class))).thenReturn(List.of());
        // *************** Act ***************
        List<Student> actual2 = stuFiS.filterStudents(ort2,null, Set.of(stuFa2));
        // ************* Assert **************
        assertEquals(0, actual2.size());
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für dann Fall,
     * dass die Funktion nach allen drei auszuwählenden Parametern filtern soll
     */
    @Test
    @DisplayName("Test nach Studentensuche über Studienfächer & JobKategorien & Orte")
    void filterStudents() {
        // ************* Arrange *************
        List<Student> expected = List.of(p1);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = stuFiS.filterStudents(ort1,Set.of(joKa1), Set.of(stuFa1, stuFa2));
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        List<Student> expected2 = List.of(p2);
        when(stuRepo.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual2 = stuFiS.filterStudents(ort2,Set.of(joKa1, joKa2), Set.of(stuFa1));
        // ************* Assert **************
        assertEquals(expected2, actual2);
    }

}