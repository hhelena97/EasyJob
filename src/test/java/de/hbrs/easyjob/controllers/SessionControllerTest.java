package de.hbrs.easyjob.controllers;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SessionControllerTest {
    // Repositories
    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;
    @Autowired
    private PersonRepository personRepository;

    // Controller
    private SessionController sessionController;

    // Entities
    @Autowired
    private AuthenticationManager authenticationManager;
    private final String existing_person_email = "nina.becker@uni-bonn.de";
    private final String existing_person_password = "NinaMINT2023";
    private final String deactivated_person_email = "helena-heyen@email.de";
    private final String deactivated_person_password = "Test123!";

    private static Routes routes;

    @BeforeAll
    public static void createRoutes() {
        // initialize routes only once, to avoid view auto-detection before every test and to speed up the tests
        routes = new Routes().autoDiscoverViews("com.vaadin.flow.demo");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);
        sessionController = new SessionController(personRepository, customSecurityContextRepository, authenticationManager);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }


    @Test
    @DisplayName("Login mit korrekten Daten einer nicht deaktivierten Person")
    void loginWithCorrectDataActiveTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(existing_person_email, existing_person_password);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertTrue(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit korrekten Daten einer deaktivierten Person")
    void loginWithCorrectDataDeactivatedTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(deactivated_person_email, deactivated_person_password);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertTrue(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit korrekten Daten einer eingeloggten Person")
    void loginWithCorrectDataLoggedInTest() {
        /* ************************************************** ACT *************************************************** */
        sessionController.login(existing_person_email, existing_person_password);
        boolean actual = sessionController.login(existing_person_email, existing_person_password);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertTrue(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit falschem Passwort")
    void loginWithWrongPasswordTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(existing_person_email, "falsches_passwort");

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit falscher E-Mail")
    void loginWithWrongEmailTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login("falsche_email", existing_person_password);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit falscher E-Mail und falschem Passwort")
    void loginWithWrongEmailAndPasswordTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login("falsche_email", "falsches_password");

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit leerer E-Mail")
    void loginWithEmptyEmailTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(null, existing_person_password);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit leerem Passwort")
    void loginWithEmptyPasswordTest()  {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(existing_person_password, null);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Login mit leerer E-Mail und leerem Passwort")
    void loginWithEmptyEmailAndPasswordTest()  {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.login(null, null);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Testet den Logout mit angemeldeter/authentifizierter Person")
    void logoutTest() {
        /* ************************************************ ARRANGE ************************************************* */
        sessionController.login(existing_person_email, existing_person_password);

        /* ************************************************** ACT *************************************************** */
        //TODO: boolean actual = sessionController.logout();

        /* ************************************************* ASSERT ************************************************* */
        //assertTrue(actual);
        assertFalse(sessionController.isLoggedIn());

    }

    @Test
    @DisplayName("Testet den Logout (wieso auch immer) mit nicht angemeldeter Person")
    void logoutStrangeTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.logout();

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Testet den Logout mit deaktiviertem Profil")
    void logoutDeactivatedTest() {
        /* ************************************************ ARRANGE ************************************************* */
        sessionController.login(deactivated_person_email, deactivated_person_password);

        /* ************************************************** ACT *************************************************** */
        //TODO: boolean actual = sessionController.logout();

        /* ************************************************* ASSERT ************************************************* */
        //assertTrue(actual);
        assertFalse(sessionController.isLoggedIn());
    }

    @Test
    @DisplayName("Testet die isLoggdedIn() Funktion")
    void isLoggedIn() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.isLoggedIn();
        sessionController.login(existing_person_email, existing_person_password);
        boolean actual2 = sessionController.isLoggedIn();
        //TODO: sessionController.logout();
        boolean actual3 = sessionController.isLoggedIn();

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
        assertTrue(actual2);
        assertFalse(actual3);
    }

    @Test
    @DisplayName("Testet die hasRole() mit angemeldeter Person Funktion")
    void hasRoleLoggedIn() {
        /* ************************************************ ARRANGE ************************************************* */
        sessionController.login(existing_person_email, existing_person_password);

        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.hasRole("ROLE_STUDENT");

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
    }

    @Test
    @DisplayName("Testet die hasRole() mit abgemeldeter Person Funktion")
    void hasRoleLoggedOut() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = sessionController.hasRole("ROLE_STUDENT");

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
    }

    @Test
    @DisplayName("Testet die getPerson() Funktion bei einer angemeldeten Person")
    void getLoggedInPerson() {
        /* ************************************************ ARRANGE ************************************************* */
        sessionController.login(existing_person_email, existing_person_password);

        /* ************************************************** ACT *************************************************** */
        Person actual = sessionController.getPerson();

        /* ************************************************* ASSERT ************************************************* */
        assertEquals(personRepository.findByEmail(existing_person_email).getId_Person(), actual.getId_Person());
    }

    @Test
    @DisplayName("Testet die getPerson() Funktion bei einer nicht angemeldeten Person")
    void getNotLoggedInPerson() {
        /* ************************************************** ACT *************************************************** */
        Person actual = sessionController.getPerson();

        /* ************************************************* ASSERT ************************************************* */
        assertNull(actual);
    }
}