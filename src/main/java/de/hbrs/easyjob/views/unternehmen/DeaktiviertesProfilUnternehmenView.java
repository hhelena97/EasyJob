package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import de.hbrs.easyjob.views.templates.DeaktiviertesProfil;

@Route(value = "Unternehmen/Deaktiviert", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
public class DeaktiviertesProfilUnternehmenView extends DeaktiviertesProfil {
    public DeaktiviertesProfilUnternehmenView() {
        super();
    }
}
