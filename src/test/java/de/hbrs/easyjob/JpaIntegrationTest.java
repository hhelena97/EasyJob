package de.hbrs.easyjob;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JpaIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;



    @Test
    @Transactional
    public void testJpaIntegration() {
        // Erstellen und speichern einer Person
        Person person = new Person();
        person.setVorname("Sina");
        person.setNachname("Kosari");
        person.setEmail("sina@example.com");
        personRepository.save(person);
        entityManager.flush();
        entityManager.clear();
        Person sina = personRepository.findById(person.getId_Person()).orElse(null);
        assertNotNull(sina);
        assertEquals("Sina", sina.getVorname());

    }
}
