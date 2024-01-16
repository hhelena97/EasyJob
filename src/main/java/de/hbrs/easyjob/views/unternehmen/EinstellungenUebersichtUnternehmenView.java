package de.hbrs.easyjob.views.unternehmen;


import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.EinstellungenUebersichtView;

import javax.annotation.security.RolesAllowed;


@Route("unternehmen/einstellungen")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class EinstellungenUebersichtUnternehmenView extends EinstellungenUebersichtView {
    public EinstellungenUebersichtUnternehmenView(SessionController sessionController) {
        super(sessionController, "#2D34A0");
    }
}
