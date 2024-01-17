package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.repositories.UnternehmenspersonRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProfilDeaktivierenControllerTest {
    // Repositories
    private static final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
    private static final UnternehmenRepository unternehmenRepository = Mockito.mock(UnternehmenRepository.class);
    private static final UnternehmenspersonRepository unternehmenspersonRepository = Mockito.mock(UnternehmenspersonRepository.class);

    // Controllers
    @InjectMocks
    private ProfilDeaktivierenController profilDeaktivierenController;

    // Entities
    private static final Student person = new Student();                  // Person Erika Mustermann (nicht in Unternehmen)
    private static final Unternehmen unternehmen = new Unternehmen();               // Unternehmen
    private static final Unternehmensperson manager = new Unternehmensperson();     // Manager
    private static final Unternehmensperson personU = new Unternehmensperson();     // Unternehmensperson Max Mustermann
    private static final Unternehmen unternehmen2 = new Unternehmen();               // Unternehmen
    private static final Unternehmensperson manager2 = new Unternehmensperson();     // Manager
    private static final Unternehmensperson personU2 = new Unternehmensperson();     // Unternehmensperson Maxx Mustermannn

    @BeforeEach
    void setUp()
    {
        person.setVorname("Erika");
        person.setNachname("Mustermann");
        person.setEmail("erika.mustermann@example.com");
        person.setPasswort("Passwort-123");
        person.setId_Person(12);
        person.setAktiv(true);

        // -------------------------------------------------------------------------------------------------------------

        unternehmen.setId_Unternehmen(1);
        unternehmen.setName("TestUnternehmen");
        unternehmen.setBeschreibung("TestBeschreibung");
        unternehmen.setAktiv(true);
        unternehmen.setUnternehmensperson(manager);

        manager.setVorname("Heinz");
        manager.setNachname("Schmitz");
        manager.setEmail("schmitz57@mail.com");
        manager.setPasswort("123-Passwort");
        manager.setUnternehmen(unternehmen);
        manager.setId_Person(5);
        manager.setAktiv(true);

        //System.out.println(manager == Arrays.asList(manager));

        personU.setVorname("Max");
        personU.setNachname("Mustermann");
        personU.setEmail("max.mustermann@example.com");
        personU.setPasswort("Hallo-123");
        personU.setUnternehmen(unternehmen);
        personU.setId_Person(7);
        personU.setAktiv(true);

        Mockito.when(unternehmenRepository.findById(unternehmen.getId_Unternehmen())).thenReturn(Optional.of(unternehmen));

        // -------------------------------------------------------------------------------------------------------------

        unternehmen2.setId_Unternehmen(2);
        unternehmen2.setName("TestUnternehmenn");
        unternehmen2.setAktiv(true);
        unternehmen2.setUnternehmensperson(manager2);

        manager2.setVorname("Heinzz");
        manager2.setNachname("Schmitzz");
        manager2.setEmail("schmitz57@mail.comm");
        manager2.setPasswort("123-Passwortt");
        manager2.setUnternehmen(unternehmen2);
        manager2.setId_Person(55);
        manager2.setAktiv(true);

        personU2.setVorname("Maxx");
        personU2.setNachname("Mustermannn");
        personU2.setEmail("max.mustermann@example.comm");
        personU2.setPasswort("Hallo-1233");
        personU2.setUnternehmen(unternehmen2);
        personU2.setId_Person(77);
        personU2.setAktiv(true);

        Mockito.when(unternehmenRepository.findById(unternehmen2.getId_Unternehmen())).thenReturn(Optional.of(unternehmen2));

        Mockito.when(unternehmenRepository.save(Mockito.any(Unternehmen.class))).thenAnswer(i -> i.getArguments()[0]);

        Mockito.when(personRepository.findAllByUnternehmenId(unternehmen.getId_Unternehmen())).thenReturn(List.of(manager, personU));
        Mockito.when(personRepository.findAllByUnternehmenId(unternehmen2.getId_Unternehmen())).thenReturn(List.of(manager2, personU2));

        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    @DisplayName("Deaktivierung Person erfolgreich")
    @Transactional
    void deactivatePersonSuccessfulPersonInactive()
    {
        // Profil deaktivieren erfolgreich
        assertTrue(profilDeaktivierenController.profilDeaktivierenPerson(person));
        assertFalse(person.getAktiv());     // Person inaktiv
    }

    @Test
    @DisplayName("Deaktivierung Unternehmen durch Manager erfolgreich")
    void deactivateManagerUnternehmenSuccessfulUnternehmenInactive()
    {
        // Profile deaktivieren erfolgreich → Alle Unternehmenspersonen des Unternehmens werden deaktiviert
        assertTrue(profilDeaktivierenController.profilDeaktivierenUnternehmen(manager));
        assertFalse(unternehmen.isAktiv()); // Unternehmen inaktiv
        assertFalse(manager.getAktiv());    // Manager inaktiv
        assertFalse(personU.getAktiv());    // Unternehmensperson inaktiv
    }

    @Test
    @DisplayName("Deaktivierung Unternehmensperson erfolgreich")
    void deactivatePersonUnternehmenSuccessfulPersonInactive()
    {
        // Profile deaktivieren erfolgreich → Nur das Profil der deaktivierenden Unternehmensperson wird deaktiviert
        assertTrue(profilDeaktivierenController.profilDeaktivierenPerson(personU2));
        assertTrue(unternehmen2.isAktiv());  // Unternehmen aktiv
        assertTrue(manager2.getAktiv());     // Manager aktiv
        assertFalse(personU2.getAktiv());    // Unternehmensperson inaktiv
    }

    @AfterAll
    static void tearDown()
    {
        Mockito.reset(personRepository);
        Mockito.reset(unternehmenRepository);
        Mockito.reset(unternehmenspersonRepository);
    }
}
