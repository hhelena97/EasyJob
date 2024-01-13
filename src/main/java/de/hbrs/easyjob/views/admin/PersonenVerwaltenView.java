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
import com.vaadin.flow.component.textfield.PasswordField;
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
@RolesAllowed("ROLE_ADMIN")

public class PersonenVerwaltenView extends VerticalLayout implements BeforeEnterObserver {
    //private final PersonSuchenService personService;
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;

    private final JobRepository jobRepository;

    private final SessionController sessionController;

    private final ProfilSperrenController profilSperrenController;

    private final UnternehmenspersonRepository unternehmenspersonRepository;

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

        H3 titel = new H3 ("Personen verwalten");
        titel.addClassName("willkommen-text");

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
        Div gruenerBlock = new Div(titel, searchLayout);
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

                //Passwort ändern
                Dialog dialogPasswortAendern = new Dialog();
                dialogPasswortAendern.setHeaderTitle("Passwort ändern");

                PasswordField pwneu1 = new PasswordField("neues Passwort");
                PasswordField pwrp1 = new PasswordField("Passwort wiederholen");

                Paragraph p10 = new Paragraph("");

                Div inhalt = new Div(pwneu1, p10, pwrp1);

                dialogPasswortAendern.add(inhalt);

                Button btnAbbruch4 = new Button ("abbrechen");
                btnAbbruch4.addClassName("buttonAbbruch");
                btnAbbruch4.addClickListener(e -> dialogPasswortAendern.close());

                Button btnPasswortAendern = new Button("Passwort ändern");
                btnPasswortAendern.setClassName("buttonBestaetigen");

                btnPasswortAendern.addClickListener(e -> {
                    if (new PasswortService(personRepository).newPassword(person, pwneu1.getValue(), pwrp1.getValue())) {
                        Notification.show("Passwort geändert");
                        dialogPasswortAendern.close();
                    } else {
                        Notification.show("Es gibt ein Problem.");
                    }
                });
                dialogPasswortAendern.getFooter().add(btnAbbruch4, btnPasswortAendern);

                Button btnneuesPasswort = new Button("Passwort ändern");
                btnneuesPasswort.addClickListener(e -> dialogPasswortAendern.open());


                //Person sperren

                //Dialog zum Nachfragen
                Dialog d = new Dialog();

                if(person.getGesperrt()){
                    sperrbutton = "Profil entsperren";
                    personSperrenDialog(person, d);
                } else {
                    sperrbutton = "Profil entsperren";
                    personEntperrenDialog(person, d);
                }

                Button btnSperren = new Button(sperrbutton);
                btnSperren.addClassName("btnSperren");
                btnSperren.addClickListener(e -> d.open());

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
                RouterLink linkAdmin = new RouterLink(EinstellungenStartView.class);
                linkAdmin.add(adminlink);
                personLayout.add(admininfo, linkAdmin);

            } else {
                Paragraph p = new Paragraph("Die Person wurde nicht gefunden");
                p.addClassName("nicht-gefunden");

                personLayout.add(p);
            }

        }
    }

    private void personSperrenDialog(Person person, Dialog dialog){

        dialog.add(new Paragraph("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " sperren?"));

        Button btnAbbruch2 = new Button("abbrechen");
        btnAbbruch2.addClassName("buttonAbbruch");
        btnAbbruch2.addClickListener(e -> {
            dialog.close();
        });

        Button btnBestaetigen = new Button("Person sperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            profilSperrenController.personSperren(person);
            dialog.close();
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
