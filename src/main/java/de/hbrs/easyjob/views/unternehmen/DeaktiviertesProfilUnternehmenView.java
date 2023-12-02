package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.views.allgemein.DeaktiviertesProfilView;

@Route(value = "Unternehmen/Deaktiviert", layout = UnternehmenLayout.class)
@PageTitle("Fehler: inaktives Profil")
public class DeaktiviertesProfilUnternehmenView extends DeaktiviertesProfilView {
    public DeaktiviertesProfilUnternehmenView() {
        super();
    }
}
