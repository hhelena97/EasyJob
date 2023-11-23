package de.hbrs.easyjob.repository;
import de.hbrs.easyjob.dtos.PersonDTO;
import de.hbrs.easyjob.dtos.impl.PersonDTOimpl;
import de.hbrs.easyjob.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findByEmail(String mail);

    PersonDTOimpl findPersonByEmail (String mail);

}
