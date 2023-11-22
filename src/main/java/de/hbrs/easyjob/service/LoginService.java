package de.hbrs.easyjob.service;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginService {
    private final PersonRepository repository;

    @Autowired
    public LoginService(PersonRepository personRepository){
        this.repository = personRepository;
    }

    public Person findByEmail(String email){
        return repository.findByEmail(email);
    }
}
