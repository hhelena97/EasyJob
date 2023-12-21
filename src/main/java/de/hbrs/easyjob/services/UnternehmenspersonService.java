package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.UnternehmenspersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class UnternehmenspersonService {

    private final UnternehmenspersonRepository unternehmenspersonRepository;

    private final UnternehmenService unternehmenService;
    @PersistenceContext
    private EntityManager entityManager;

    public UnternehmenspersonService(UnternehmenspersonRepository unternehmenspersonRepository, UnternehmenService unternehmenService) {
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.unternehmenService = unternehmenService;
    }


    public Unternehmensperson saveUnternehmensperson(Unternehmensperson unternehmensperson) {
        Unternehmen bestehendesUnternehmen = unternehmenService.findByName(unternehmensperson.getUnternehmen().getName());

        if (bestehendesUnternehmen != null) {
            // Unternehmen existiert, Unternehmensperson dem Unternehmen zuordnen
            bestehendesUnternehmen = entityManager.merge(bestehendesUnternehmen);
            unternehmensperson.setUnternehmen(bestehendesUnternehmen);
        } else {
            // Unternehmen existiert nicht, neues Unternehmen erstellen
            Unternehmen neuesUnternehmen = unternehmenService.savenewUnternehmen(unternehmensperson.getUnternehmen(), unternehmensperson);
            //Unternehmensprofil aktivieren
            neuesUnternehmen.setAktiv(true);
            unternehmensperson.setUnternehmen(neuesUnternehmen);
        }
        //Account aktivieren
        unternehmensperson.setAktiv(true);

        return unternehmenspersonRepository.save(unternehmensperson);
    }

}
