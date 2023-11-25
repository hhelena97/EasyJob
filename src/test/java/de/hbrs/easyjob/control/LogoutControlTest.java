package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LogoutControlTest {

    @Autowired
    private PersonRepository persRepo;
    private LoginControl login;
    private LogoutControl logout;
    private static Person p;

    @BeforeEach
    void setUpEach() throws Exception {
        login = new LoginControl(persRepo);
        logout  = new LogoutControl(persRepo);

        Optional<Person> p_opt = persRepo.findById(53);

        if (p_opt.isEmpty()) {
            throw new Exception("Kein Objekt in der Datenbank gefunden");
        } else {
            p = p_opt.get();
        }

    }

    @AfterEach
    void tearDownEach() {
        p = null;
    }

    @AfterAll
    static void tearDown() {
        p.setAktiv(true);
    }

    @Test
    @DisplayName("Testet den Logout mit angemeldeter Person")
    @Transactional
    void logoutTest() {
        // ************** Arrange **************
        login.authenticate(p.getEmail(), p.getPasswort());

        // **************** Act ****************
        boolean actual = logout.logout();

        // ************** Assert ***************
        assertTrue(actual);
    }

    @Test
    @DisplayName("Testet den Logout (wieso auch immer) mit nicht angemeldeter Person")
    @Transactional
    void strangeLogoutTest() {
        assertThrows(Exception.class, () -> logout.logout());
    }

    @Test
    @DisplayName("Testet den Logout mit deaktiviertem Profil")
    @Transactional
    void deactivateLogoutTest() {
        // ************** Arrange **************
        p.setAktiv(false);
        login.authenticate(p.getEmail(), p.getPasswort());

        // **************** Act ****************
        boolean actual = logout.logout();

        // ************** Assert ***************
        assertTrue(actual);
    }
}