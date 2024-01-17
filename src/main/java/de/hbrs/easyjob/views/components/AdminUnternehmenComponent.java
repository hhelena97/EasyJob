package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;


public abstract class AdminUnternehmenComponent extends VerticalLayout {
    private final transient Unternehmen unternehmen;
    private final transient ProfilSperrenController profilSperrenController;
    private final transient UnternehmenService unternehmenService;

    AdminUnternehmenComponent(
            Unternehmen unternehmen,
            ProfilSperrenController profilSperrenController,
            UnternehmenService unternehmenService
    ) {
        this.unternehmen = unternehmen;
        this.profilSperrenController = profilSperrenController;
        this.unternehmenService = unternehmenService;
        initializeComponent();
    }

    private void initializeComponent() {

        if (unternehmen == null) {
            UI.getCurrent().navigate(PersonenVerwaltenView.class);
            return;
        }

        Div oben = new Div();

        //Bild des Unternehmens
        Div bildUnternehmen = new Div();
        bildUnternehmen.addClassName("bildUnternehmen");
        bildUnternehmen.setWidth(getMaxWidth());

        //Name des Unternehmens
        H2 titelUnternehmen = new H2();
        titelUnternehmen.addClassName("TitelUnternehmen");
        titelUnternehmen.add(unternehmen.getName());

        //Dialog zum Nachfragen beim Sperren
        Dialog d = new Dialog();
        d.add(new Paragraph("Möchten Sie " + unternehmen.getName() +
                " sperren? Damit werden auch alle Personen zu diesem Unternehmen gesperrt."));

        Button btnAbbruch2 = new Button("Abbrechen");
        btnAbbruch2.addClassName("close-admin");
        btnAbbruch2.addClickListener(e -> d.close());

        Button btnBestaetigen = new Button("Unternehmen sperren");
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            if (profilSperrenController.unternehmenSperren(unternehmen)) {
                d.close();
            } else {
                Notification.show("Der Account konnte nicht gesperrt werden");
            }
        });
        d.getFooter().add(btnBestaetigen, btnAbbruch2);

        Button btnSperren = new Button("Unternehmen sperren");
        btnSperren.addClassName("btnSperren");
        btnSperren.addClickListener(e -> d.open());

        oben.add(bildUnternehmen, titelUnternehmen, btnSperren);

        //Beschreibung
        Paragraph beschreibung = new Paragraph();
        beschreibung.add(unternehmen.getBeschreibung());
        beschreibung.addClassName("beschreibung");


        Paragraph anzahlAngebote = new Paragraph();
        anzahlAngebote.addClassName("anzahlAngebote");
        anzahlAngebote.add(unternehmenService.anzahlJobs(unternehmen) + " Stellenangebot(e)");


        add(oben, beschreibung, anzahlAngebote);

    }


}
