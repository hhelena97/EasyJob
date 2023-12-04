package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.JobKategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface JobKategorieRepository extends JpaRepository<JobKategorie, Integer> {
    JobKategorie findByKategorie(String kategorie);
}
