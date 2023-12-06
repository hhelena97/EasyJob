package de.hbrs.easyjob.services;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Studienfach;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import de.hbrs.easyjob.services.specifications.JobSpecification;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobFilterService {


    @Autowired
    private JobRepository jobRepository;

    /**
     * Filtert Jobs basierend auf den übergebenen Kriterien.
     * <p>
     * Diese Methode ermöglicht es, Jobs anhand von spezifischen Kriterien wie Ort, Jobkategorie und Studienfächern zu filtern.
     * Es wird eine dynamische Abfrage erstellt, die alle angegebenen Bedingungen berücksichtigt. Die Methode gibt eine Liste
     * von Jobs zurück, die den angegebenen Kriterien entsprechen. Wenn ein Kriterium null oder leer ist, wird dieses
     * Kriterium beim Filtern ignoriert.
     *
     * @param orte eine Menge Ort, nach dem gefiltert werden soll.
     * @param kategorien Eine Menge Jobkategorie, nach der gefiltert werden soll.
     * @param studienfacher Eine Menge von Studienfächern, nach denen gefiltert werden soll.
     * @param homeOffice
     * @param branchen
     * @return Eine Liste von Jobs, die den angegebenen Filterkriterien entsprechen. Die Liste kann leer sein, wenn keine
     *         Jobs gefunden wurden, die den Kriterien entsprechen.
     */
    public List<Job> filterJobs(List<Job> jobs,Set<Ort> orte, Set<JobKategorie> kategorien, Set<Studienfach> studienfacher, boolean homeOffice, Set<Branche> branchen) {
        if(jobs == null){
            Specification<Job> spec = Specification.where(JobSpecification.inOrte(orte))
                    .and(JobSpecification.inJobKategorien(kategorien))
                    .and(JobSpecification.inStudienfacher(studienfacher))
                    .and(JobSpecification.isHomeOffice(homeOffice))
                    .and(JobSpecification.inBranchen(branchen));

            return jobRepository.findAll(spec);

        }

        return jobs.stream()
                .filter(job -> orte.isEmpty() || orte.contains(job.getOrt()))
                .filter(job -> kategorien.isEmpty() || kategorien.contains(job.getJobKategorie()))
                .filter(job -> studienfacher.isEmpty() || studienfacher.contains(job.getStudienfacher()))
                .filter(job -> !homeOffice || job.isHomeOffice())
                .filter(job -> branchen.isEmpty() || branchen.stream().anyMatch(branche -> job.getUnternehmen().getBranchen().contains(branche)))
                .collect(Collectors.toList());
    }
}
