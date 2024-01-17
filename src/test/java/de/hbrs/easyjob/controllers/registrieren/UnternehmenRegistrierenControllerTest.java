package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UnternehmenRegistrierenControllerTest {
    // Repositories
    @Autowired
    private BrancheRepository brancheRepository;
    @Autowired
    private UnternehmenRepository unternehmenRepository;
    @Autowired
    private OrtRepository ortRepository;

    // Controller
    private UnternehmenRegistrierenController unternehmenRegistrierenController;

    // Entity
    private Unternehmen unternehmen;

    @BeforeEach
    void setUp() {
        // Controller
        unternehmenRegistrierenController = new UnternehmenRegistrierenController(unternehmenRepository, brancheRepository, ortRepository);

        // Entity
        unternehmen = new Unternehmen();
    }

    @AfterEach
    void tearDown() {
        unternehmenRegistrierenController = null;
    }

    @Test
    @DisplayName("Testet, ob valides Unternehmen registriert werden kann")
    @Transactional
    void createValidUnternehmenTest() {
        /* ******************************** ARRANGE ******************************** */
        unternehmen.setName("MeowMeow Cafe GmbH");
        unternehmen.setBranchen(Set.of(brancheRepository.findByName("Öffentlicher Dienst")));
        unternehmen.setStandorte(Set.of(ortRepository.findByPLZAndOrt("53115","Bonn")));
        unternehmen.setBeschreibung("Das ist ein Cafe. Da kann man Kaffee trinken. Yeah.");

        /* ********************************** ACT ********************************** */
        boolean actual = unternehmenRegistrierenController.createUnternehmen(unternehmen);

        /* ********************************* ASSERT ******************************** */
        assertTrue(actual);

        /* ******************************** TEAR DOWN ****************************** */
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen.getId_Unternehmen()));
    }

    @Test
    @DisplayName("Testet, ob nicht valides Unternehmen registriert werden kann")
    @Transactional
    void createNonValidUnternehmenTest() {
        /* ********************************** ACT ********************************** */
        boolean actual = unternehmenRegistrierenController.createUnternehmen(unternehmen);

        /* ********************************* ASSERT ******************************** */
        assertFalse(actual);
    }
}