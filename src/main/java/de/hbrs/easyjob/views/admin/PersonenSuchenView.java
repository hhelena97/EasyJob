package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;


@Route(value = "admin/personenSuchen", layout = AdminLayout.class)
@PageTitle("Personen Suchen")
@StyleSheet("Variables.css")
@StyleSheet("AdminPersonenSuchen.css")
//@RolesAllowed("ROLE_ADMIN")

public class PersonenSuchenView extends VerticalLayout implements BeforeEnterObserver {
    //private final PersonSuchenService personService;
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;

    private final SessionController sessionController;

    private VerticalLayout personLayout;

    TextField searchField;
    String email;

    private final ProfilDeaktivierenController personSperrencController;

    private String sperrbutton = "Profil sperren";

    private final StudentService studentService;

    private final UnternehmenService unternehmenService;



    public PersonenSuchenView(SessionController sessionController,
                              UnternehmenRepository unternehmenRepository, PersonRepository personRepository,
                              ProfilDeaktivierenController profilDeaktivierenController,
                              StudentService studentService, UnternehmenService unternehmenService) {
        this.sessionController = sessionController;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.personSperrencController = profilDeaktivierenController;
        this.studentService = studentService;
        this.unternehmenService = unternehmenService;
        initializeView();
    }

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



    private void initializeView(){
        addClassName("person-suchen-view");

        H3 titel = new H3 ("Personen suchen");
        titel.addClassName("titel");

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        searchField = new TextField();
        searchField.setPlaceholder("E-Mail der gesuchten Person");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> {
            VaadinSession.getCurrent().setAttribute("email", searchField.getValue());
            loadPerson();
        });
        //Todo: die Person wird erst beim Erneuern der Seite aufgerufen. Warum ist das so?

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");

        //alles in einen grünen Block
        Div gruenerBlock = new Div(titel, searchLayout);
        gruenerBlock.addClassName("gruenerblock");

        //Ergebnis
        personLayout = new VerticalLayout();
        //personLayout.setWidth("90%");
        //personLayout.setAlignItems(Alignment.STRETCH);
        personLayout.setClassName("ergebnis-layout");

        // Person laden und anzeigen
        //loadPerson();

        // wieder aufräumen
        VaadinSession.getCurrent().setAttribute("email", null);

        add(gruenerBlock, personLayout);
    }


    private void loadPerson() {
        personLayout.removeAll();

        email = (String) VaadinSession.getCurrent().getAttribute("email");
        if (email != null) {
            Person person = personRepository.findByEmail(email);

            if (person != null) {
                Div infos = new Div();

                //Profil Bild
                VerticalLayout profilBild = new VerticalLayout();
                profilBild.setWidth("200px");
                profilBild.addClassName("profilBild");
                profilBild.add(new Image(person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png", "EasyJob"));

                //Name
                H2 name = new H2();
                name.addClassName("name");
                name.add(person.getVorname() + " " + person.getNachname());

                infos.add(profilBild, name);

                //Buttons
                Div buttons = new Div();
                PasswortAendernDialogView passwortAendernDialog = new PasswortAendernDialogView(true);

                Button btnneuesPasswort = new Button("Passwort ändern", e -> passwortAendernDialog.openDialogOverlay());
                btnneuesPasswort.addClassName("btnNeuesPasswort");


                //Sperr-Button mit Nachfrage
                if (!person.getAktiv()) {
                    sperrbutton = "Profil entsperren";
                }
                //Dialog zum Nachfragen
                Div nachfragen = new Div();
                Paragraph p;
                Button btnDialogSperren;

                if (!person.getAktiv()){
                    p = new Paragraph ("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " sperren?");
                    btnDialogSperren = new Button("sperren", e-> personSperrencController.profilDeaktivierenPerson(person));
                } else {
                    p = new Paragraph ("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " reaktivieren?");
                    btnDialogSperren = new Button("entsperren", e-> personSperrencController.profilReaktivierenPerson(person));
                }
                nachfragen.add(p, btnDialogSperren);
                DialogLayout d = new DialogLayout(true);

                Button btnSperren = new Button(sperrbutton, e-> d.insertContentDialogContent("", nachfragen, "abbrechen", "???"));
                btnSperren.addClassName("btnSperren");

                buttons.add(btnneuesPasswort, btnSperren);

                personLayout.add(infos, buttons);

                if (person instanceof Student) {
                    Student student = (Student) person;
                    personLayout.add(new AdminStudentProfileComponent(student, "AdminProfilVerwalten.css", studentService));
                } else if (person instanceof Unternehmensperson) {
                    Unternehmensperson uperson = (Unternehmensperson) person;
                    personLayout.add(new AdminUnternehmenspersonProfileComponent(uperson, "AdminProfilVerwalten.css", unternehmenService));
                }
            } else if (person instanceof Admin) {
                Paragraph admininfo = new Paragraph("Das ist ein Admin.");
                Paragraph adminlink = new Paragraph("Zur Administration");
                RouterLink linkAdmin = new RouterLink(AdministrationView.class);
                linkAdmin.add(adminlink);
                personLayout.add(admininfo, linkAdmin);

            } else {
                Paragraph p = new Paragraph("Die Person wurde nicht gefunden");
                p.addClassName("nicht-gefunden");

                personLayout.add(p);
            }

        }
    }
}
