package de.hbrs.easyjob.controllers;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;

/**
 * Klasse zur Verwaltung der Session des Benutzers.
 * Sie wird genutzt, um den Benutzer anzumelden, abzumelden und zu prüfen, ob er eingeloggt ist.
 * Außerdem wird geprüft, ob der Benutzer eine bestimmte Rolle hat.
 * Sie kann das Person-Objekt des eingeloggten Benutzers zurückgeben.
 */
@Controller
public class SessionController {
    private final PersonRepository personRepository;
    private final CustomSecurityContextRepository customSecurityContextRepository;
    private final AuthenticationManager authenticationManager;

    public SessionController(PersonRepository personRepository, CustomSecurityContextRepository customSecurityContextRepository, AuthenticationManager authenticationManager) {
        this.personRepository = personRepository;
        this.customSecurityContextRepository = customSecurityContextRepository;
        this.authenticationManager = authenticationManager;
    }

    /** Meldet den Benutzer an, falls die Authentifizierung erfolgreich ist.
     * @param email E-Mail-Adresse des Benutzers
     * @param password Passwort des Benutzers
     * @return true, falls die Authentifizierung erfolgreich ist, sonst false
     */
    public boolean login(String email, String password) {
        // Falls E-Mail / Passwort null oder Benutzer bereits eingeloggt, dann false zurückgeben
        if (email == null || password == null || isLoggedIn()) {
            return false;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            customSecurityContextRepository.saveContext(
                    securityContext,
                    VaadinServletRequest.getCurrent().getHttpServletRequest(),
                    VaadinServletResponse.getCurrent().getHttpServletResponse()
            );
            return isLoggedIn();
        } catch (AuthenticationException e) {
            // Falls Authentifizierung fehlschlägt, dann false zurückgeben
            return false;
        }
    }

    /** Meldet den Benutzer ab, falls er eingeloggt ist.
     * @return true, falls der Benutzer abgemeldet wurde, sonst false
     */
    public boolean logout() {
        if (isLoggedIn()) {
            new SecurityContextLogoutHandler().logout(
                    VaadinServletRequest.getCurrent().getHttpServletRequest(),
                    null,
                    null
            );
            customSecurityContextRepository.clearContext();
            return true;
        }
        return false;
    }

    /** Prüft, ob der Benutzer eingeloggt ist.
     * @return true, falls der Benutzer eingeloggt ist, sonst false
     */
    public boolean isLoggedIn() {
        SecurityContext securityContext = getSecurityContext();
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            return auth != null && auth.isAuthenticated();
        }
        return false;
    }

    /** Prüft, ob der Benutzer mindestens eine der angegebenen Rollen hat.
     * @param roles Rollen, die der Benutzer haben muss
     * @return true, falls der Benutzer mindestens eine der angegebenen Rollen hat, sonst false
     */
    public boolean hasRole(String... roles) {
        if (isLoggedIn()) {
            SecurityContext securityContext = getSecurityContext();
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                for (String role : roles) {
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Gibt das Person-Objekt des eingeloggten Benutzers zurück, falls vorhanden.
     * @return Person-Objekt des eingeloggten Benutzers, falls vorhanden, sonst null
     */
    public Person getPerson() {
        SecurityContext securityContext = getSecurityContext();
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

    private SecurityContext getSecurityContext() {
        return customSecurityContextRepository.loadContext(VaadinServletRequest.getCurrent().getHttpServletRequest()).get();
    }
}
