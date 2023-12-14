package de.hbrs.easyjob.services;

import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    @Transactional
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAllPersons();
    }

    public Person getCurrentPerson() {
        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);

        if (context != null && context.getAuthentication() != null) {
            Object principal = context.getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            Person person = personRepository.findByEmail(username);
            if (person == null) {
                logger.debug("Kein Student mit dieser E-Mail gefunden."); // Logging
            }
            return person;

        }
        logger.debug("SecurityContext oder Authentication ist null.");
        return null;
    }
    public List<Person> saveAllPersons(List<Person> persons) {
        return personRepository.saveAll(persons);
    }
}
