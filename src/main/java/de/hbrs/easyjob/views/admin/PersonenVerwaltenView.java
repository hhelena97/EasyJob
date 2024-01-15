package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.repositories.UnternehmenspersonRepository;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;


@Route(value = "admin/personenVerwalten", layout = AdminLayout.class)
@PageTitle("Personen Verwalten")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
@StyleSheet("AdminPersonenVerwaltenView.css")
@RolesAllowed("ROLE_ADMIN")

public class PersonenVerwaltenView extends VerticalLayout implements BeforeEnterObserver {

    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final JobRepository jobRepository;
    private final UnternehmenspersonRepository unternehmenspersonRepository;

    private final SessionController sessionController;

    private final ProfilSperrenController profilSperrenController;

    private VerticalLayout personLayout;

    TextField searchField;
    String email;

    private String sperrbutton = "Profil sperren";

    private final StudentService studentService;

    private final UnternehmenService unternehmenService;



    public PersonenVerwaltenView(SessionController sessionController, JobRepository jobRepository,
                                 UnternehmenRepository unternehmenRepository, PersonRepository personRepository,
                                 StudentService studentService, UnternehmenService unternehmenService,
                                 UnternehmenspersonRepository unternehmenspersonRepository) {
        this.sessionController = sessionController;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.jobRepository = jobRepository;
        this.profilSperrenController = new ProfilSperrenController(personRepository, unternehmenRepository, jobRepository, unternehmenspersonRepository);
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
                event.rerouteTo(LoginView.class);
            }
        } else {
            event.rerouteTo(LoginView.class);
        }
    }

    private boolean hasRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }



    private void initializeView(){

        Div btnAusloggen = new AdminAusloggen(sessionController, "AdminLayout.css");
        HorizontalLayout ausloggen = new HorizontalLayout(btnAusloggen);
        ausloggen.addClassName("ausloggenFenster");


        // Titel der Seite
        H3 titel = new H3 ("Personen verwalten");
        titel.addClassName("personentext");

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        searchField = new TextField();
        searchField.setPlaceholder("E-Mail der gesuchten Person");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> {
            VaadinSession.getCurrent().setAttribute("email", searchField.getValue());
            loadPerson();
        });

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");

        //alles in einen grünen Block
        Div gruenerBlock = new Div(ausloggen,titel, searchLayout);
        gruenerBlock.addClassName("gruene-box");

        //Ergebnis
        personLayout = new VerticalLayout();
        personLayout.setClassName("ergebnis-layout");

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

                //Wenn Admin zu Admin Verwalten verlinken
                if (person instanceof Admin) {
                    Paragraph admininfo = new Paragraph("Das ist ein Admin.");
                    Dialog d = new Dialog();

                    if (person.getGesperrt()) {
                        sperrbutton = "Profil entsperren";
                        personEntperrenDialog(person, d);
                    } else {
                        sperrbutton = "Profil sperren";
                        personSperrenDialog(person, d);
                    }

                    Button btnSperren = new Button(sperrbutton);
                    btnSperren.addClassName("btnSperren");
                    btnSperren.addClickListener(e -> d.open());

                    personLayout.add(admininfo, btnSperren);

                // Wenn nicht, Person mit Buttons anzeigen
                } else {
                    Div infos = new Div();
                    infos.addClassName("infos");
                    String foto = person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png";
                    //Profil Bild
                    VerticalLayout profilBild = new VerticalLayout(new Image(foto, "EasyJob"));
                    profilBild.addClassName("profilBild");

                    //Name
                    H2 name = new H2(person.getVorname() + " " + person.getNachname());
                    name.addClassName("nameM");

                    infos.add(profilBild, name);

                    //Buttons
                    Div buttons = new Div();
                    buttons.addClassName("buttons");

                    //Passwort ändern
                    PasswortNeuDialog passwortNeuDialog = new PasswortNeuDialog(person, "AdminLayout.css", new PasswortService(personRepository));
                    Button btnneuesPasswort = new Button("Passwort ändern");
                    btnneuesPasswort.addClassName("btnNeuesPasswort");
                    btnneuesPasswort.addClickListener(e-> passwortNeuDialog.open());


                    //Person sperren

                    //Dialog zum Nachfragen
                    Dialog d = new Dialog();

                    if (person.getGesperrt()) {
                        sperrbutton = "Profil entsperren";
                        personEntperrenDialog(person, d);
                    } else {
                        sperrbutton = "Profil sperren";
                        personSperrenDialog(person, d);
                    }

                    Button btnSperren = new Button(sperrbutton);
                    btnSperren.addClassName("btnSperren");
                    btnSperren.addClickListener(e -> d.open());

                    buttons.add(btnneuesPasswort, btnSperren);

                    personLayout.add(infos, buttons);

                    if (person instanceof Student) {
                        Student student = (Student) person;
                        personLayout.add(new AdminStudentProfileComponent(student, "AdminPersonenVerwaltenView.css", studentService));
                    } else if (person instanceof Unternehmensperson) {
                        System.out.println("Unternehmensperson");
                        Unternehmensperson uperson = (Unternehmensperson) person;
                        personLayout.add(new AdminUnternehmenspersonProfileComponent(
                                personRepository, unternehmenRepository, jobRepository, uperson,
                                "AdminPersonenVerwaltenView.css", unternehmenService, profilSperrenController));
                    } else {
                        Paragraph p = new Paragraph("Die Person wurde nicht gefunden");
                        p.addClassName("nicht-gefunden");
                        personLayout.add(p);
                    }
                }
            }

        }
    }

    private void personSperrenDialog(Person person, Dialog dialog){

        dialog.add(new Paragraph("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " sperren?"));

        Button btnAbbruch2 = new Button("Abbrechen");
        btnAbbruch2.addClassName("buttonAbbruch");
        btnAbbruch2.addClickListener(e -> {
            dialog.close();
        });

        Button btnBestaetigen = new Button("Person sperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            if (profilSperrenController.personSperren(person)){
                dialog.close();
            } else {
                Notification.show("Die Person konnte nicht gesperrt werden");
            }
        });
        dialog.getFooter().add(btnAbbruch2, btnBestaetigen);
    }

    private void personEntperrenDialog(Person person, Dialog dialog){

        dialog.add(new Paragraph("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " entsperren?"));

        Button btnAbbruch3 = new Button("abbrechen");
        btnAbbruch3.addClassName("buttonAbbruch");
        btnAbbruch3.addClickListener(e -> {
            dialog.close();
        });

        Button btnBestaetigen = new Button("Person entsperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            profilSperrenController.personEntsperren(person);
            dialog.close();
        });
        dialog.getFooter().add(btnAbbruch3, btnBestaetigen);
    }
}
