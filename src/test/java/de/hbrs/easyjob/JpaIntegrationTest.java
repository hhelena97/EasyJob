package de.hbrs.easyjob;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repository.PersonRepository;
import de.hbrs.easyjob.repository.StudentRepository;
import de.hbrs.easyjob.repository.StudienfachRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JpaIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StudienfachRepository studienfachRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Test
    @Transactional
    public void testJpaIntegration() {
        // Erstellen und speichern einer Person
        Person person = new Person();
        person.setVorname("Sina");
        person.setNachname("Kosari");
        person.setEmail("sina@example.com");
        personRepository.save(person);

        Person sina = personRepository.findById(person.getId_Person()).orElse(null);
        assertNotNull(sina);
        assertEquals("Sina", sina.getVorname());

        Studienfach fach = new Studienfach();
        fach.setFach("Informatik");
        fach.setAbschluss("Bachelor");
        studienfachRepository.save(fach);

        Student student = new Student();
        student.setVorname("Max");
        student.setNachname("Mustermann");
        student.setEmail("Max@example.com");
        student.setStudienfach(fach);
        studentRepository.save(student);

        Person stu = personRepository.findById(student.getId_Person()).orElse(null);
        assertNotNull(stu);
        assertEquals("Max", stu.getVorname());

        personRepository.deleteById(person.getId_Person());
        Person deletedPerson = personRepository.findById(person.getId_Person()).orElse(null);
        Assertions.assertNull(deletedPerson);
    }
}
