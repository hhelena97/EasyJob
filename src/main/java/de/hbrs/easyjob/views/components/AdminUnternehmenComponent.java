package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
//import de.hbrs.easyjob.controllers.JobProfilController;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.admin.PersonenSuchenView;


public class AdminUnternehmenComponent extends VerticalLayout {

    private final String style;
    VerticalLayout beschreibung = new VerticalLayout();

    private Unternehmen unternehmen;
    private final ProfilDeaktivierenController deaktivierenController;
    private final UnternehmenService unternehmenService;

    public AdminUnternehmenComponent(Unternehmen unternehmen, String styleClass, ProfilDeaktivierenController deaktivierenController,
                                     UnternehmenService unternehmenService){
        this.unternehmen = unternehmen;
        this.style = styleClass;
        this.deaktivierenController = deaktivierenController;
        this.unternehmenService = unternehmenService;
        initializeComponent();
    }

    private void initializeComponent() {

        if (unternehmen == null) {
            UI.getCurrent().navigate(PersonenSuchenView.class);
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
        Div nachfragen = new Div();
        Paragraph p;
        Button btnDialogSperren;
        Unternehmensperson manager = unternehmen.getUnternehmensperson();

        p = new Paragraph ("Wollen Sie " + unternehmen.getName() +
                    " sperren? Damit werden auch alle Personen zu diesem Unternehmen gesperrt.");
        btnDialogSperren = new Button("sperren", e-> deaktivierenController.profilDeaktivierenUnternehmen(manager));

        nachfragen.add(p, btnDialogSperren);
        DialogLayout d = new DialogLayout(true);

        Button btnSperren = new Button("sperren", e-> d.insertContentDialogContent("", nachfragen, "abbrechen", "???"));
        btnSperren.addClassName("btnSperren");

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
