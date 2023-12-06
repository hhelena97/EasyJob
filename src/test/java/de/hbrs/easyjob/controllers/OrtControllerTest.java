package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.services.OrtService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrtControllerTest {

    // Services
    private final static OrtService ortService = Mockito.mock(OrtService.class);

    // Controller
    @InjectMocks
    private OrtController ortController;

    // Datenobjekte
    private static Ort ort1;
    private static Ort ort2;
    private static Ort ort3;

    /**
     * setzt die Werte für die Ort-Objekte
     */
    @BeforeAll
    static void setUp() {
        ort1 = new Ort();
        ort1.setOrt("Bonn");
        ort1.setPLZ("53111");

        ort2 = new Ort();
        ort2.setOrt("Köln");
        ort2.setPLZ("50667");

        ort3 = new Ort();
        ort3.setOrt("Bonn");
        ort3.setPLZ("53119");

        ortService.saveOrt(ort1);
        ortService.saveOrt(ort2);
        ortService.saveOrt(ort3);

    }

    /**
     * erstellt den zu testenden Controller
     */
    @BeforeEach
    void setUpEach() {
        ortController = new OrtController(ortService);
    }

    /**
     * nullt den zu testenden Controller, damit pro Test eine neue Controller-Instanz aufgerufen werden kann
     */
    @AfterEach
    void tearDown() {
        ortController = null;
    }

    //TODO: getOrtItemFilter()-Test
    /*
     * testet die Methode getOrtItemFilter()
     *
    @Test
    @DisplayName("Test für getOrtItemFilter()")
    void getOrtItemFilterTest() {

    }
     */

    /**
     * testet die Methode getAlleOrte() mit einer ungeordneten Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte()")
    void getAlleOrteTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        expected.add(ort1);
        expected.add(ort3);
        expected.add(ort2);

        List<Ort> unsorted = new ArrayList<>();
        unsorted.add(ort1);
        unsorted.add(ort2);
        unsorted.add(ort3);

        Mockito.when(ortService.getAlleOrte()).thenReturn(unsorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getAlleOrte() mit einer leeren Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte() mit leerer Liste")
    void getAlleOrteEmptyListTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        List<Ort> unsorted = new ArrayList<>();

        Mockito.when(ortService.getAlleOrte()).thenReturn(unsorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode getAlleOrte() mit einer geordneten Liste
     */
    @Test
    @DisplayName("Test für getAlleOrte() mit geordneter Liste")
    void getAlleOrteSortedListTest() {
        // ************* Arrange *************
        List<Ort> expected = new ArrayList<>();
        expected.add(ort1);
        expected.add(ort3);
        expected.add(ort2);

        List<Ort> sorted = new ArrayList<>();
        sorted.add(ort1);
        sorted.add(ort3);
        sorted.add(ort2);

        Mockito.when(ortService.getAlleOrte()).thenReturn(sorted);

        // *************** Act ***************
        List<Ort> actual = ortController.getAlleOrte();

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    //TODO: Test für getOrtItemLabelGenerator
    /*
     * testet die Methode getOrtItemLabelGenerator
     *
    @Test
    @DisplayName("Testet getOrtItemLabelGeneratorTest")
    void getOrtItemLabelGeneratorTest() {

    }
     */

    /**
     * testet die Methode createOrt() mit einem Ort-Objekt
     */
    @Test
    @DisplayName("Testet die Methode createOrt")
    void createOrtTest() {
        // ************* Arrange *************
        ResponseEntity<Ort> expected = new ResponseEntity<>(ort1, HttpStatus.CREATED);

        Mockito.when(ortService.saveOrt(ort1)).thenReturn(ort1);

        // *************** Act ***************
        ResponseEntity<Ort> actual = ortController.createOrt(ort1);

        // ************* Assert **************
        assertEquals(expected, actual);
    }

    /**
     * testet die Methode createOrt() mit null
     */
    @Test
    @DisplayName("Testet die Methode createOrt")
    void createNullOrtTest() {
        // ************* Arrange *************
        ResponseEntity<Ort> expected = new ResponseEntity<>(null, HttpStatus.CREATED);

        Mockito.when(ortService.saveOrt(ort1)).thenReturn(null);

        // *************** Act ***************
        ResponseEntity<Ort> actual = ortController.createOrt(ort1);

        // ************* Assert **************
        assertEquals(expected, actual);
    }
}