package de.hbrs.easyjob.services;

import de.hbrs.easyjob.controllers.ValidationController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UnternehmenService {
    private final UnternehmenRepository unternehmenRepository;
    private final OrtRepository ortRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final JobRepository jobRepository;
    private final BrancheRepository brancheRepository;

    @Autowired
    public UnternehmenService(UnternehmenRepository unternehmenRepository, OrtRepository ortRepository, JobRepository jobRepository, BrancheRepository brancheRepository) {
        this.unternehmenRepository = unternehmenRepository;
        this.ortRepository = ortRepository;
        this.jobRepository = jobRepository;
        this.brancheRepository = brancheRepository;
    }
    public Unternehmen findByName(String name) {
        return unternehmenRepository.findByName(name);
    }
    public Unternehmen saveUnternehmen(Unternehmen unternehmen) {
        if(unternehmen.getName() == null || unternehmen.getName().isEmpty()){
            return null;
        }
        return unternehmenRepository.save(unternehmen);
    }
    public Unternehmen findByID(Integer id){
        return unternehmenRepository.findById(id).orElse(null);
    }

    public int anzahlJobs(Unternehmen unternehmen){
        return jobRepository.countByUnternehmenId_Unternehmen(unternehmen.getId_Unternehmen());
    }

    @Transactional
    public Ort getFirstStandort(Unternehmen unternehmen) {
        return unternehmen.getStandorte().stream().findFirst().orElse(null);
    }
    @Transactional
    public Unternehmen savenewUnternehmen(Unternehmen unternehmen, Unternehmensperson unternehmensperson) {
        if (ValidationController.isValidUnternehmen(unternehmen, ortRepository, brancheRepository)) {
            Set<Ort> aktualisierteStandorte = new HashSet<>();
            for (Ort ort : unternehmen.getStandorte()) {
                Ort gefundenerOrt = ortRepository.findByPLZAndOrt(ort.getPLZ(), ort.getOrt());
                aktualisierteStandorte.add(gefundenerOrt);
            }
            unternehmen.setStandorte(aktualisierteStandorte);
            unternehmen.setUnternehmensperson(unternehmensperson);
            unternehmen.setAktiv(true);
            return unternehmenRepository.save(unternehmen);
        } else {
            return null;
        }
    }
    public List<Branche> getAllBranchen() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Branche> cq = cb.createQuery(Branche.class);
        Root<Unternehmen> unternehmen = cq.from(Unternehmen.class);
        cq.select(unternehmen.get("branchen")).distinct(true);
        List<Branche> branches = entityManager.createQuery(cq).getResultList();
        return branches.stream().distinct().toList();
    }

    public List<Job> getAllJobs(Integer unternehmenId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> job = cq.from(Job.class);
        Join<Job, Unternehmen> unternehmen = job.join("unternehmen");
        cq.select(job)
                .where(cb.equal(unternehmen.get("id_Unternehmen"), unternehmenId));
        return entityManager.createQuery(cq).getResultList();
    }


    public List<Job> getAllJobsByUnternehmenspersonId(Integer id){
        return jobRepository.findAllJobs(id);
    }
}