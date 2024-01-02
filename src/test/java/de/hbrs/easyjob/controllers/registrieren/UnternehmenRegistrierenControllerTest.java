package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.controllers.UnternehmenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.services.UnternehmenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnternehmenRegistrierenControllerTest {
    // Repositories
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private UnternehmenRepository unternehmenRepository;

    // Controller
    private UnternehmenController unternehmenController;

    // Entity
    private Unternehmen unternehmen;

    @BeforeEach
    void setUp() {
        // Service
        UnternehmenService unternehmenService = new UnternehmenService(unternehmenRepository, ortRepository, jobRepository);

        // Controller
        unternehmenController = new UnternehmenController(unternehmenService);

        // Entity
        unternehmen = new Unternehmen();
    }

    @AfterEach
    void tearDown() {
        // Controller
        unternehmenController = null;

        // Entity
        unternehmen = null;
    }

    @Test
    @DisplayName("valides Unternehmen registrieren")
    void createValidUnternehmenTest() {
        /* ***************************** ARRANGE ***************************** */
        unternehmen.setName("Katzenkörbchen GmbH");

        /* ******************************* ACT ******************************* */
        ResponseEntity<Unternehmen> actual = unternehmenController.createUnternehmen(unternehmen);

        /* ****************************** ASSERT ***************************** */
        assertNotNull(actual);

        /* **************************** TEAR DOWN **************************** */
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen.getId_Unternehmen()));
    }

    @Test
    @DisplayName("nicht valides Unternehmen registrieren")
    void createNonValidUnternehmenTest() {
        /* ******************************* ACT ******************************* */
        ResponseEntity<Unternehmen> actual = unternehmenController.createUnternehmen(unternehmen);

        // -------------------------------------------------------------------------------------------------------------
        // sollte nicht möglich sein > wenn in UnternehmenController gefixt, kann die Zeile hier gelöscht werden
        unternehmenRepository.delete(unternehmenRepository.getReferenceById(unternehmen.getId_Unternehmen()));
        // -------------------------------------------------------------------------------------------------------------

        /* ****************************** ASSERT ***************************** */
        assertNull(actual);
    }
}