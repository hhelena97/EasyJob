package de.hbrs.easyjob.views.student;


import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.EinstellungenUebersichtView;

import javax.annotation.security.RolesAllowed;


@Route("student/einstellungen")
@RolesAllowed("ROLE_STUDENT")
@PageTitle("Einstellungen")
public class EinstellungenUebersichtStudentView extends EinstellungenUebersichtView {
    public EinstellungenUebersichtStudentView(SessionController sessionController) {
        super(sessionController, "Student");
    }
}
