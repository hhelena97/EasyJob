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
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@SpringBootTest
class StudentProfilFilterServiceTest {
    // Repositories
    private final static OrtRepository ortRepository = mock(OrtRepository.class);
    private final static JobKategorieRepository jobKategorieRepository = mock(JobKategorieRepository.class);
    private final static StudienfachRepository studienfachRepository = mock(StudienfachRepository.class);
    private final static StudentRepository studentRepository = mock(StudentRepository.class);

    // Services
    @InjectMocks
    private StudentProfilFilterService studentProfilFilterService;

    // Entities
    private static Student student1;
    private static Student student2;
    private final static Studienfach studienfach1 = new Studienfach();
    private final static JobKategorie jobKategorie1 = new JobKategorie();
    private final static Ort ort1 = new Ort();
    private final static Studienfach studienfach2 = new Studienfach();
    private final static JobKategorie jobKategorie2 = new JobKategorie();
    private final static Ort ort2 = new Ort();

    /**
     * setzt die Datenobjekte, die im späteren Verlauf des Tests verwendet werden, auf
     */
    @BeforeAll
    static void setUp() {
        // Daten für Studenten setzen
        jobKategorie1.setKategorie("Masterarbeit");
        ort1.setPLZ("53111");
        ort1.setOrt("Bonn");
        studienfach1.setAbschluss("Master");
        studienfach1.setFach("Informatik");
        Set<JobKategorie> tmp = new HashSet<>();
        tmp.add(jobKategorie1);
        Set<Ort> tmp2 = new HashSet<>();
        tmp2.add(ort1);

        jobKategorie2.setKategorie("Praktikum");
        ort2.setPLZ("50667");
        ort2.setOrt("Köln");
        studienfach2.setAbschluss("Bachelor");
        studienfach2.setFach("Informatik");
        Set<JobKategorie> tmp3 = new HashSet<>();
        tmp3.add(jobKategorie1);
        tmp3.add(jobKategorie2);
        Set<Ort> tmp4 = new HashSet<>();
        tmp4.add(ort2);

        // Daten in Studenten einsetzen
        student1 = new Student();
        student1.setVorname("Max");
        student1.setNachname("Mustermann");
        student1.setEmail("max.mustermann@example.de");
        student1.setStudienfach(studienfach1);
        student1.setJobKategorien(tmp);
        student1.setOrte(tmp2);

        student2 = new Student();
        student2.setVorname("Mareike");
        student2.setNachname("Musterfrau");
        student2.setEmail("mareike.musterfrau@example.de");
        student2.setStudienfach(studienfach1);
        student2.setJobKategorien(tmp3);
        student2.setOrte(tmp4);

        // Studenten speichern
        StudentService studentService = new StudentService(studentRepository, studienfachRepository, jobKategorieRepository, ortRepository);
        studentService.saveStudent(student1);
        studentService.saveStudent(student2);
    }

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUpEach() {
        studentProfilFilterService = new StudentProfilFilterService();
        studentProfilFilterService.studentRepository = studentRepository;
    }

    /**
     * resettet die gemockten Repositorys
     */
    @AfterAll
    static void tearDown() {
        Mockito.reset(studienfachRepository);
        Mockito.reset(jobKategorieRepository);
        Mockito.reset(studentRepository);
        Mockito.reset(ortRepository);
    }

    /**
     * testet filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) für den Fall,
     * dass keine Filterangaben gemacht werden. Da sollten dann alle vorhandenen Datensätze ausgegeben werden.
     */
    @Test
    @DisplayName("Test nach Studentensuche, wenn keine Filterangaben gemacht sind")
    void filterStudentsWithoutFilter() {
        // ************* Arrange *************
        List<Student> expected = List.of(student1, student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(null,null, null);
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
        List<Student> expected = List.of(student1, student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(null,null, Set.of(studienfach1));
        List<Student> actual2 = studentProfilFilterService.filterStudents(null,null, Set.of(studienfach1, studienfach2));
        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected, actual2);

        // ************* Arrange ************* 2
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of());
        // *************** Act *************** 2
        List<Student> actual3 = studentProfilFilterService.filterStudents(null,null, Set.of(studienfach2));
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
        List<Student> expected = List.of(student1, student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(null,Set.of(jobKategorie1), null);
        List<Student> actual2 = studentProfilFilterService.filterStudents(null,Set.of(jobKategorie1, jobKategorie2), null);
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
        List<Student> expected = List.of(student1);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(ort1,null, null);
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
        List<Student> expected = List.of(student1);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(null,Set.of(jobKategorie1), Set.of(studienfach1));
        List<Student> actual2 = studentProfilFilterService.filterStudents(null,Set.of(jobKategorie1, jobKategorie2), Set.of(studienfach1));
        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected, actual2);

        // ************* Arrange *************
        List<Student> expected2 = List.of(student1, student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual3 = studentProfilFilterService.filterStudents(null,Set.of(jobKategorie1, jobKategorie2), Set.of(studienfach1, studienfach2));
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
        List<Student> expected = List.of(student1);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(ort1,Set.of(jobKategorie1), null);
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        List<Student> expected2 = List.of(student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual2 = studentProfilFilterService.filterStudents(ort2,Set.of(jobKategorie1, jobKategorie2), null);
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
        List<Student> expected = List.of(student1);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(ort1,null, Set.of(studienfach1, studienfach2));
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of());
        // *************** Act ***************
        List<Student> actual2 = studentProfilFilterService.filterStudents(ort2,null, Set.of(studienfach2));
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
        List<Student> expected = List.of(student1);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected);
        // *************** Act ***************
        List<Student> actual = studentProfilFilterService.filterStudents(ort1,Set.of(jobKategorie1), Set.of(studienfach1, studienfach2));
        // ************* Assert **************
        assertEquals(expected, actual);

        // ************* Arrange *************
        List<Student> expected2 = List.of(student2);
        when(studentRepository.findAll(any(Specification.class))).thenReturn(expected2);
        // *************** Act ***************
        List<Student> actual2 = studentProfilFilterService.filterStudents(ort2,Set.of(jobKategorie1, jobKategorie2), Set.of(studienfach1));
        // ************* Assert **************
        assertEquals(expected2, actual2);
    }

}