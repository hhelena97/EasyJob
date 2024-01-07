package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "MeldungDetail", layout = AdminLayout.class)
@PageTitle("Meldung bearbeiten")
@StyleSheet("Variables.css")
@StyleSheet("MeldungDetailView.css")
//Reihenfolge wichtig. Das erste kann im zweiten verwendet werden
//@RolesAllowed("ROLE_ADMIN")
public class MeldungDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;
    private final String gemeldet;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!sessionController.isLoggedIn() || sessionController.hasRole("ROLE_ADMIN")){
            //beforeEnterEvent.rerouteTo(LoginView.class);
            //später: erst prüfen, ob Admin, sonst weiterleiten zur Startseite
        }
    }

    @Autowired
    public MeldungDetailView(SessionController sessionController, String gemeldet){
        this.sessionController = sessionController;
        this.gemeldet = gemeldet;

        Div bericht = new Div();

        Icon zurueck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zurueck.addClassName("zurueckAdmin");

        bericht.addClassName("bericht"); //das ist für die CSS-Datei

        H3 titel = new H3("Meldung");
        Paragraph leer = new Paragraph("");


        Paragraph meldung = new Paragraph("Gemeldet: ");

        Paragraph werWas = new Paragraph(gemeldet);

        bericht.add(titel, leer, meldung, werWas);

        Button btnSperren = new Button("Profil sperren", new Icon (VaadinIcon.LOCK));
        btnSperren.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSperren.addClassName("btnSperren");

        Button btnFertig = new Button ("Fall schließen", new Icon (VaadinIcon.CHECK));
        btnFertig.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnFertig.addClassName("btnFertig");



        add(zurueck, bericht, btnSperren, btnFertig); // kommt vom VerticalLayout, alle Komponenten hier einfügen.
    }
}
