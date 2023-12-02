package de.hbrs.easyjob.views.student;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.allgemein.DeaktiviertesProfilView;
import de.hbrs.easyjob.views.unternehmen.UnternehmenLayout;


@Route(value = "Student/Deaktiviert", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
public class DeaktiviertesProfilStudentView extends DeaktiviertesProfilView {

    public DeaktiviertesProfilStudentView() {
        super();
    }
}
