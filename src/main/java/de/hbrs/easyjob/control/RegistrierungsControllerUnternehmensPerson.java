package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import de.hbrs.easyjob.repository.UnternehmenspersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrierungsControllerUnternehmensPerson extends RegistrierungsController{
    private final UnternehmenspersonRepository unternehmenspersonRepository;
    private final UnternehmenRepository unternehmenRepository;
    @Autowired
    public RegistrierungsControllerUnternehmensPerson( UnternehmenspersonRepository unternehmenspersonRepository, UnternehmenRepository unternehmenRepository) {
        super();
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.unternehmenRepository = unternehmenRepository;
    }
    public boolean createUnternehmensPerson(Unternehmensperson unternehmensperson, boolean AGB ){
        //prüfe Email und Passwort
        boolean ret =(isValidEmail(unternehmensperson.getEmail()))&&(isValidPassword(unternehmensperson.getPasswort())&&isAGBAccepted(AGB)&&isValidVorname(unternehmensperson.getVorname())&&isValidNachname(unternehmensperson.getNachname())&&isValidTelefonnummer(unternehmensperson.getTelefon())&&isValidUnternehmen(unternehmensperson));
        if(ret){
            unternehmenspersonRepository.save(unternehmensperson);
        }
        return ret;
    }
    public boolean isValidUnternehmen(Unternehmensperson unternehmensperson){
            Unternehmen gefundensUnternehmen = unternehmenRepository.findByName(unternehmensperson.getUnternehmen().getName());
            if(gefundensUnternehmen== null){return false;}
            unternehmensperson.setUnternehmen(gefundensUnternehmen);//ersetze string to id
        return true;
    }

}

