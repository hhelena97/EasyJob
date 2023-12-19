package de.hbrs.easyjob.views.student;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.templates.DeaktiviertesProfil;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;


@Route(value = "student/inaktiv", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
@RolesAllowed("ROLE_STUDENT")
public class DeaktiviertesProfilStudentView extends DeaktiviertesProfil implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if(context != null) {
            Authentication auth = context.getAuthentication();
            if (auth == null || !auth.isAuthenticated() || !hasRole(auth)) {
                event.rerouteTo(LoginView.class);
            }
        } else {
            event.rerouteTo(LoginView.class);
        }
    }
    private boolean hasRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
    }

    public DeaktiviertesProfilStudentView() {
        super();
    }
}
