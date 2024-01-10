package de.hbrs.easyjob.views.student;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import de.hbrs.easyjob.views.templates.DeaktiviertesProfil;

import javax.annotation.security.RolesAllowed;


@Route(value = "student/inaktiv", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
@RolesAllowed("ROLE_STUDENT")
public class DeaktiviertesProfilStudentView extends DeaktiviertesProfil implements BeforeEnterObserver {

    public DeaktiviertesProfilStudentView(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_STUDENT")) {
            event.rerouteTo(LoginView.class);
        }
    }
}
