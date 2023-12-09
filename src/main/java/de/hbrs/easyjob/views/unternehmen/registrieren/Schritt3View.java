package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

public class Schritt3View extends RegistrierenSchritt {
    Image logo;
    private final Unternehmen unternehmen;
    public Schritt3View(Unternehmen unternehmen) {
        this.unternehmen = unternehmen;
        insertContent();
    }

    @Override
    public void insertContent() {

        logo = new Image("images/blank-logo.jpeg", "Logo Platzhalter");
        Div logoDiv = new Div(logo);
        logoDiv.addClassName("picture-square");

        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        add(logoDiv, bildBearbeiten);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        unternehmen.setLogo(logo.getSrc());
        return true;
    }
}
