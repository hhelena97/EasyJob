package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.ChatRepository;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.MeldungRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeldungControllerTest {
    // Repositories
    @Autowired
    private MeldungRepository meldungRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private PersonRepository personRepository;

    // Controller
    private MeldungController meldungController;

    // Entities
    private Meldung meldung;
    private Person person;
    private Job job;
    private Unternehmen unternehmen;
    private Chat chat;


    @BeforeEach
    void setUp() {
        meldungController = new MeldungController(meldungRepository);
        meldung = new Meldung();
        person = new Person();
        job = new Job();
        unternehmen = new Unternehmen();
        chat = new Chat();
    }

    @AfterEach
    void tearDown() {
        meldungController = null;
    }

    @Test
    @DisplayName("Testet, ob man eine Person einer Meldung zuweisen kann")
    @Transactional
    void saveMeldungPersonTest() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("Unhöfliches Nutzerverhalten");

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, person);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(person, meldung.getPerson());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man eine Person einer leeren Meldung (ohne Grund) zuweisen kann")
    @Transactional
    void saveNullMeldungPersonTest() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, person);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(person, meldung.getPerson());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man einen Job einer Meldung zuweisen kann")
    @Transactional
    void saveMeldungJobTest() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("Schlechte Bezahlung");

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, job);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(job, meldung.getJob());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man einen Chat einer Meldung zuweisen kann")
    @Transactional
    void saveMeldungChatTest() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("Unangemessene Kraftausdrücke");

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, chat, person);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(chat, meldung.getChat());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man ein Unternehmen einer Meldung zuweisen kann")
    @Transactional
    void saveMeldungUnternehmenTest() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("Unhöfliches Nutzerverhalten");

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, unternehmen);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(unternehmen, meldung.getUnternehmen());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Personen ausgeben lassen kann")
    @Transactional
    void getAllGemeldetePersonen() {
        /* ************************************************ ARRANGE ************************************************* */
        List<Meldung> expected = List.of(meldungRepository.findById(2).orElseThrow(NullPointerException::new), meldungRepository.findById(180).orElseThrow(NullPointerException::new));

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldetePersonen();

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Unternehmen ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteUnternehmen() {
        /* ************************************************ ARRANGE ************************************************* */
        List<Meldung> expected = List.of(meldungRepository.findById(31).orElseThrow(NullPointerException::new), meldungRepository.findById(32).orElseThrow(NullPointerException::new));

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteUnternehmen();

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Jobs ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteJobs() {
        /* ************************************************ ARRANGE ************************************************* */
        meldungController.saveMeldung(meldung, jobRepository.findById(1).orElseThrow(NullPointerException::new));

        List<Meldung> notExpected = List.of(meldungRepository.findById(31).orElseThrow(NullPointerException::new));
        List<Meldung> expected = List.of(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteJobs();

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual.containsAll(notExpected));
        assertTrue(actual.containsAll(expected));

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Chats ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteChats() {
        /* ************************************************ ARRANGE ************************************************* */
        meldungController.saveMeldung(meldung, chatRepository.findById(13).orElseThrow(NullPointerException::new), personRepository.findById(16).orElseThrow(NullPointerException::new));

        List<Meldung> expected = List.of(meldung);
        List<Meldung> notExpected = List.of(meldungRepository.findById(31).orElseThrow(NullPointerException::new));

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteChats();

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual.containsAll(notExpected));
        assertTrue(actual.containsAll(expected));

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man Meldungen bearbeiten kann")
    @Transactional
    void meldungBearbeiten() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("unhöfliches Nutzerverhalten");
        meldungController.saveMeldung(meldung, person);

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.meldungBearbeiten(meldung);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).orElseThrow(NullPointerException::new));
    }

    @Test
    @DisplayName("Testet, ob man null-Meldungen bearbeiten kann")
    @Transactional
    void nullMeldungBearbeiten() {
        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.meldungBearbeiten(null);

        /* ************************************************* ASSERT ************************************************* */
        assertFalse(actual);
    }
}