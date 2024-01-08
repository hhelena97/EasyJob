package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.views.components.AdminLayout;


@Route(value = "admin/personenSuchen", layout = AdminLayout.class)
@PageTitle("Personen Suchen")
@StyleSheet("Variables.css")
@StyleSheet("AdminPersonenSuchen.css")
//@RolesAllowed("ROLE_ADMIN")

public class PersonenSuchenView extends VerticalLayout implements BeforeEnterObserver {
    private final PersonRepository prepo;
    private final SessionController sessionController;

    private final VerticalLayout ergebnisLayout;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            //event.rerouteTo(LoginView.class);
        }
    }

    public PersonenSuchenView(SessionController sessionController, PersonRepository personRepository){
        this.sessionController = sessionController;
        this.prepo = personRepository;

        H3 titel = new H3 ("Personen Suchen");
        titel.addClassName("titel");

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        TextField searchField = new TextField();
        searchField.setPlaceholder("E-Mail der gesuchten Person");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> personRepository.findByEmail(searchField.getValue()));


        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");


        Div gruenerBlock = new Div(titel, searchLayout);
        gruenerBlock.addClassName("gruenerblock");


        // Liste für Ergebnisse (kann aber eigentlich immer nur ein Ergebnis sein
        ergebnisLayout = new VerticalLayout();
        ergebnisLayout.setWidthFull();
        ergebnisLayout.setAlignItems(Alignment.STRETCH);
        ergebnisLayout.setClassName("ergebnis-layout");

        // Person laden und anzeigen
        //loadPerson();


        add(gruenerBlock,ergebnisLayout);
    }

    /*
    private void loadPerson(){
        //TODO: hier muss noch die Person geladen werden
        Person person;

        //zuerst das Bild in klein

        // RouterLink für die Aktionen
        RouterLink linkPerson = new RouterLink("", PersonVerwaltenView.class, person.getId_Person());
        linkPerson.add(new H1(person.getVorname() + " " + person.getNachname()));
        linkPerson.addClassName("link-zur-person");

        Icon i = new Icon(VaadinIcon.UNLOCK);

        if (!person.getAktiv()){
            i = new Icon(VaadinIcon.LOCK);
        }

        ergebnisLayout.add(linkPerson, i);
    }

     */

}
