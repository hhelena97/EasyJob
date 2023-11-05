package de.hbrs.easyjob.control;

import de.hbrs.easyjob.repository.PersonRepository;
import org.springframework.stereotype.Component;

@Component
public class LoginControl {
    public LoginControl(PersonRepository personRepository) {
    }

    public boolean authenticate(String email, String password) {
        return true;
    }
}
