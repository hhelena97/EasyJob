package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repository.BrancheRepository;
import de.hbrs.easyjob.repository.OrtRepository;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class RegistrierungsControllerUnternehmen {
    final
    UnternehmenRepository unternehmenRepository;
    private final BrancheRepository brancheRepository;
    private final OrtRepository ortRepository;
    @Autowired
    public RegistrierungsControllerUnternehmen(UnternehmenRepository unternehmenRepository, BrancheRepository brancheRepository, OrtRepository ortRepository) {
        super();
        this.unternehmenRepository = unternehmenRepository;
        this.brancheRepository = brancheRepository;
        this.ortRepository = ortRepository;
    }

    public boolean createUnternehmen(Unternehmen unternehmen ){
        boolean ret =(isValidName(unternehmen.getName())&&isValidBranche(unternehmen)&&isValidOrt(unternehmen)&&isValidBeschreibung(unternehmen.getBeschreibung()));
        if(ret){
            unternehmenRepository.save(unternehmen);
        }
        return ret;
    }
    public boolean isValidName(String name){
        return true;
    }
    public boolean isValidBranche(Unternehmen unternehmen){
        Set<Branche> brancheSet = new HashSet<>();
        for (Branche branche: unternehmen.getBranchen()){
            Branche gefundenBranche = brancheRepository.findByName(branche.getName());
            if(gefundenBranche== null){return false;}
            brancheSet.add(gefundenBranche);
        }
        unternehmen.setBranchen(brancheSet);//ersetze string to id
        return true;
    }
    public boolean isValidOrt(Unternehmen unternehmen){
        Set<Ort> ortSet = new HashSet<>();
        for (Ort ort: unternehmen.getStandorte()){
            Ort gefundenOrte = ortRepository.findByPLZAndOrt(ort. getPLZ(), ort.getOrt());
            if(gefundenOrte== null){return false;}
            ortSet.add(gefundenOrte);
        }
        unternehmen.setStandorte(ortSet);//ersetze string to id
        return true;
    }
    public  boolean isValidBeschreibung(String beschreibung) {
        return beschreibung != null && beschreibung.length() <= 400;
    }
    public void setUnternehmensperson(Unternehmen unternehmen, Unternehmensperson unternehmensperson){
        unternehmen.setUnternehmensperson(unternehmensperson);
    }

}
