package de.hbrs.easyjob.controllers;

import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {
    private final PersonRepository personRepository;

    public SessionController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public boolean isLoggedIn() {
        SecurityContext securityContext = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            return auth != null && auth.isAuthenticated();
        }
        return false;
    }

    public boolean hasRole(String role) {
        SecurityContext securityContext = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                return auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(role));
            }
        }
        return false;
    }

    public Person getPerson() {
        SecurityContext securityContext = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                User user = (User) auth.getPrincipal();
                String email = user.getUsername();
                return personRepository.findByEmail(email);
            }
        }
        return null;
    }
}
