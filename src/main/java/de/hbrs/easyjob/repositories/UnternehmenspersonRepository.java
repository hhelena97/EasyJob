package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.entities.Unternehmen;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface UnternehmenspersonRepository extends JpaRepository<Unternehmensperson, Integer> {

    @Query("SELECT p FROM Unternehmensperson p WHERE p.unternehmensperson.id_Unternehmen = :unternehmenID")
    List<Unternehmensperson> findAllByUnternehmen(int unternehmenID);
}
