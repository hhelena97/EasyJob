package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.Branche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface BrancheRepository extends JpaRepository<Branche, Integer> {
    Branche findByName(String branche);
}
