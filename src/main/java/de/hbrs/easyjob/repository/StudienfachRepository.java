package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.Studienfach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface StudienfachRepository extends JpaRepository<Studienfach, Long>{
}

