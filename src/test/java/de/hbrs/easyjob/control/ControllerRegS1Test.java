package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repository.StudienfachRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class ControllerRegS1Test {

    // Repositories
    private final static StudienfachRepository studienfachRepository = Mockito.mock(StudienfachRepository.class);

    // Controller
    @InjectMocks
    private ControllerRegS1 controllerRegS1;

    // Entities
    private static Studienfach fach1;
    private static Studienfach fach2;
    private static Studienfach fach3;

    /**
     * setzt die Daten zu den Studienfach-Objekten
     */
    @BeforeAll
    static void setUp() {
        fach1 = new Studienfach();
        fach1.setFach("Informatik");
        fach1.setAbschluss("Bachelor");

        fach2 = new Studienfach();
        fach2.setFach("Informatik");
        fach2.setAbschluss("Master");

        fach3 = new Studienfach();
        fach3.setFach("Mathematik");
        fach3.setAbschluss("Bachelor");
    }

    /**
     * erstellt den zu testenden Controller
     */
    @BeforeEach
    void setUpEach() {
        controllerRegS1 = new ControllerRegS1(studienfachRepository);
    }

    /**
     * nullt den zu testenden Controller, sodass pro Test eine neue Instanz von dem Controller aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        controllerRegS1 = null;
    }

    /**
     * testet die Funktion getStudienfachNachAbschluss()
     */
    @Test
    @DisplayName("Testet getStudienfachNachAbschluss()")
    void getStudienfachNachAbschlussTest() {
        // ******************** Arrange ********************
        Set<Studienfach> expected = Set.of(fach1, fach3);

        Mockito.when(studienfachRepository.findAllByAbschluss(anyString())).thenReturn(Set.of(fach1, fach3));

        // ********************** Act **********************
        Set<Studienfach> actual = controllerRegS1.getStudienfachNachAbschluss("Bachelor");

        // ******************** Assert *********************
        assertEquals(expected, actual);
    }
}