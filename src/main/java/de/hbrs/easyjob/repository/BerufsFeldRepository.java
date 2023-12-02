package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.BerufsFelder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface BerufsFeldRepository extends JpaRepository<BerufsFelder, Integer> {
    BerufsFelder findByName(String berufsFelder);
}
