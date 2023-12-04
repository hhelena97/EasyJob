package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.BerufsFelder;
import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.repositories.BerufsFeldRepository;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class RegS2Controller {
    private final JobKategorieRepository jobKategorieRepository;
    private final BrancheRepository brancheRepository;
    private final BerufsFeldRepository berufsFeldRepository;
    private final OrtRepository ortRepository;

    public RegS2Controller(JobKategorieRepository jobKategorieRepository, BrancheRepository brancheRepository, BerufsFeldRepository berufsFeldRepository, OrtRepository ortRepository) {
        this.jobKategorieRepository = jobKategorieRepository;
        this.brancheRepository = brancheRepository;
        this.berufsFeldRepository = berufsFeldRepository;
        this.ortRepository = ortRepository;
    }

    public List<JobKategorie> getBerufsbezeichnung(){
        return jobKategorieRepository.findAll();
    }
    public List<Branche> getBranche(){
        return brancheRepository.findAll();
    }
    public List<BerufsFelder> getBerufsFelder(){
        return berufsFeldRepository.findAll();
    }
    public List<Ort> getOrte(){
        return ortRepository.findAll();
    }
}
