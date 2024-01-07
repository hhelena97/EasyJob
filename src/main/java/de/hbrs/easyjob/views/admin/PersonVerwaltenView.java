package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.ZurueckButtonText;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "person", layout = AdminLayout.class)
@StyleSheet("Variables.css")
@StyleSheet("AdminProfileVerwalten.css")
//Reihenfolge wichtig. Das erste kann im zweiten verwendet werden
//@RolesAllowed("ROLE_ADMIN")
public class PersonVerwaltenView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;
    private Person person;
    private final PersonRepository personRepository;

    private final MeldungController meldungController;

    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn() || sessionController.hasRole("ROLE_ADMIN")){
            //beforeEnterEvent.rerouteTo(LoginView.class);
            //für später: um erst zu prüfen, ob Admin, sonst weiterleiten zur Startseite
        }
    }

    @Autowired
    public PersonVerwaltenView(SessionController sescont, Person p, PersonRepository prepo, MeldungController mcont){
        this.sessionController = sescont;
        this.personRepository = prepo;
        this.meldungController = mcont;
        this.person = p;
        String text = "";
        Icon icon = new Icon (VaadinIcon.USER);

        if (p == null) {
            UI.getCurrent().navigate(EinstellungenStartView.class);
        }

        Icon zurueck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zurueck.addClassName("zurueckAdmin");
        zurueck.addClickListener(e -> zurueck.getUI().ifPresent(ui -> ui.navigate("admin/personenSuchen")));

        Div profilBild = new Div();
        profilBild.addClassName("profilBild");

        profilBild.add(new Image(person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png", "EasyJob"));

        H1 name = new H1();
        name.addClassName("name");
        name.add(person.getVorname() + " " + person.getNachname());

        Button btnPasswort = new Button("Passwort ändern", new Icon (VaadinIcon.USER));
        btnPasswort.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPasswort.addClassName("btnPasswort");

        if (person.getAktiv()){
            text = "Profil sperren";
            icon = new Icon(VaadinIcon.LOCK);
        } else {
            text = "Profil reaktivieren";
            icon = new Icon(VaadinIcon.UNLOCK);
        }

        Button btnSperren = new Button (text, icon);
        btnSperren.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSperren.addClassName("btnSperren");

        add(zurueck, profilBild,name, btnPasswort, btnSperren);
    }

}
