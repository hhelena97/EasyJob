package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class UnternehmenProfilController {



    private JobRepository jobRepository;
    public UnternehmenProfilController(){

    }

    @Autowired
    public UnternehmenProfilController(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }
    public String getUnternehmensName(Unternehmen unternehmen){
        return unternehmen.getName();
    }
    public String getUnternehmensBeschreibung(Unternehmen unternehmen){
        return unternehmen.getBeschreibung();
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
