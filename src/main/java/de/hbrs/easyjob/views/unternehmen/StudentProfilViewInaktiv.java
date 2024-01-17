package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;

import javax.annotation.security.RolesAllowed;

@Route(value = "inaktiv-student-profil")
@PageTitle("Inaktiv Student Profil")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class StudentProfilViewInaktiv extends VerticalLayout implements BeforeEnterObserver {

    VerticalLayout profilLayout = new VerticalLayout();

    private final transient SessionController sessionController;

    public StudentProfilViewInaktiv(SessionController sessionController) {

        this.sessionController = sessionController;
        initializeView();
    }


    private void initializeView() {

        addClassName("student-profil-view-inaktiv");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //Profil Bild
        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        Image platzhalterBild = new Image("images/blank-profile-picture.png", "blank-profile-picture");
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        // Zurück Button
        ZurueckButtonRundLayout zurueckButton = new ZurueckButtonRundLayout("Unternehmen");
        zurueckButton.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());
        add(zurueckButton);

        // Inaktiv-Text
        H3 inaktivText = new H3("Dieses Profil ist inaktiv.");
        inaktivText.addClassName("inaktiv-text");
        add(inaktivText);

        profilLayout.add(rahmen);
        profilLayout.add(inaktivText);
        profilLayout.setAlignItems(Alignment.CENTER);
        add(profilLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn()|| !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")){
            event.rerouteTo(LoginView.class);
        }
    }
}
