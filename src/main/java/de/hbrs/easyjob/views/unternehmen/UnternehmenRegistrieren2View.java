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
import de.hbrs.easyjob.control.RegistrierungsControllerUnternehmen;
import de.hbrs.easyjob.control.RegistrierungsControllerUnternehmensPerson;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repository.BrancheRepository;
import de.hbrs.easyjob.repository.OrtRepository;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import de.hbrs.easyjob.repository.UnternehmenspersonRepository;
import de.hbrs.easyjob.views.allgemein.DialogLayout;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;
import org.springframework.beans.factory.annotation.Autowired;

@Route("Unternehmen/Registrieren-2")
@PageTitle("Unternehmen Registrieren 2")
@StyleSheet("UnternehmenRegistrieren.css")
public class UnternehmenRegistrieren2View extends RegistrierenView {

    private Image profilBild;
    private Unternehmen unternehmen;
    private boolean istNeuesUnternehmen;
    private UnternehmenRepository unternehmenRepository;
    private UnternehmenspersonRepository unternehmenspersonRepository;
    private BrancheRepository brancheRepository;
    private OrtRepository ortRepository;
    private DialogLayout finishDialog = new DialogLayout(true);

    @Autowired
    public UnternehmenRegistrieren2View(UnternehmenRepository unternehmenRepository, UnternehmenspersonRepository unternehmenspersonRepository, BrancheRepository brancheRepository, OrtRepository ortRepository) {
        super();
        this.unternehmen = ComponentUtil.getData(UI.getCurrent(), Unternehmen.class);
        this.istNeuesUnternehmen = ComponentUtil.getData(UI.getCurrent(), Boolean.class);
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("Unternehmen/Registrieren-1");
        super.setHeader("Ihr persönliches Profilbild...");
        insertContent();
        super.addFertigButton();
        super.addButtons();
        super.setAbbrechenDialog("Unternehmen");
        super.fertig.addClickListener(e -> finishRegistration());
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.brancheRepository = brancheRepository;
        this.ortRepository = ortRepository;
    }
    @Override
    public void insertContent() {

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

        super.frame.add(rahmen, bildBearbeiten);
    }

    private void finishRegistration(){
        RegistrierungsControllerUnternehmensPerson registrierungsControllerUnternehmensPerson =
        new RegistrierungsControllerUnternehmensPerson(unternehmenspersonRepository, unternehmenRepository);
        RegistrierungsControllerUnternehmen registrierungsControllerUnternehmen =
                new RegistrierungsControllerUnternehmen(unternehmenRepository, brancheRepository, ortRepository);
        super.person.setFoto(profilBild.getSrc());
        if(istNeuesUnternehmen) {
            registrierungsControllerUnternehmen.createUnternehmen(unternehmen);
            ((Unternehmensperson) super.person).setUnternehmen(unternehmen);
        }else {
            ((Unternehmensperson) super.person).setUnternehmen(unternehmen);
        }
        registrierungsControllerUnternehmensPerson.createUnternehmensPerson(((Unternehmensperson) super.person), true);
        registrierungsControllerUnternehmen.setUnternehmensperson(unternehmen, ((Unternehmensperson) super.person));
        Button weiterZumLogin = new Button("Weiter zum Login");
        weiterZumLogin.addClassName("close-unternehmen");
        finishDialog.simpleDialog("Account erfolgreich angelegt!", weiterZumLogin, "login");
        finishDialog.openDialogOverlay();
    }
}
