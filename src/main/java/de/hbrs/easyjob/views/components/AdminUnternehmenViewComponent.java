package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.services.UnternehmenService;

@StyleSheet("AdminLayout.css")
public class AdminUnternehmenViewComponent extends AdminUnternehmenComponent {
    public AdminUnternehmenViewComponent(
            Unternehmen unternehmen,
            ProfilSperrenController profilSperrenController,
            UnternehmenService unternehmenService
    ) {
        super(unternehmen, profilSperrenController, unternehmenService);
    }
}
