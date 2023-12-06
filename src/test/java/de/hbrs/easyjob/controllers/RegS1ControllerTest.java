package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class RegS1ControllerTest {

    // Repositories
    private final static StudienfachRepository studienfachRepository = Mockito.mock(StudienfachRepository.class);

    // Controller
    @InjectMocks
    private RegS1Controller regS1Controller;

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
        regS1Controller = new RegS1Controller(studienfachRepository);
    }

    /**
     * nullt den zu testenden Controller, sodass pro Test eine neue Instanz von dem Controller aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        regS1Controller = null;
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
        Set<Studienfach> actual = regS1Controller.getStudienfachNachAbschluss("Bachelor");

        // ******************** Assert *********************
        assertEquals(expected, actual);
    }
}