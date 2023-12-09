package de.hbrs.easyjob.views.student;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.templates.DeaktiviertesProfil;
import de.hbrs.easyjob.views.components.UnternehmenLayout;


@Route(value = "student/inaktiv", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
public class DeaktiviertesProfilStudentView extends DeaktiviertesProfil {

    public DeaktiviertesProfilStudentView() {
        super();
    }
}
