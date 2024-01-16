package de.hbrs.easyjob.controllers;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
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

    // Controllers
    @InjectMocks
    private OrtController ortController;

    // Entites
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

    @Test
    @DisplayName("Testet getOrtItemLabelGenerator")
    void getOrtItemLabelGeneratorTest() {
        // ************* Arrange *************
        ItemLabelGenerator<Ort> generator = ortController.getOrtItemLabelGenerator();
        String expected = "Bonn (53111)";
        String expected2 = "Köln (50667)";
        String expected3 = "Bonn (53119)";

        // *************** Act ***************
        String actual = generator.apply(ort1);
        String actual2 = generator.apply(ort2);
        String actual3 = generator.apply(ort3);

        // ************* Assert **************
        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }

    @Test
    @DisplayName("Testet getOrtItemFilter")
    void getOrtItemFilterTest() {
        // ************* Arrange *************
        ComboBox.ItemFilter<Ort> filter = ortController.getOrtItemFilter();

        // *************** Act ***************
        boolean actual = filter.test(ort1, "Bonn");
        boolean actual2 = filter.test(ort1, "Bon");
        boolean actual3 = filter.test(ort1, "Bo");
        boolean actual4 = filter.test(ort1, "B");
        boolean actual5 = filter.test(ort1, "Ber");

        // ************* Assert **************
        assertTrue(actual);
        assertTrue(actual2);
        assertTrue(actual3);
        assertTrue(actual4);
        assertFalse(actual5);
    }


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