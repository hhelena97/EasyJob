package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.services.UnternehmenService;

@StyleSheet("AdminPersonenVerwaltenView.css")
public class AdminUnternehmenManageComponent extends AdminUnternehmenComponent {
    public AdminUnternehmenManageComponent(
            Unternehmen unternehmen,
            ProfilSperrenController profilSperrenController,
            UnternehmenService unternehmenService
    ) {
        super(unternehmen, profilSperrenController, unternehmenService);
    }
}
