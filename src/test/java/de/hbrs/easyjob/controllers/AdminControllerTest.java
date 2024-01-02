package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminControllerTest {
    // Repository
    @Autowired
    private PersonRepository personRepository;

    // Controller
    private AdminController adminController;

    // Entity
    private Admin admin;

    @BeforeEach
    void setUp() {
        adminController = new AdminController(personRepository);
        admin = new Admin();
    }

    @AfterEach
    void tearDown() {
        adminController = null;
        admin = null;
    }

    @Test
    @DisplayName("Testet, ob ein valider Admin angelegt werden kann")
    void createValidAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("tollervalideradmin@yeah.de");

        /* ************************ ACT ************************ */
        boolean actual = adminController.createAdmin(admin);

        /* *********************** ASSERT ********************** */
        assertTrue(actual);

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
    }

    @Test
    @DisplayName("Testet, ob ein nicht valider Admin angelegt werden kann")
    void createNichtValidAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setEmail("nichtvalideradmin.de");

        /* ************************ ACT ************************ */
        boolean actual = adminController.createAdmin(admin);

        /* *********************** ASSERT ********************** */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet die Deaktivierung eines Admins")
    void deleteAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("supertolleradmin@yeah.de");
        adminController.createAdmin(admin);

        /* ************************ ACT ************************ */
        boolean actual = adminController.deleteAdmin(admin);

        /* *********************** ASSERT ********************** */
        assertTrue(actual);
        assertFalse(admin.getAktiv());

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
    }

    @Test
    @DisplayName("Testet die Deaktivierung für nicht-existierenden Admin")
    void deleteNotExistingAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("schlechternichtexistierenderadmin@yeah.de");

        /* ************************ ACT ************************ */
        boolean actual = adminController.deleteAdmin(admin);

        // -------------------------------------------------------------------------------------------------------------
        // Es sollte kein Admin angelegt werden, sondern einfach nur false ausgegeben werden.
        // wenn gefixt, kann dieser Teil hier gelöscht werden
        //personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
        // -------------------------------------------------------------------------------------------------------------

        /* *********************** ASSERT ********************** */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet, ob neues Passwort angelegt wurde für einen existierenden Admin")
    void newPasswordExistingAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("tolleradmin@yeah.de");
        adminController.createAdmin(admin);

        String notExpected = admin.getPasswort();

        /* ************************ ACT ************************ */
        boolean actual = adminController.newPasswordAdmin(admin, "Ilikecats123!");

        /* *********************** ASSERT ********************** */
        assertTrue(actual);
        assertNotEquals(notExpected, admin.getPasswort());

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
    }

    @Test
    @DisplayName("Testet, ob neues Passwort angelegt wurde für einen nicht existierenden Admin")
    void newPasswordNotExistingAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("tolleradmin@yeah.de");

        /* ************************ ACT ************************ */
        boolean actual = adminController.newPasswordAdmin(admin, "Ilikecats123!");

        // -------------------------------------------------------------------------------------------------------------
        // Es sollte kein Admin angelegt werden, sondern einfach nur false ausgegeben werden.
        // wenn gefixt, kann dieser Teil hier gelöscht werden
        //personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
        // -------------------------------------------------------------------------------------------------------------

        /* *********************** ASSERT ********************** */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet, ob neues nicht valides Passwort angelegt wurde für einen existierenden Admin")
    void newInvalidPasswordExistingAdminTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("tolleradmin@yeah.de");
        adminController.createAdmin(admin);

        String expected = admin.getPasswort();

        /* ************************ ACT ************************ */
        boolean actual = adminController.newPasswordAdmin(admin, "l");

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));

        /* *********************** ASSERT ********************** */
        assertEquals(expected, admin.getPasswort());
        assertFalse(actual);  // false, weil nichts geändert wurde, da Passwort invalid
    }
}