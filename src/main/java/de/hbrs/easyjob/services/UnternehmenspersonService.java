package de.hbrs.easyjob.services;


import de.hbrs.easyjob.controllers.registrieren.UnternehmenspersonRegistrierenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service

public class UnternehmenspersonService {


    private final UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController;
    private final UnternehmenService unternehmenService;

    public UnternehmenspersonService(UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController, UnternehmenService unternehmenService) {
        this.unternehmenspersonRegistrierenController = unternehmenspersonRegistrierenController;
        this.unternehmenService = unternehmenService;
    }

    @Transactional
    public boolean saveUnternehmensperson(Unternehmensperson unternehmensperson) {
        // falls der Person gar kein Unternehmen zu geordnet wurde
        if (unternehmensperson.getUnternehmen() == null) {
            return false;
        }

        Unternehmen bestehendesUnternehmen = unternehmenService.findByName(unternehmensperson.getUnternehmen().getName());
        //Account aktivieren
        unternehmensperson.setAktiv(true);
        unternehmensperson.setGesperrt(false);
        if (bestehendesUnternehmen != null) {
            // Unternehmen existiert, Unternehmensperson dem Unternehmen zuordnen
            unternehmensperson.setUnternehmen(bestehendesUnternehmen);
            return unternehmenspersonRegistrierenController.createUnternehmenspersonWithCompany(unternehmensperson, true);
        } else {
            // Unternehmen existiert nicht, neues Unternehmen erstellen
            Unternehmen neuesUnternehmen = unternehmenService.savenewUnternehmen(unternehmensperson.getUnternehmen(), unternehmensperson);
            if (neuesUnternehmen == null) {
                return false;
            }
            unternehmensperson.setUnternehmen(neuesUnternehmen);
            return unternehmenspersonRegistrierenController.createUnternehmenspersonWithoutCompany(unternehmensperson, true);
        }
    }

}
