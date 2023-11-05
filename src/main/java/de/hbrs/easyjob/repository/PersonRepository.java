package de.hbrs.easyjob.repository;
import de.hbrs.easyjob.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findByEmail(String mail);
}
