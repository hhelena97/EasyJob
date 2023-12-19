package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;

@Route(value = "student/benachrichtigungen", layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
public class BenachrichtigungenView extends VerticalLayout implements BeforeEnterObserver {
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
}
