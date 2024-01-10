package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonSuchenService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.StudentProfileComponent;
import de.hbrs.easyjob.views.components.UnternehmenspersonProfileComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;


@Route(value = "person-verwalten", layout = AdminLayout.class)
@PageTitle("Person verwalten")
@StyleSheet("Variables.css")
@StyleSheet("AdminProfilVerwalten.css")
//@RolesAllowed("ROLE_ADMIN")
public class PersonVerwaltenView extends VerticalLayout implements HasUrlParameter<Integer>, BeforeEnterObserver {

    @Autowired
    private final PersonSuchenService personService;

    @Autowired
    private final StudentService studentService;

    @Autowired
    private final UnternehmenService unternehmenservice;

    Person person;

    private String sperrbutton = "Profil sperren";


    public PersonVerwaltenView(PersonSuchenService personSuchenService,
                               StudentService studentService,
                               UnternehmenService unternehmenservice){
        this.personService = personSuchenService;
        this.studentService = studentService;
        this.unternehmenservice = unternehmenservice;

        Icon zurueck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zurueck.addClassName("zurueckAdmin");

        H2 mail = new H2(person.getEmail());
        mail.addClassName("email");

        PasswortAendernDialogView passwortAendernDialog = new PasswortAendernDialogView(true);

        Button btnneuesPasswort = new Button("Passwort ändern", e -> passwortAendernDialog.openDialogOverlay());
        btnneuesPasswort.addClassName("btnNeuesPasswort");

        if (!person.getAktiv()){
            sperrbutton = "Profil entsperren";
        }

        Button btnSperren = new Button(sperrbutton);
        btnSperren.addClassName("btnSperren");

        add(zurueck, mail, btnneuesPasswort, btnSperren);
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

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer personID){
        Icon zurueck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zurueck.addClassName("zurueckAdmin");
        if ( personID != null) {
            this.person = personService.getPersonById(personID);
            if(person==null){
                throw new RuntimeException("Etwas ist schief gelaufen!");
            }
            add(zurueck, personDetail(person));
        } else {
            add(zurueck, new H3("Person konnte nicht geladen werden."));
        }
    }

    private Component personDetail(Person person){
        VerticalLayout fuerAlle = new VerticalLayout();

        Paragraph mail = new Paragraph(person.getEmail());
        mail.addClassName("email");

        PasswortAendernDialogView passwortAendernDialog = new PasswortAendernDialogView(true);

        Button btnneuesPasswort = new Button("Passwort ändern", e -> passwortAendernDialog.openDialogOverlay());
        btnneuesPasswort.addClassName("btnNeuesPasswort");

        if (!person.getAktiv()){
            sperrbutton = "Profil entsperren";
        }

        Button btnSperren = new Button(sperrbutton);
        btnSperren.addClassName("btnSperren");



        VerticalLayout infos = new VerticalLayout();

        if(person instanceof Student){
            Student student = (Student)person;
            infos.add(new StudentProfileComponent(student, "AdminProfilVerwalten.css",studentService));
        } else if (person instanceof Unternehmensperson){
            Unternehmensperson uperson = (Unternehmensperson) person;
            infos.add( new UnternehmenspersonProfileComponent(uperson, "AdminProfilVerwalten.css", unternehmenservice));
        }

        fuerAlle.add(mail, btnneuesPasswort, btnSperren, infos);

        return fuerAlle;
    }
}
