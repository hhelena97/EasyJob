package de.hbrs.easyjob.controllers;

import com.vaadin.flow.server.VaadinSession;

import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {

    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    public LogoutController() {}


    // -----------------------------------------------------------------------------------------------------------------
    // Bitte löschen, sobald die Tests für die richtige Methode logout() fertig sind. :)
    private PersonRepository personRepository;
    public LogoutController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    // -----------------------------------------------------------------------------------------------------------------


    /**
     * Die Methode logout() wird aufgerufen, wenn der Benutzer sich ausloggen möchte.
     * Sie ruft die Methode logout() der Klasse SecurityContextLogoutHandler auf.
     * Diese Methode löscht den SecurityContext und die Session des Benutzers.
     * Die Methode clearContext() aus CustomSecurityContextRepository wird genutzt, um den SecurityContext zu löschen.
     *
     * @param request   - aktuelle Anfrage
     * @param response  - aktuelle Antwort
     *
     */
    public void logout (HttpServletRequest request, HttpServletResponse response) {

        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if(context != null) {
            Authentication auth = context.getAuthentication();

            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                customSecurityContextRepository.clearContext();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Bitte löschen, sobald die Tests für die richtige Methode logout() fertig sind. :)
    public boolean logout() {
        return true;
    }
    // -----------------------------------------------------------------------------------------------------------------
}
