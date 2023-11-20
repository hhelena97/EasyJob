package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LoginControlTest {
    private static final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
    private final LoginControl loginControl = new LoginControl(personRepository);

    @BeforeAll
    static void setUp() {
        Person person = new Person();
        person.setVorname("Max");
        person.setNachname("Mustermann");
        person.setEmail("max.mustermann@example.com");
        person.setPasswort("123456");
        Mockito.when(personRepository.findByEmail("max.mustermann@example.com")).thenReturn(person);
    }

    @Test
    @DisplayName("Authentifizierung mit korrekten Daten")
    void authenticateWithCorrectData() {
        assertTrue(loginControl.authenticate("max.mustermann@example.com", "123456"));
    }

    @Test
    @DisplayName("Authentifizierung mit falschem Passwort")
    void authenticateWithWrongPassword() {
        assertFalse(loginControl.authenticate("max.mustermann@example.com", "654321"));
    }

    @Test
    @DisplayName("Authentifizierung mit falscher E-Mail")
    void authenticateWithWrongEmail() {
        assertFalse(loginControl.authenticate("max.mustermann@wrong.com", "123456"));
    }

    @Test
    @DisplayName("Authentifizierung mit falscher E-Mail und falschem Passwort")
    void authenticateWithWrongEmailAndPassword() {
        assertFalse(loginControl.authenticate("max.mustermann@wrong.com", "654321"));
    }

    @Test
    @DisplayName("Authentifizierung mit leerer E-Mail")
    void authenticateWithEmptyEmail() {
        assertFalse(loginControl.authenticate("", "123456"));
    }

    @Test
    @DisplayName("Authentifizierung mit leerem Passwort")
    void authenticateWithEmptyPassword()  {
        assertFalse(loginControl.authenticate("max.mustermann@example.com", ""));
    }

    @Test
    @DisplayName("Authentifizierung mit leerer E-Mail und leerem Passwort")
    void authenticateWithEmptyEmailAndPassword()  {
        assertFalse(loginControl.authenticate("", ""));
    }

    @Test
    @DisplayName("Authentifizierung mit null als E-Mail")
    void authenticateWithNullEmail()  {
        assertFalse(loginControl.authenticate(null, "123456"));
    }

    @Test
    @DisplayName("Authentifizierung mit null als Passwort")
    void authenticateWithNullPassword()  {
        assertFalse(loginControl.authenticate("max.mustermann@example.com", null));
    }

    @Test
    @DisplayName("Authentifizierung mit null als E-Mail und null als Passwort")
    void authenticateWithNullEmailAndPassword() {
        assertFalse(loginControl.authenticate(null, null));
    }

    @AfterAll
    static void tearDown() {
        Mockito.reset(personRepository);
    }
}