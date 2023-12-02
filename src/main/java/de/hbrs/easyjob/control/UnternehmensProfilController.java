package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.JobRepository;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@RestController
public class UnternehmensProfilController {



    private JobRepository jobRepository;
    public UnternehmensProfilController(){

    }

    @Autowired
    public UnternehmensProfilController(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }
    public String getUnternehmensName(Unternehmen unternehmen){
        return unternehmen.getName();
    }
    public String getUnternehmensBeschreibung(Unternehmen unternehmen){
        return unternehmen.getBeschreibung();
    }
    public String getUnternehmensOrte(Unternehmen unternehmen){

        String result = "";

        Ort s = unternehmen.getStandorte().stream().findFirst().orElse(null);
                result = s.getOrt() + " " + s.getPLZ();

        return result;

    }
    public Set<Branche> getUnternehmensBranchen(Unternehmen unternehmen){
        return unternehmen.getBranchen();
    }
    public String getUnternehmensLogo(Unternehmen unternehmen){
        return unternehmen.getLogo();
    }
    public Unternehmensperson getUnternehmens(Unternehmen unternehmen){
        return unternehmen.getUnternehmensperson();
    }
    public Set<Job> getJobSet(Unternehmen unternehmen){
    return jobRepository.findAllByUnternehmen(unternehmen.getId_Unternehmen());
    }


}
