package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.BerufsFelder;
import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.repository.BerufsFeldRepository;
import de.hbrs.easyjob.repository.BrancheRepository;
import de.hbrs.easyjob.repository.JobKategorieRepository;
import de.hbrs.easyjob.repository.OrtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class ControllerRegS2 {
    private final JobKategorieRepository jobKategorieRepository;
    private final BrancheRepository brancheRepository;
    private final BerufsFeldRepository berufsFeldRepository;
    private final OrtRepository ortRepository;

    public ControllerRegS2(JobKategorieRepository jobKategorieRepository, BrancheRepository brancheRepository, BerufsFeldRepository berufsFeldRepository, OrtRepository ortRepository) {
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
