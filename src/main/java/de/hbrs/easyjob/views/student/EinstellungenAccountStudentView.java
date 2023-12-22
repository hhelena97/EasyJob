package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.DeaktivierenConfirmDialog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;

@Route("student/einstellungen/account")
@PageTitle("Accounteinstellungen")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@RolesAllowed("ROLE_STUDENT")
public class EinstellungenAccountStudentView extends VerticalLayout implements BeforeLeaveObserver, BeforeEnterObserver {

    private final ProfilDeaktivierenController profilDeaktivieren;

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
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
    }

    public EinstellungenAccountStudentView(PersonRepository personRepository, UnternehmenRepository unternehmenRepository) {
        this.profilDeaktivieren = new ProfilDeaktivierenController(personRepository, unternehmenRepository);

        VerticalLayout frame = new VerticalLayout();
        DeaktivierenConfirmDialog deaktivierenDialog = new DeaktivierenConfirmDialog(true, "Student", "Dein Profil wird unsichtbar und du kannst keine ChatsView mehr erhalten. Du kannst deinen Account jederzeit reaktivieren.") ;

        frame.setClassName("Container");
        Button back = new Button("", new Icon(VaadinIcon.ARROW_LEFT));

        Label ueber = new Label("Accounteinstellungen");

        System.out.println("EinstellungenAccountStudentView");
        System.out.println("Session: " + VaadinSession.getCurrent());

        Button deaktivieren = new Button("Account deaktivieren", e -> deaktivierenDialog.openDialogOverlay());


        frame.add(back, ueber, deaktivieren);
        add(frame);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // Deaktiviere Studenten-Account
        Person person = (Person) UI.getCurrent().getSession().getAttribute("current_User");
        if (profilDeaktivieren.profilDeaktivierenPerson(person)) {
            System.out.printf("Profil '%s' deaktiviert.\n", person.getEmail());
        }
    }
}
