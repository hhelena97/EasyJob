package de.hbrs.easyjob.services;

import de.hbrs.easyjob.controllers.registrieren.UnternehmenspersonRegistrierenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UnternehmenspersonServiceTest {
    // Repositories
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UnternehmenRepository unternehmenRepository;
    @Autowired
    private BrancheRepository brancheRepository;

    // Service
    private UnternehmenspersonService unternehmenspersonService;

    // Entity
    private Unternehmensperson unternehmensperson;


    @BeforeEach
    void setUp() {
        // Controller
        UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController = new UnternehmenspersonRegistrierenController(personRepository, unternehmenRepository);

        // Services
        UnternehmenService unternehmenService = new UnternehmenService(unternehmenRepository, ortRepository, jobRepository, brancheRepository);
        unternehmenspersonService = new UnternehmenspersonService(unternehmenspersonRegistrierenController, unternehmenService);

        // Entity
        unternehmensperson = new Unternehmensperson();
    }

    @AfterEach
    void tearDown() {
        // Service
        unternehmenspersonService = null;

        // Entity
        unternehmensperson = null;
    }

    @Test
    @DisplayName("Testet Speichern einer validen Unternehmensperson")
    @Transactional
    void saveUnternehmenspersonTest() {
        /* ******************************* ARRANGE ******************************* */
        Unternehmen unternehmen = unternehmenRepository.findByName("Kaffee-Pause");
        unternehmensperson.setUnternehmen(unternehmen);
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        /* ********************************* ACT ********************************* */
        boolean actual = unternehmenspersonService.saveUnternehmensperson(unternehmensperson);

        /* ******************************** ASSERT ******************************* */
        assertTrue(actual);

        /* ****************************** TEAR DOWN ****************************** */
        personRepository.delete(personRepository.getReferenceById(unternehmensperson.getId_Person()));
    }

    @Test
    @DisplayName("Testet Speichern einer nicht validen Unternehmensperson")
    @Transactional
    void saveNotValidUnternehmenspersonTest() {
        /* ********************************* ACT ********************************* */
        boolean actual = unternehmenspersonService.saveUnternehmensperson(unternehmensperson);

        /* ******************************** ASSERT ******************************* */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet Speichern einer validen Unternehmensperson ohne gespeichertes (aber valides) Unternehmen")
    @Transactional
    void saveValidUnternehmenspersonNeuesUnternehmenTest() {
        /* ******************************* ARRANGE ******************************* */
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setName("Katzenkörbchen GmbH");
        unternehmen.setStandorte(Set.of(ortRepository.findByPLZAndOrt("53115","Bonn")));
        unternehmen.setBeschreibung("Dieses Unternehmen unterstützt Menschen in den Schwierigkeiten ihres Alltags, in dem es Katzentherapie praktiziert.");
        unternehmen.setBranchen(Set.of(brancheRepository.findById(3).orElseThrow(NullPointerException::new)));

        unternehmensperson.setUnternehmen(unternehmen);
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        /* ********************************* ACT ********************************* */
        boolean actual = unternehmenspersonService.saveUnternehmensperson(unternehmensperson);

        /* ******************************** ASSERT ******************************* */
        assertTrue(actual);

        /* ****************************** TEAR DOWN ****************************** */
        personRepository.delete(personRepository.getReferenceById(unternehmensperson.getId_Person()));
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen.getId_Unternehmen()));
    }

    @Test
    @DisplayName("Testet Speichern einer validen Unternehmensperson mit nicht validem Unternehmen")
    @Transactional
    void saveValidUnternehmenspersonNotValidUnternehmenTest() {
        /* ******************************* ARRANGE ******************************* */
        Unternehmen unternehmen = new Unternehmen();

        unternehmensperson.setUnternehmen(unternehmen);
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        /* ********************************* ACT ********************************* */
        boolean actual = unternehmenspersonService.saveUnternehmensperson(unternehmensperson);

        /* ******************************** ASSERT ******************************* */
        assertFalse(actual);
    }
}