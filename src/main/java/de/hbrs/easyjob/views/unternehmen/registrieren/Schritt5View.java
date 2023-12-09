package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

public class Schritt5View extends RegistrierenSchritt {
    private Image profilBild;
    private final Unternehmensperson unternehmensperson;

    public Schritt5View(Unternehmensperson unternehmensperson) {
        this.unternehmensperson = unternehmensperson;
        insertContent();
    }

    @Override
    public void insertContent() {
        setAlignItems(Alignment.CENTER);

        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");

        Image ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        profilBild = new Image("images/blank-profile-picture.png", "Profilbild Platzhalter");
        Div bildDiv = new Div(profilBild);
        bildDiv.addClassName("picture-round");
        rahmen.add(bildDiv);

        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        add(rahmen, bildBearbeiten);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        unternehmensperson.setFoto(profilBild.getSrc());
        return true;
    }
}
