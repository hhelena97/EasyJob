package de.hbrs.easyjob.services;

import de.hbrs.easyjob.controllers.registrieren.UnternehmenspersonRegistrierenController;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnternehmenServiceTest {
    // Repositories
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UnternehmenRepository unternehmenRepository;

    // Service
    private UnternehmenService unternehmenService;

    @BeforeEach
    void setUp() {
        // Service
        unternehmenService = new UnternehmenService(unternehmenRepository, ortRepository, jobRepository);
    }

    @AfterEach
    void tearDown() {
        // Service
        unternehmenService = null;
    }

    @Test
    @DisplayName("Finde ein existierendes Unternehmen mit Namen")
    void findExistingByNameTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen expected = unternehmenRepository.findByName("Kaffee-Pause");

        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.findByName("Kaffee-Pause");

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    @DisplayName("Finde ein nicht existierendes Unternehmen mit Namen (gebe null aus)")
    void findNotExistingByNameTest() {
        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.findByName("Katzenkörbchen GmbH");

        /* ****************************** ASSERT ***************************** */
        assertNull(actual);
    }

    @Test
    @DisplayName("speichere ein valides Unternehmen")
    void saveValidUnternehmenTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen expected = new Unternehmen();
        expected.setName("Katzenkörbchen GmbH");

        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.saveUnternehmen(expected);

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected.toString(), actual.toString());

        /* ***************************** TEAR DOWN *************************** */
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(expected.getId_Unternehmen()));
    }

    @Test
    @DisplayName("speichere ein nicht valides Unternehmen")
    void saveNonValidUnternehmenTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen unternehmen = new Unternehmen();

        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.saveUnternehmen(unternehmen);

        // -------------------------------------------------------------------------------------------------------------
        // sollte nicht möglich sein, ein "null"-Unternehmen zu speichern > diese Zeile löschen, wenn in
        // UnternehmenService gefixt
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen.getId_Unternehmen()));
        // -------------------------------------------------------------------------------------------------------------

        /* ****************************** ASSERT ***************************** */
        assertNull(actual);
    }

    @Test
    @DisplayName("finde existierendes Unternehmen mit ID")
    void findExistingByIDTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen expected = unternehmenRepository.findByName("Kaffee-Pause");

        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.findByID(expected.getId_Unternehmen());

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    @DisplayName("finde nicht-existierendes Unternehmen mit ID")
    void findNotExistingByIDTest() {
        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.findByID(10000);

        /* ****************************** ASSERT ***************************** */
        assertNull(actual);
    }

    @Test
    @DisplayName("Gebe aus, wie viele Jobs es zu einem existierenden Unternehmen gibt")
    void anzahlJobsVonExistierendenUnternehmenTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen unternehmen = unternehmenRepository.findByName("BioChemTech AG");

        /* ******************************* ACT ******************************* */
        int actual = unternehmenService.anzahlJobs(unternehmen);

        /* ****************************** ASSERT ***************************** */
        assertEquals(2, actual);
    }

    @Test
    @DisplayName("Gebe aus, wie viele Jobs es zu einem nicht existierenden Unternehmen gibt")
    void anzahlJobsVonNichtExistierendenUnternehmenTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen unternehmen = new Unternehmen();

        /* ******************************* ACT ******************************* */
        int actual = unternehmenService.anzahlJobs(unternehmen);

        /* ****************************** ASSERT ***************************** */
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Gebe den ersten Standort des Unternehmens aus")
    void getFirstStandortTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen unternehmen = unternehmenRepository.findByName("Kaffee-Pause");
        Ort expected = ortRepository.findByOrt("München");

        /* ******************************* ACT ******************************* */
        Ort actual = unternehmenService.getFirstStandort(unternehmen);

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Testet, ob Unternehmen mit nicht existierender Unternehmensperson gespeichert werden kann")
    void savenewUnternehmenNonExistingUnternehmenspersonTest() {
        /* ******************************** ARRANGE *********************************** */
        Unternehmen expected = new Unternehmen();
        expected.setName("Katzenkörbchen GmbH");
        expected.setStandorte(Set.of(ortRepository.findByPLZAndOrt("53115","Bonn")));

        Unternehmensperson unternehmensperson = new Unternehmensperson();
        unternehmensperson.setUnternehmen(expected);
        unternehmensperson.setEmail("laura.stern@planetder.affen");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Laura");
        unternehmensperson.setVorname("Stern");

        /* ******************************* ACT / ASSERT ******************************* */
        assertThrows(Exception.class, () -> unternehmenService.savenewUnternehmen(expected, unternehmensperson));
    }

    @Test
    @DisplayName("Testet, ob Unternehmen mit  existierender Unternehmensperson gespeichert werden kann")
    void savenewUnternehmenExistingUnternehmenspersonTest() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen expected = new Unternehmen();
        expected.setName("Katzenkörbchen GmbH");
        expected.setStandorte(Set.of(ortRepository.findByPLZAndOrt("53115","Bonn")));

        UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController = new UnternehmenspersonRegistrierenController(personRepository, unternehmenRepository);

        Unternehmensperson unternehmensperson = new Unternehmensperson();
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        unternehmenspersonRegistrierenController.createUnternehmenspersonWithoutCompany(unternehmensperson, true);

        /* ******************************* ACT ******************************* */
        Unternehmen actual = unternehmenService.savenewUnternehmen(expected, unternehmensperson);

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected.toString(), actual.toString());

        /* ***************************** TEAR DOWN *************************** */
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(expected.getId_Unternehmen()));
        personRepository.delete(personRepository.getReferenceById(unternehmensperson.getId_Person()));
    }

    @Test
    @DisplayName("Gibt alle Branchen aus")
    void getAllBranchen() {
        //TODO: Test schreiben
    }

    @Test
    @DisplayName("Gibt alle Jobs eines Unternehmens aus")
    void getAllJobs() {
        //TODO: Test schreiben
    }

    @Test
    @DisplayName("Testet die Methode getUnternehmensOrte")
    void getUnternehmensOrte() {
        /* ***************************** ARRANGE ***************************** */
        Unternehmen unternehmen = unternehmenRepository.findByName("Kaffee-Pause");
        String expected = "München 80315";

        /* ******************************* ACT ******************************* */
        String actual = unternehmenService.getUnternehmensOrte(unternehmen);

        /* ****************************** ASSERT ***************************** */
        assertEquals(expected, actual);
    }
}