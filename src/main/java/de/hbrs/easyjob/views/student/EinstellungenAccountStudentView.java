package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.DeaktivierenConfirmDialog;
import de.hbrs.easyjob.views.components.PasswortAendernDialog;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;

import javax.annotation.security.RolesAllowed;

@Route("student/einstellungen/account")
@PageTitle("Accounteinstellungen")
@StyleSheet("Registrieren.css")
@StyleSheet("Einstellungen.css")
@StyleSheet("DialogLayout.css")
@RolesAllowed("ROLE_STUDENT")
public class EinstellungenAccountStudentView extends VerticalLayout implements BeforeLeaveObserver, BeforeEnterObserver {

    private final transient SessionController sessionController;
    private final transient ProfilDeaktivierenController profilDeaktivieren;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_STUDENT")) {
            event.rerouteTo(LoginView.class);
        }
    }

    public EinstellungenAccountStudentView(ProfilDeaktivierenController profilDeaktivierenController, SessionController sessionController, PasswortService passwortService) {
        this.sessionController = sessionController;
        Student student = (Student) sessionController.getPerson();
        this.profilDeaktivieren = profilDeaktivierenController;

        VerticalLayout frame = new VerticalLayout();

        //Zurück
        Button back = new ZurueckButtonRundLayout("Student");
        RouterLink linkzuruck = new RouterLink(EinstellungenUebersichtStudentView.class);
        linkzuruck.add(back);

        //Überschrift
        Label ueber = new Label("Accounteinstellungen");
        ueber.addClassName("accounteinstellungen");

        //Passwort ändern
        PasswortAendernDialog passwort = new PasswortAendernDialog(student, "Student", passwortService);
        Button passwortaendern = new Button("Passwort ändern", e -> passwort.open());
        passwortaendern.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        passwortaendern.addClassName("menu-button");

        //Account deaktivieren
        DeaktivierenConfirmDialog deaktivierenDialog = new DeaktivierenConfirmDialog("Student", "Dein Profil wird unsichtbar und du kannst keine Nachrichten mehr erhalten. " + "Du kannst deinen Account jederzeit reaktivieren.");
        Button deaktivieren = new Button("Account deaktivieren", e -> deaktivierenDialog.openDialogOverlay());
        deaktivieren.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deaktivieren.addClassName("deaktivieren");

        VerticalLayout buttons = new VerticalLayout(passwortaendern, deaktivieren);
        buttons.setSpacing(false);

        frame.add(linkzuruck, ueber, buttons);
        add(frame);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // Deaktiviere Studenten-Account
        Person person = sessionController.getPerson();
        if (profilDeaktivieren.profilDeaktivierenPerson(person)) {
            System.out.printf("Profil '%s' deaktiviert.\n", person.getEmail());
        }
    }

    //TODO: aus irgendeinem Grund landet man nicht auf http://localhost:8080/login beim Ausloggen (siehe Seleniumtest)
}
