package de.hbrs.easyjob.controllers.registrieren;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UnternehmenspersonRegistrierenControllerTest {
    // Repositories
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UnternehmenRepository unternehmenRepository;

    // Controllers
    private UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController;

    // Entities
    private Unternehmen unternehmen;
    private Unternehmensperson unternehmensperson;

    @BeforeEach
    void setUp() {
        // Controllers
        unternehmenspersonRegistrierenController = new UnternehmenspersonRegistrierenController(personRepository, unternehmenRepository);

        // Entities
        unternehmen = unternehmenRepository.findByName("Kaffee-Pause");
        unternehmensperson = new Unternehmensperson();
    }

    @AfterEach
    void tearDown() {
        // Controllers
        unternehmenspersonRegistrierenController = null;

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

        Unternehmen unternehmen4 = unternehmenRepository.findById(12).orElseThrow(NullPointerException::new);

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
    }
}