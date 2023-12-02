package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.JobKategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface JobKategorieRepository extends JpaRepository<JobKategorie, Integer> {
    JobKategorie findByKategorie(String kategorie);
}
