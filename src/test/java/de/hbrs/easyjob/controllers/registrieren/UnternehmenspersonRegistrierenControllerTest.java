package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.controllers.UnternehmenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.services.UnternehmenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UnternehmenspersonRegistrierenControllerTest {
    // Repositories
    @Autowired
    private UnternehmenRepository unternehmenRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private JobRepository jobRepository;

    // Controllers
    private UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController;
    private UnternehmenController unternehmenController;

    // Entities
    private Unternehmensperson unternehmensperson;
    private Unternehmen unternehmen;

    @BeforeEach
    void setUp() {
        // Service
        UnternehmenService unternehmenService = new UnternehmenService(unternehmenRepository, ortRepository, jobRepository);

        // Controllers
        unternehmenspersonRegistrierenController = new UnternehmenspersonRegistrierenController(personRepository, unternehmenRepository);
        unternehmenController = new UnternehmenController(unternehmenService);

        // Entities
        unternehmen = unternehmenRepository.findByName("Kaffee-Pause");
        unternehmensperson = new Unternehmensperson();
    }

    @AfterEach
    void tearDown() {
        // Controllers
        unternehmenspersonRegistrierenController = null;
        unternehmenController = null;

        // Entities
        unternehmen = null;
        unternehmensperson = null;
    }

    @Test
    @DisplayName("Eine normale Unternehmensperson (AGBs akzeptiert) von einem in der Datenbank existierenden Unternehmen registrieren")
    void createUnternehmenspersonWithCompanyTest() {
        /* ****************** ARRANGE ****************** */
        unternehmensperson.setUnternehmen(unternehmen);
        unternehmensperson.setEmail("laura.stern@planetder.affen");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Laura");
        unternehmensperson.setVorname("Stern");

        /* ******************** ACT ******************** */
        boolean actual = unternehmenspersonRegistrierenController.createUnternehmenspersonWithCompany(unternehmensperson, true);

        /* ****************** ASSERT ******************* */
        assertTrue(actual);

        /* ***************** TEAR DOWN ***************** */
        personRepository.delete(personRepository.getReferenceById(unternehmensperson.getId_Person()));
    }

    @Test
    @DisplayName("Eine normale Unternehmensperson (AGBs nicht akzeptiert) von einem in der Datenbank existierenden Unternehmen registrieren")
    void createUnternehmenspersonWithCompanyNoAGBSTest() {
        /* ****************** ARRANGE ****************** */
        unternehmensperson.setUnternehmen(unternehmen);
        unternehmensperson.setEmail("laura.stern@planetder.affen");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Laura");
        unternehmensperson.setVorname("Stern");

        /* ******************** ACT ******************** */
        boolean actual = unternehmenspersonRegistrierenController.createUnternehmenspersonWithCompany(unternehmensperson, false);

        /* ****************** ASSERT ******************* */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Eine normale Unternehmensperson (AGBs akzeptiert) von einem in der Datenbank nicht existierenden Unternehmen registrieren")
    void createUnternehmenspersonWithoutCompanyTest() {
        /* ****************** ARRANGE ****************** */
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        /* ******************** ACT ******************** */
        boolean actual = unternehmenspersonRegistrierenController.createUnternehmenspersonWithoutCompany(unternehmensperson, true);

        /* ****************** ASSERT ******************* */
        assertTrue(actual);

        /* ***************** TEAR DOWN ***************** */
        personRepository.delete(personRepository.getReferenceById(unternehmensperson.getId_Person()));
    }

    @Test
    @DisplayName("Eine normale Unternehmensperson (AGBs nicht akzeptiert) von einem in der Datenbank nicht existierenden Unternehmen registrieren")
    void createUnternehmenspersonWithoutCompanyNoAGBSTest() {
        /* ****************** ARRANGE ****************** */
        unternehmensperson.setEmail("lara.croft@adventure.org");
        unternehmensperson.setPasswort("Test123!");
        unternehmensperson.setNachname("Lara");
        unternehmensperson.setVorname("Croft");

        /* ******************** ACT ******************** */
        boolean actual = unternehmenspersonRegistrierenController.createUnternehmenspersonWithoutCompany(unternehmensperson, false);

        /* ****************** ASSERT ******************* */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Test zu isValidUnternehmen() (sowohl gültiges als auch nicht gültiges Unternehmen)")
    void isValidUnternehmenTest() {
        /* ****************** ARRANGE ****************** */
        Unternehmen unternehmen2 = new Unternehmen();

        Unternehmen unternehmen3 = new Unternehmen();
        unternehmen3.setName("Katzenkörbchen GmbH");

        Unternehmen unternehmen4 = new Unternehmen();
        unternehmen4.setName("Dreamland GmbH");
        unternehmenController.createUnternehmen(unternehmen4);

        /* ******************** ACT ******************** */
        boolean actual = unternehmenspersonRegistrierenController.isValidUnternehmen(unternehmen);
        boolean actual2 = unternehmenspersonRegistrierenController.isValidUnternehmen(unternehmen2);
        boolean actual3 = unternehmenspersonRegistrierenController.isValidUnternehmen(unternehmen3);
        boolean actual4 = unternehmenspersonRegistrierenController.isValidUnternehmen(unternehmen4);

        /* ****************** ASSERT ******************* */
        assertTrue(actual);  // in unternehmenRepository gespeichert
        assertFalse(actual2);  // nicht gespeichert
        assertFalse(actual3);  // zwar mit Namen, aber nicht in unternehmenRepository gespeichert
        assertTrue(actual4);  // in unternehmenRepository gespeichert und deshalb gültig

        /* ***************** TEAR DOWN ***************** */
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen4.getId_Unternehmen()));
    }
}