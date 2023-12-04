package de.hbrs.easyjob.repositories;
import de.hbrs.easyjob.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findByEmail(String mail);


    @Query("SELECT p FROM Person p WHERE p in (SELECT up FROM Unternehmensperson up WHERE up.unternehmen.id_Unternehmen = :unternehmen)")
    List<Person> findAllByUnternehmenId(Integer unternehmen);

}
