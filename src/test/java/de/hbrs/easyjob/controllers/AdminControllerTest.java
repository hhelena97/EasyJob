package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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

    @Test
    @DisplayName("Testet changePassword() von einer existierenden Person")
    @Transactional
    void changePasswordOfExistingPersonTest() {
        /* ********************** ARRANGE ********************** */
        Student p1 = new Student();
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p1.setPasswort("ichbimsDerGr0sst1!!!");
        personRepository.save(p1);
        String old = personRepository.findByEmail("max.mustermann@example.de").getPasswort();

        /* ************************ ACT ************************ */
        boolean actual = adminController.changePassword("max.mustermann@example.de", "ilikebigbiblesandIcannotli3!");
        String neu = personRepository.getReferenceById(p1.getId_Person()).getPasswort();

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(p1.getId_Person()));

        /* *********************** ASSERT ********************** */
        assertNotEquals(old, neu);
        assertTrue(actual);

    }

    @Test
    @DisplayName("Testet changePassword() von einer deaktivierten Person")
    @Transactional
    void changePasswordOfDeactivatedPersonTest() {
        /* ********************** ARRANGE ********************** */
        Student p1 = new Student();
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p1.setPasswort("ichbimsDerGr0sst1!!!");
        p1.setAktiv(false);
        personRepository.save(p1);
        String old = personRepository.findByEmail("max.mustermann@example.de").getPasswort();

        /* ************************ ACT ************************ */
        boolean actual = adminController.changePassword("max.mustermann@example.de", "ilikebigbiblesandIcannotli3!");
        String neu = personRepository.getReferenceById(p1.getId_Person()).getPasswort();

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(p1.getId_Person()));

        /* *********************** ASSERT ********************** */
        assertFalse(actual);
        assertNotEquals(old,neu);
    }

    @Test
    @DisplayName("Testet changePassword() von einer nicht existierenden Person")
    @Transactional
    void changePasswordOfNotExistingPersonTest() {
        /* ********************** ARRANGE ********************** */
        Student p1 = new Student();
        p1.setVorname("Max");
        p1.setNachname("Mustermann");
        p1.setEmail("max.mustermann@example.de");
        p1.setPasswort("ichbimsDerGr0sst1!!!");

        /* ************************ ACT ************************ */
        boolean actual = adminController.changePassword("max.mustermann@example.de", "ilikebigbiblesandIcannotli3!");

        /* *********************** ASSERT ********************** */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet getAllAdmins()")
    @Transactional
    void getAllAdminsTest() {
        /* ********************** ARRANGE ********************** */
        admin.setPasswort("Test123!");
        admin.setEmail("tolleradmin@yeah.de");
        adminController.createAdmin(admin);
        List<Admin> expected = List.of(admin);

        /* ************************ ACT ************************ */
        List<Admin> actual = adminController.getAllAdmins();

        // 2. Admin hinzufügen und auch noch finden
        Admin admin2 = new Admin();
        admin2.setPasswort("Test123!");
        admin2.setEmail("vieltollereradmin@boo-yeah.de");
        adminController.createAdmin(admin2);
        List<Admin> expected2 = List.of(admin, admin2);

        List<Admin> actual2 = adminController.getAllAdmins();

        /* ********************** TEAR DOWN ******************** */
        personRepository.delete(personRepository.getReferenceById(admin.getId_Person()));
        personRepository.delete(personRepository.getReferenceById(admin2.getId_Person()));

        /* *********************** ASSERT ********************** */
        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }
}