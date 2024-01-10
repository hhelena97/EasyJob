package de.hbrs.easyjob.controllers.registrieren;

import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnternehmenspersonRegistrierenController extends PersonRegistrierenController {
    private final UnternehmenRepository unternehmenRepository;

    /**
     * Konstruktor des Controllers für die Registrierung von Unternehmenspersonen.
     *
     * @param personRepository      Repository für Personen
     * @param unternehmenRepository Repository für Unternehmen
     */
    public UnternehmenspersonRegistrierenController(
            PersonRepository personRepository,
            UnternehmenRepository unternehmenRepository
    ) {
        super(personRepository);
        this.unternehmenRepository = unternehmenRepository;
    }

    /**
     * Legt eine Unternehmensperson an.
     *
     * @param unternehmensperson Unternehmensperson, die angelegt werden soll
     * @param hasAcceptedAGB     Prüfung, ob AGB akzeptiert wurden
     * @return true, wenn Unternehmensperson angelegt wurde & false, wenn nicht
     */
    public boolean createUnternehmenspersonWithCompany(Unternehmensperson unternehmensperson, boolean hasAcceptedAGB) {
        //Account aktivieren
        unternehmensperson.setAktiv(true);
        return isValidUnternehmen(unternehmensperson.getUnternehmen()) && super.createPerson(unternehmensperson, hasAcceptedAGB);
    }

    public boolean createUnternehmenspersonWithoutCompany(Unternehmensperson unternehmensperson, boolean hasAcceptedAGB) {
        //Account aktivieren
        unternehmensperson.setAktiv(true);
        return super.createPerson(unternehmensperson, hasAcceptedAGB);
    }

    /**
     * Prüft, ob das Unternehmen gültig ist.
     *
     * @param unternehmen Unternehmen, das geprüft werden soll
     * @return true, wenn Unternehmen gültig ist & false, wenn nicht
     */
    public boolean isValidUnternehmen(Unternehmen unternehmen) {
        return unternehmenRepository.findByName(unternehmen.getName()) != null;
    }
}

