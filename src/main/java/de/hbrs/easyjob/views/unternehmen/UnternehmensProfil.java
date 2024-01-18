package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.AbstractUnternehmenProfilView;
import de.hbrs.easyjob.views.components.UnternehmenLayout;

import javax.annotation.security.RolesAllowed;

@Route(value = "unternehmen/unternehmensprofil", layout = UnternehmenLayout.class)
@PageTitle("Unternehmensprofil")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class UnternehmensProfil extends AbstractUnternehmenProfilView {
    public UnternehmensProfil(UnternehmenService unternehmenService,
                              SessionController sessionController,
                              MeldungController meldungController){
        super(unternehmenService, sessionController, meldungController);
    }

}
