package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobKategorieService {
    private final JobKategorieRepository JobKategorieRepository;

    @Autowired
    public JobKategorieService(JobKategorieRepository JobKategorieRepository) {
        this.JobKategorieRepository = JobKategorieRepository;
    }

    public JobKategorie saveJobKategorie(JobKategorie jobKategorie) {
        return JobKategorieRepository.save(jobKategorie);
    }
}
