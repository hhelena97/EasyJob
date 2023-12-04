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
@Service
public class JobFilterService {


    @Autowired
    private JobRepository jobRepository;

    /**
     * Filtert JobsUebersichtView basierend auf den übergebenen Kriterien.
     * <p>
     * Diese Methode ermöglicht es, JobsUebersichtView anhand von spezifischen Kriterien wie Ort, Jobkategorie und Studienfächern zu filtern.
     * Es wird eine dynamische Abfrage erstellt, die alle angegebenen Bedingungen berücksichtigt. Die Methode gibt eine Liste
     * von JobsUebersichtView zurück, die den angegebenen Kriterien entsprechen. Wenn ein Kriterium null oder leer ist, wird dieses
     * Kriterium beim Filtern ignoriert.
     *
     * @param orte eine Menge Ort, nach dem gefiltert werden soll.
     * @param kategorien Eine Menge Jobkategorie, nach der gefiltert werden soll.
     * @param studienfacher Eine Menge von Studienfächern, nach denen gefiltert werden soll.
     * @param homeOffice
     * @param branchen
     * @return Eine Liste von JobsUebersichtView, die den angegebenen Filterkriterien entsprechen. Die Liste kann leer sein, wenn keine
     *         JobsUebersichtView gefunden wurden, die den Kriterien entsprechen.
     */
    public List<Job> filterJobs(Set<Ort> orte, Set<JobKategorie> kategorien, Set<Studienfach> studienfacher, boolean homeOffice, Set<Branche> branchen) {
        Specification<Job> spec = Specification.where(JobSpecification.inOrte(orte))
                .and(JobSpecification.inJobKategorien(kategorien))
                .and(JobSpecification.inStudienfacher(studienfacher))
                .and(JobSpecification.isHomeOffice(homeOffice))
                .and(JobSpecification.inBranchen(branchen));

        return jobRepository.findAll(spec);
    }
}
