package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.UnternehmenspersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnternehmenspersonService {

    private final UnternehmenspersonRepository unternehmenspersonRepository;
    private final UnternehmenService unternehmenService;



    public Unternehmensperson saveUnternehmensperson(Unternehmensperson unternehmensperson) {
        Unternehmen bestehendesUnternehmen = unternehmenService.findByName(unternehmensperson.getUnternehmen().getName());

        if (bestehendesUnternehmen != null) {
            // Unternehmen existiert, Unternehmensperson dem Unternehmen zuordnen
            unternehmensperson.setUnternehmen(bestehendesUnternehmen);
        } else {
            // Unternehmen existiert nicht, neues Unternehmen erstellen
            Unternehmen neuesUnternehmen = unternehmenService.savenewUnternehmen(unternehmensperson.getUnternehmen(), unternehmensperson);
            //Unternehmensprofil aktivieren
            neuesUnternehmen.setAktiv(true);
            unternehmensperson.setUnternehmen(neuesUnternehmen);
            // Aktualisieren des Unternehmens mit der neuen Unternehmensperson

        }
        //Account aktivieren
        unternehmensperson.setAktiv(true);

        return unternehmenspersonRepository.save(unternehmensperson);
    }

}
