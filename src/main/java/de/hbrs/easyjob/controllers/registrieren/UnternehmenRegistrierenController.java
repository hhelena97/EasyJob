package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.controllers.ValidationController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnternehmenRegistrierenController implements ValidationController {
    // Repositories
    private final UnternehmenRepository unternehmenRepository;
    private final BrancheRepository brancheRepository;
    private final OrtRepository ortRepository;

    /** Konstruktor des Controllers für die Registrierung von Unternehmen.
     * @param unternehmenRepository Repository für Unternehmen
     * @param brancheRepository Repository für Branchen
     * @param ortRepository Repository für Orte
     */
    public UnternehmenRegistrierenController(UnternehmenRepository unternehmenRepository, BrancheRepository brancheRepository, OrtRepository ortRepository) {
        this.unternehmenRepository = unternehmenRepository;
        this.brancheRepository = brancheRepository;
        this.ortRepository = ortRepository;
    }

    /**
     * Legt ein Unternehmen an.
     *
     * @param unternehmen Unternehmen, das angelegt werden soll
     * @return true, wenn Unternehmen angelegt wurde & false, wenn nicht
     */
    public boolean createUnternehmen(Unternehmen unternehmen) {
        boolean ret = ValidationController.isValidUnternehmen(unternehmen, ortRepository, brancheRepository);
        if (ret) {
            //Profil aktivieren
            unternehmen.setAktiv(true);
            unternehmenRepository.save(unternehmen);
        }
        return ret;
    }
}
