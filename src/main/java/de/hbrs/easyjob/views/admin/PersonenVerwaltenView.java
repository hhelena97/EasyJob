package de.hbrs.easyjob.views.admin;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
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
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.*;

import javax.annotation.security.RolesAllowed;


@Route(value = "admin/personenVerwalten", layout = AdminLayout.class)
@PageTitle("Accounts Verwalten")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
@StyleSheet("AdminPersonenVerwaltenView.css")
@RolesAllowed("ROLE_ADMIN")

public class PersonenVerwaltenView extends VerticalLayout implements BeforeEnterObserver {
    // Repositories
    private final transient PersonRepository personRepository;

    // Services
    private final transient FaehigkeitService faehigkeitService;
    private final transient StudentService studentService;
    private final transient UnternehmenService unternehmenService;

    // Controllers
    private final transient ProfilSperrenController profilSperrenController;
    private final transient SessionController sessionController;

    // Components
    private VerticalLayout personLayout;
    TextField searchField;
    String email;


    public PersonenVerwaltenView(
            PersonRepository personRepository,
            FaehigkeitService faehigkeitService,
            StudentService studentService,
            UnternehmenService unternehmenService,
            ProfilSperrenController profilSperrenController,
            SessionController sessionController
    ) {
        this.personRepository = personRepository;
        this.faehigkeitService = faehigkeitService;
        this.studentService = studentService;
        this.unternehmenService = unternehmenService;
        this.profilSperrenController = profilSperrenController;
        this.sessionController = sessionController;

        initializeView();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            event.rerouteTo(LoginView.class);
        }
    }


    private void initializeView() {

        Div btnAusloggen = new AdminAusloggen(sessionController);
        HorizontalLayout ausloggen = new HorizontalLayout(btnAusloggen);
        ausloggen.addClassName("ausloggenFenster");


        // Titel der Seite
        H3 titel = new H3("Accounts verwalten");
        titel.addClassName("personentext");

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        searchField = new TextField();
        searchField.setPlaceholder("E-Mail Adresse");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addKeyPressListener(Key.ENTER, e -> {
            VaadinSession.getCurrent().setAttribute("email", searchField.getValue());
            loadPerson();
        });

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");

        // alles in einen grünen Block
        Div gruenerBlock = new Div(ausloggen, titel, searchLayout);
        gruenerBlock.addClassName("gruene-box");

        // Ergebnis
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

                // Wenn Admin zu Admin Verwalten verlinken
                if (person instanceof Admin) {

                    Label admininfo = new Label("Der gesuchte Account gehört zur Administration.");
                    Label adminlink = new Label("← Hier verwalten");
                    RouterLink linkAdmin = new RouterLink(EinstellungenStartView.class);
                    linkAdmin.add(adminlink);
                    VerticalLayout info = new VerticalLayout(admininfo, linkAdmin);
                    info.setAlignItems(Alignment.CENTER);
                    info.setAlignSelf(Alignment.CENTER);
                    personLayout.add(info);

                    // Wenn nicht, Person mit Buttons anzeigen
                } else {
                    VerticalLayout infos = new VerticalLayout();
                    infos.setAlignItems(Alignment.CENTER);
                    infos.addClassName("infos");
                    String foto = person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png";

                    // Profil Bild
                    VerticalLayout profilBild = new VerticalLayout(new Image(foto, "EasyJob"));
                    profilBild.addClassName("profilBild");

                    // Name
                    H2 name = new H2(person.getVorname() + " " + person.getNachname());
                    name.addClassName("nameM");

                    infos.add(profilBild, name);

                    // Buttons
                    Div buttons = new Div();
                    buttons.addClassName("buttons");

                    IconFactory[] iconFactory = new IconFactory[]{FontAwesome.Solid.USER_EDIT, FontAwesome.Solid.LOCK, FontAwesome.Solid.LOCK_OPEN};

                    // Passwort ändern
                    PasswortNeuDialog passwortNeuDialog = new PasswortNeuDialog(person, new PasswortService(personRepository));
                    Button btnneuesPasswort = new Button("Passwort ändern", iconFactory[0].create());
                    btnneuesPasswort.addClassName("btnNeuesPasswort");
                    btnneuesPasswort.addClickListener(e -> passwortNeuDialog.open());

                    // Person sperren
                    // Dialog zum Nachfragen
                    Dialog d = new Dialog();

                    Icon lock;
                    String sperrbutton;
                    if (person.getGesperrt()) {
                        sperrbutton = "Profil entsperren";
                        lock = iconFactory[2].create();
                        personEntperrenDialog(person, d);
                    } else {
                        sperrbutton = "Profil sperren";
                        lock = iconFactory[1].create();
                        personSperrenDialog(person, d);
                    }

                    Button btnSperren = new Button(sperrbutton, lock);
                    btnSperren.addClassName("btnSperren");
                    btnSperren.addClickListener(e -> d.open());

                    buttons.add(btnneuesPasswort, btnSperren);

                    personLayout.add(infos, buttons);

                    if (person instanceof Student student) {
                        personLayout.add(new AdminStudentProfileComponent(student, studentService, faehigkeitService));
                    } else if (person instanceof Unternehmensperson uperson) {
                        System.out.println("Unternehmensperson");
                        personLayout.add(new AdminUnternehmenspersonProfileComponent(
                                uperson, unternehmenService, profilSperrenController));
                    }
                }
            } else {
                Paragraph p = new Paragraph("Account nicht gefunden");
                p.addClassName("nicht-gefunden");
                personLayout.add(p);
            }
        }
    }

    private void personSperrenDialog(Person person, Dialog dialog) {

        dialog.add(new Paragraph("Möchten Sie " + person.getVorname() + " " + person.getNachname() + " sperren?"));

        Button btnAbbruch2 = new Button("Abbrechen");
        btnAbbruch2.addClassName("close-admin");
        btnAbbruch2.addClickListener(e -> {
            dialog.close();
        });

        Button btnBestaetigen = new Button("Account sperren");
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            if (profilSperrenController.personSperren(person)) {
                dialog.close();
            } else {
                Notification.show("Der Account konnte nicht gesperrt werden");
            }
        });
        dialog.getFooter().add(btnBestaetigen, btnAbbruch2);
    }

    private void personEntperrenDialog(Person person, Dialog dialog) {

        dialog.add(new Paragraph("Möchten Sie " + person.getVorname() + " " + person.getNachname() + " entsperren?"));

        Button btnAbbruch3 = new Button("abbrechen");
        btnAbbruch3.addClassName("close-admin");
        btnAbbruch3.addClickListener(e -> dialog.close());

        Button btnBestaetigen = new Button("Account entsperren");
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            profilSperrenController.personEntsperren(person);
            dialog.close();
        });
        dialog.getFooter().add(btnBestaetigen, btnAbbruch3);
    }
}
