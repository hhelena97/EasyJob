package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.views.templates.RegistrierenView;

@Route("Unternehmen/Neu-2")
@PageTitle("Unternehmen anlegen 2")
@StyleSheet("UnternehmenRegistrieren.css")
public class UnternehmenAnlegen2View extends RegistrierenView {

    Image logo;
    private Unternehmen unternehmen;
    public UnternehmenAnlegen2View() {
        super();
        this.unternehmen = ComponentUtil.getData(UI.getCurrent(), Unternehmen.class);
        super.setLastView("Unternehmen/Neu-1");
        super.setNextView("Unternehmen/Registrieren-1");
        super.setHeader("Ihr Logo...");
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Unternehmen");
        super.next.addClickListener(e -> logoSpeichern());
    }

    @Override
    public void insertContent() {

        logo = new Image("images/blank-logo.jpeg", "Logo Platzhalter");
        Div logoDiv = new Div(logo);
        logoDiv.addClassName("picture-square");

        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        super.frame.add(logoDiv, bildBearbeiten);
    }

    private void logoSpeichern(){
        unternehmen.setLogo(logo.getSrc());
        getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
    }
}
