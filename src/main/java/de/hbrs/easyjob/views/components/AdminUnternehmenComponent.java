package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.JobProfilController;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.admin.EinstellungenStartView;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;
import org.springframework.stereotype.Component;


public class AdminUnternehmenComponent extends VerticalLayout {

    private final String style;
    VerticalLayout beschreibung = new VerticalLayout();

    private Unternehmen unternehmen;
    private final ProfilDeaktivierenController deaktivierenController;
    private final UnternehmenService unternehmenService;

    public AdminUnternehmenComponent(Unternehmen unternehmen, String styleClass, ProfilDeaktivierenController sperrenController,
                                     UnternehmenService unternehmenService){
        this.unternehmen = unternehmen;
        this.style = styleClass;
        this.deaktivierenController = sperrenController;
        this.unternehmenService = unternehmenService;
        initializeComponent();
    }

    private void initializeComponent() {

        if (unternehmen == null) {
            UI.getCurrent().navigate(PersonenVerwaltenView.class);
            return;
        }

        UI.getCurrent().getPage().addStyleSheet(style);

        Div oben = new Div();

        //Bild des Unternehmens
        Div bildUnternehmen = new Div();
        bildUnternehmen.addClassName("bildUnternehmen");
        bildUnternehmen.setWidth(getMaxWidth());

        //Name des Unternehmens
        H2 TitelUnternehmen = new H2();
        TitelUnternehmen.addClassName("TitelUnternehmen");
        TitelUnternehmen.add(unternehmen.getName());

        //Dialog zum Nachfragen beim Sperren
        Dialog d = new Dialog();
        d.add(new Paragraph("Wollen Sie " + unternehmen.getName() +
                " sperren? Damit werden auch alle Personen zu diesem Unternehmen gesperrt."));

        Button btnAbbruch2 = new Button("abbrechen");
        btnAbbruch2.addClassName("buttonAbbruch");
        btnAbbruch2.addClickListener(e -> {
            d.close();
        });

        Button btnBestaetigen = new Button("Unternehmen sperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            if (deaktivierenController.profilDeaktivierenUnternehmen(unternehmen.getUnternehmensperson())){
                d.close();
            } else {
                Notification.show("Die Person konnte nicht gesperrt werden");
            }
        });
        d.getFooter().add(btnAbbruch2, btnBestaetigen);

        Button btnSperren = new Button("Unternehmen sperren");
        btnSperren.addClassName("btnSperren");
        btnSperren.addClickListener(e -> d.open());

        oben.add(bildUnternehmen, TitelUnternehmen, btnSperren);

        //Beschreibung
        Paragraph beschreibung = new Paragraph();
        beschreibung.add(unternehmen.getBeschreibung());
        beschreibung.addClassName("beschreibung");


        HorizontalLayout locationPlusAnzahl = new HorizontalLayout();
        locationPlusAnzahl.addClassName("locationPlusAnzahl");
        locationPlusAnzahl.setSpacing(false);
        locationPlusAnzahl.setPadding(false);
        locationPlusAnzahl.setMargin(false);

        IconFactory lo = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon loc =  lo.create();
        loc.addClassName("iconsInUnternehmen");

        Paragraph stadt = new Paragraph();
        stadt.addClassName("stadt");
        stadt.add(unternehmenService.getFirstStandort(unternehmen).getOrt());

        Paragraph anzahlAngebote = new Paragraph();
        anzahlAngebote.addClassName("anzahlAngebote");
        anzahlAngebote.add(unternehmenService.anzahlJobs(unternehmen)+" Stellenangebot");

        locationPlusAnzahl.add(loc, stadt, anzahlAngebote);


        add(oben, beschreibung, locationPlusAnzahl);

    }


}
