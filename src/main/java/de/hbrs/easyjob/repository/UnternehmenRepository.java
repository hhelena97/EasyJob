package de.hbrs.easyjob.repository;


import de.hbrs.easyjob.entities.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UnternehmenRepository extends JpaRepository<Unternehmen, Integer> {
    Unternehmen findByName(String name);
}
