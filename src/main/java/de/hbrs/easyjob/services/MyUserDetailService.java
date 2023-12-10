package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private final PersonRepository personRepository;

    public MyUserDetailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Person person = personRepository.findByEmail(username);
        UserDetails user = User.withUsername(person.getEmail()).password(person.getPasswort()).authorities("USER").build();
        return user;
    }

}
