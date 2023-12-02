package de.hbrs.easyjob.repository;

import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.entities.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface UnternehmenspersonRepository extends JpaRepository<Unternehmensperson, Integer> {

    public Unternehmensperson findAllByUnternehmen(Unternehmen unternehmen);
}
