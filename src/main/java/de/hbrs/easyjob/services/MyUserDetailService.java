package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(username);
        if (person == null) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden");
        }
        return new User(person.getEmail(), person.getPasswort(), getAuthorities(person));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Person person) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (person instanceof Student) {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        } else if (person instanceof Unternehmensperson) {
            authorities.add(new SimpleGrantedAuthority("ROLE_UNTERNEHMENSPERSON"));
        } else if (person instanceof Admin){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }


}
