package de.hbrs.easyjob.views.student;


import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.allgemein.EinstellungenUebersichtView;

import javax.annotation.security.RolesAllowed;


@Route("student/einstellungen")
@RolesAllowed("ROLE_STUDENT")
public class EinstellungenUebersichtStudentView extends EinstellungenUebersichtView {


    public EinstellungenUebersichtStudentView() {
        super("#A3336F", "ROLE_STUDENT");

    }

}
