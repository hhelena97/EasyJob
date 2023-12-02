package de.hbrs.easyjob.service;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Ort;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;


@Service
public class JobService {

    private final JobRepository jobRepository;
    private final StudienfachRepository studienfachRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final OrtRepository ortRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public JobService(JobRepository jobRepository, StudienfachRepository studienfachRepository,
                      JobKategorieRepository jobKategorieRepository, PersonRepository personRepository,
                      UnternehmenRepository unternehmenRepository, OrtRepository ortRepository) {
        this.jobRepository = jobRepository;
        this.studienfachRepository = studienfachRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.ortRepository = ortRepository;
    }

    public List<Job> findAllByUnternehmenId(int unternehmenId) {
        return jobRepository.findAllByUnternehmenId(unternehmenId);
    }

    @Transactional
    public Job saveJob(Job job) {
        Ort gefundeneOrt = ortRepository.findByPLZAndOrt(job.getOrt().getPLZ(),job.getOrt().getOrt());
        if(gefundeneOrt==null){
            gefundeneOrt = ortRepository.save(new Ort(job.getOrt().getPLZ(),job.getOrt().getOrt()));
        }
        job.setOrt(gefundeneOrt);
        Unternehmen gefundeneUnternehmen = unternehmenRepository.findByName(job.getUnternehmen().getName());
        job.setUnternehmen(gefundeneUnternehmen);
        Person gefundenePerson = personRepository.findByEmail(job.getPerson().getEmail());
        job.setPerson(gefundenePerson);
        JobKategorie gefundeneKategorie = jobKategorieRepository.findByKategorie(job.getJobKategorie().getKategorie());
        if (gefundeneKategorie == null){
            gefundeneKategorie = jobKategorieRepository.save(new JobKategorie(job.getJobKategorie().getKategorie()));
        }
        job.setJobKategorie(gefundeneKategorie);
        Set<Studienfach> studienfaecher = new HashSet<>();
        for (Studienfach studienfach : job.getStudienfacher()) {
            Studienfach gefundeneStiduenfach = studienfachRepository.findByFachAndAbschluss(studienfach.getFach(),studienfach.getAbschluss());
            if (gefundeneStiduenfach == null){
                gefundeneStiduenfach = studienfachRepository.save(new Studienfach(studienfach.getFach(),studienfach.getAbschluss()));
            }
            studienfaecher.add(gefundeneStiduenfach);

        }
        job.setStudienfacher(studienfaecher);

        return jobRepository.save(job);
    }


    public List<Job> getAllJobs() {
        return jobRepository.findAllJobs();
    }

    public Job getJobById(Integer id){
        return jobRepository.findById(id).get();
    }

    public List<Job> getJobsByIds(List<Integer> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> job = cq.from(Job.class);

        cq.select(job).where(job.get("id_Job").in(ids));
        return entityManager.createQuery(cq).getResultList();
    }
    public List<Ort> getAllStandorte() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ort> cq = cb.createQuery(Ort.class);
        Root<Job> job = cq.from(Job.class);
        cq.select(job.get("ort")).distinct(true);
        List<Ort> orte = entityManager.createQuery(cq).getResultList();
        return orte.stream().distinct().collect(Collectors.toList());
    }

    public List<JobKategorie> getAllJobKategorien() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobKategorie> cq = cb.createQuery(JobKategorie.class);
        Root<Job> job = cq.from(Job.class);
        cq.select(job.get("jobKategorie")).distinct(true);
        List<JobKategorie> jobKategorien = entityManager.createQuery(cq).getResultList();
        return jobKategorien.stream().distinct().collect(Collectors.toList());
    }
    public List<Studienfach> getAllStudienfacher() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Studienfach> cq = cb.createQuery(Studienfach.class);

        Root<Job> job = cq.from(Job.class);
        Join<Job, Studienfach> studienfach = job.join("studienfacher");

        cq.select(studienfach).distinct(true);

        return entityManager.createQuery(cq).getResultList();
    }
}
