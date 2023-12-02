package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("deaktiviert")
public class DeaktiviertesProfilView extends VerticalLayout {

    //private ProfilDeaktiviertControl pdControl;

    public DeaktiviertesProfilView() {
        UI.getCurrent().getPage().addStyleSheet("DeaktiviertesProfil.css");

        //Hintergrund vom Profilbild
        Div background = new Div();
        background.addClassName("background");
        background.setWidth(getMaxWidth());

        //Name
        H1 nameInaktiv = new H1("Profilname");
        nameInaktiv.addClassName("nameInaktiv");

        //es ist inaktiv
        H1 inaktiv = new H1("Dieses Profil ist inaktiv.");
        inaktiv.addClassName("inaktiv");


        VerticalLayout fenster = new VerticalLayout();
        fenster.add(background, nameInaktiv, inaktiv);
        fenster.setAlignItems(Alignment.CENTER);
        fenster.setJustifyContentMode(JustifyContentMode.CENTER);
        fenster.setSpacing(false);

        add(fenster);
    }
}
