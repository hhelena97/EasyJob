package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
@SpringBootTest
class StudentSearchServiceTest {

    // Repositories
    @Autowired
    private StudentRepository stuRepo;

    // Services
    private StudentSearchService stuSeSe;

    /**
     * erstellt den zu testenden Service
     */
    @BeforeEach
    void setUp() {
        stuSeSe = new StudentSearchService(stuRepo);
    }

    /**
     * nullt den zu testenden Service, damit er wieder neu erstellt werden kann pro Test
     */
    @AfterEach
    void tearDown() {
        stuSeSe = null;
    }

    /**
     * testet die Funktion vollTextSuche(String volltext) mit Volltext-Wörtern
     */
    @Test
    @DisplayName("Test für die Volltextsuche")
    @Transactional
    void vollTextSuche() throws Exception {
        // ************* Arrange *************
        Optional<Student>[] p = new Optional[8];
        Student[] stus = new Student[8];
        p[0] = stuRepo.findById(25);
        p[1] = stuRepo.findById(23);
        p[2] = stuRepo.findById(2);
        p[3] = stuRepo.findById(27);
        p[4] = stuRepo.findById(28);
        p[5] = stuRepo.findById(29);
        p[6] = stuRepo.findById(30);
        p[7] = stuRepo.findById(1);

        int i = 0;
        for (Optional<Student> stu : p) {
            if (stu.isEmpty()) {
                throw new Exception("Kein Objekt in der Datenbank gefunden!");
            } else {
                stus[i] = p[i].get();
                System.out.println(stus[i]);
            }
            i++;
        }
        List<Student> expected = List.of(stus);

        // *************** Act ***************
        List<Student> actual = stuSeSe.vollTextSuche("Bachelor");

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Funktion teilZeichenSuche(String teilZeichen) mit Teilzeichen
     */
    @Test
    @DisplayName("Test für die Teilzeichensuche")
    @Transactional
    void teilZeichenSuche() throws Exception {
        // ************* Arrange *************
        Optional<Student>[] p = new Optional[8];
        Student[] stus = new Student[8];
        p[0] = stuRepo.findById(1);
        p[1] = stuRepo.findById(2);
        p[2] = stuRepo.findById(23);
        p[3] = stuRepo.findById(25);
        p[4] = stuRepo.findById(26);
        p[5] = stuRepo.findById(27);
        p[6] = stuRepo.findById(28);
        p[7] = stuRepo.findById(30);

        int i = 0;
        for (Optional<Student> stu : p) {
            if (stu.isEmpty()) {
                throw new Exception("Kein Objekt in der Datenbank gefunden!");
            } else {
                stus[i] = p[i].get();
            }
            i++;
        }
        List<Student> expected = List.of(stus);

        // *************** Act ***************
        List<Student> actual = stuSeSe.teilZeichenSuche("Bon");

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Funktion istVollTextSuche(String keyword) mit ganzen Wörtern und Teilzeichen
     */
    @Test
    @DisplayName("Test für die Überprüfung, ob etwas Volltext ist")
    void istVollTextSuche() {
        // *************** Act ***************
        boolean actual  = stuSeSe.istVollTextSuche("Masterarbeit");
        boolean actual2 = stuSeSe.istVollTextSuche("M");
        boolean actual3 = stuSeSe.istVollTextSuche("Master");
        boolean actual4 = stuSeSe.istVollTextSuche("Inf");
        boolean actual5 = stuSeSe.istVollTextSuche("Informatik");
        boolean actual6 = stuSeSe.istVollTextSuche("Bo");
        boolean actual7 = stuSeSe.istVollTextSuche("Bonn");

        // ************* Assert **************
        assertTrue(actual);
        assertFalse(actual2);
        assertTrue(actual3);
        assertFalse(actual4);
        assertTrue(actual5);
        assertFalse(actual6);
        assertTrue(actual7);
    }
}