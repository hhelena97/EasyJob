package de.hbrs.easyjob.views.unternehmen;


import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.allgemein.EinstellungenUebersichtView;

import javax.annotation.security.RolesAllowed;


@Route("unternehmen/einstellungen")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class EinstellungenUebersichtUnternehmenView extends EinstellungenUebersichtView {

    public EinstellungenUebersichtUnternehmenView() {
        super("#2D34A0","ROLE_UNTERNEHMENSPERSON");
    }

}
