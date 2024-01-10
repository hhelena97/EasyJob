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
import de.hbrs.easyjob.services.PersonSuchenService;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.admin.PersonVerwaltenView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;


@Route(value = "admin/personenSuchen", layout = AdminLayout.class)
@PageTitle("Personen Suchen")
@StyleSheet("Variables.css")
@StyleSheet("AdminPersonenSuchen.css")
//@RolesAllowed("ROLE_ADMIN")

public class PersonenSuchenView extends VerticalLayout implements BeforeEnterObserver {
    private final PersonSuchenService personService;

    private final SessionController sessionController;

    private VerticalLayout personListLayout;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if(context != null) {
            Authentication auth = context.getAuthentication();
            if (auth == null || !auth.isAuthenticated() || !hasRole(auth)) {
                //event.rerouteTo(LoginView.class);
            }
        } else {
            //event.rerouteTo(LoginView.class);
        }
    }

    private boolean hasRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public PersonenSuchenView(SessionController sessionController, PersonSuchenService personService) {
        this.sessionController = sessionController;
        this.personService = personService;
        initializeView();
    }

    private void initializeView(){
        addClassName("person-suchen-view");

        H3 titel = new H3 ("Personen suchen");
        titel.addClassName("titel");

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        TextField searchField = new TextField();
        searchField.setPlaceholder("E-Mail der gesuchten Person");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> searchPerson(searchField.getValue()));

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");

        //alles in einen grünen Block
        Div gruenerBlock = new Div(titel, searchLayout);
        gruenerBlock.addClassName("gruenerblock");

        //Liste für Ergebnisse
        personListLayout = new VerticalLayout();
        personListLayout.setWidthFull();
        personListLayout.setAlignItems(Alignment.STRETCH);
        personListLayout.setClassName("ergebnis-list-layout");

        // Person laden und anzeigen
        loadPerson();


        add(gruenerBlock, personListLayout);
    }


    private void loadPerson() {
        Object sessionAttribute = VaadinSession.getCurrent().getAttribute("filteredPersonenIDs");
        if (sessionAttribute instanceof List) {
            List<Integer> personIds = (List<Integer>) sessionAttribute;
            if (!personIds.isEmpty()) {
                List<Person> personen = personService.getPersonenByIds(personIds);
                personen.forEach(this::addPersonComponentToLayout);

                // Entfernen der IDs aus der Sitzung, um zukünftige Konflikte zu vermeiden
                VaadinSession.getCurrent().setAttribute("filteredPersonenIds", null);
            }
        }else {
            List<Person> personen = personService.getAllPersonen();
            personen.forEach(this::addPersonComponentToLayout);
            VaadinSession.getCurrent().setAttribute("searchedPersonen", personen);
        }
    }

    private void searchPerson(String searchText) {
        personListLayout.removeAll();
        List<Person> personen = personService.vollTextSuche(searchText);
        personen.forEach(this::addPersonComponentToLayout);

        VaadinSession.getCurrent().setAttribute("searchedPersonen", personen);
    }

    private void addPersonComponentToLayout(Person person){
        VerticalLayout card = new VerticalLayout();
        card.addClassName("person-card");
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.STRETCH);
        card.setWidth("100%");

        HorizontalLayout frame = new HorizontalLayout();

        VerticalLayout foto = new VerticalLayout();
        foto.setWidth("63px");
        Image profilePic = new Image("images/blank-profile-picture.png", "Profilbild");
        profilePic.addClassName("ellipse-profile-picture");
        foto.add(profilePic);

        VerticalLayout personDetails = new VerticalLayout();
        personDetails.setSpacing(false);
        personDetails.setAlignItems(Alignment.START);
        personDetails.addClassName("student-details");


        RouterLink vorUndNachname = new RouterLink("", PersonVerwaltenView.class, person.getId_Person());
        vorUndNachname.addClassName("name-label");
        vorUndNachname.add(person.getVorname() + " " + person.getNachname());

        card.add(frame);
        personListLayout.add(card);
    }

}
