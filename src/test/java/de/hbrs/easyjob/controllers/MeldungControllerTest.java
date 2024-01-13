package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.MeldungRepository;
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
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
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
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
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
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
    }

    @Test
    @DisplayName("Testet, ob man einen Chat einer Meldung zuweisen kann")
    @Transactional
    void saveMeldungChatTest() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("Unangemessene Kraftausdrücke");

        /* ************************************************** ACT *************************************************** */
        boolean actual = meldungController.saveMeldung(meldung, chat);

        /* ************************************************* ASSERT ************************************************* */
        assertTrue(actual);
        assertEquals(chat, meldung.getChat());

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
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
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Personen ausgeben lassen kann")
    @Transactional
    void getAllGemeldetePersonen() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("unhöfliches Nutzerverhalten");
        meldungController.saveMeldung(meldung, person);

        Meldung meldung2 = new Meldung();
        meldung2.setGrund("Ein ganz toller Grund");
        meldungController.saveMeldung(meldung2, person);

        Meldung meldung3 = new Meldung();
        meldung3.setGrund("Ein noch viel besserer Grund");
        meldungController.saveMeldung(meldung2, job);

        List<Meldung> expected = List.of(meldung, meldung2);

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldetePersonen();

        /* ************************************************* ASSERT ************************************************* */
        assertEquals(expected, actual);

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung2.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung3.getId_Meldung()).get());
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Unternehmen ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteUnternehmen() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("unhöfliches Nutzerverhalten");
        meldungController.saveMeldung(meldung, unternehmen);

        Meldung meldung2 = new Meldung();
        meldung2.setGrund("Ein ganz toller Grund");
        meldungController.saveMeldung(meldung2, unternehmen);

        Meldung meldung3 = new Meldung();
        meldung3.setGrund("Ein noch viel besserer Grund");
        meldungController.saveMeldung(meldung2, job);

        List<Meldung> expected = List.of(meldung, meldung2);

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteUnternehmen();

        /* ************************************************* ASSERT ************************************************* */
        assertEquals(expected, actual);

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung2.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung3.getId_Meldung()).get());
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Jobs ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteJobs() {
        /* ************************************************ ARRANGE ************************************************* */
        meldung.setGrund("unhöfliches Nutzerverhalten");
        meldungController.saveMeldung(meldung, person);

        Job job2 = new Job();
        Meldung meldung2 = new Meldung();
        meldung2.setGrund("Ein ganz toller Grund");
        meldungController.saveMeldung(meldung2, job2);

        Meldung meldung3 = new Meldung();
        meldung3.setGrund("Ein noch viel besserer Grund");
        meldungController.saveMeldung(meldung2, job);

        List<Meldung> expected = List.of(meldung2, meldung3);

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteJobs();

        /* ************************************************* ASSERT ************************************************* */
        assertEquals(expected, actual);

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung2.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung3.getId_Meldung()).get());
    }

    @Test
    @DisplayName("Testet, ob man sich alle gemeldeten Chats ausgeben lassen kann")
    @Transactional
    void getAllGemeldeteChats() {
        /* ************************************************ ARRANGE ************************************************* */
        Chat chat2 = new Chat();

        meldung.setGrund("unhöfliches Nutzerverhalten");
        meldungController.saveMeldung(meldung, chat2);

        Meldung meldung2 = new Meldung();
        meldung2.setGrund("Ein ganz toller Grund");
        meldungController.saveMeldung(meldung2, chat2);

        Meldung meldung3 = new Meldung();
        meldung3.setGrund("Ein noch viel besserer Grund");
        meldungController.saveMeldung(meldung2, chat);

        List<Meldung> expected = List.of(meldung, meldung2, meldung3);

        /* ************************************************** ACT *************************************************** */
        List<Meldung> actual = meldungController.getAllGemeldeteChats();

        /* ************************************************* ASSERT ************************************************* */
        assertEquals(expected, actual);

        /* ************************************************ TEAR DOWN *********************************************** */
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung2.getId_Meldung()).get());
        meldungRepository.delete(meldungRepository.findById(meldung3.getId_Meldung()).get());
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
        meldungRepository.delete(meldungRepository.findById(meldung.getId_Meldung()).get());
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