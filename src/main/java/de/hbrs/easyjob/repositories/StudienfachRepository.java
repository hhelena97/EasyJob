package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Studienfach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface StudienfachRepository extends JpaRepository<Studienfach, Long> {
    Studienfach findByFachAndAbschluss(String fach, String abschluss);

    Set<Studienfach> findAllByAbschluss(String abschluss);
}

