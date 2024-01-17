package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.DeaktivierenConfirmDialog;
import de.hbrs.easyjob.views.components.PasswortAendernDialog;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;

import javax.annotation.security.RolesAllowed;

@Route("unternehmen/einstellungen/account")
@PageTitle("Accounteinstellungen")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class EinstellungenAccountUnternehmenView extends VerticalLayout implements BeforeLeaveObserver, BeforeEnterObserver {

    private final transient SessionController sessionController;
    private final transient ProfilDeaktivierenController profilDeaktivierenController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
            event.rerouteTo(LoginView.class);
        }
    }

    public EinstellungenAccountUnternehmenView(
            ProfilDeaktivierenController profilDeaktivierenController,
            SessionController sessionController,
            PasswortService passwortService
    ) {
        this.profilDeaktivierenController = profilDeaktivierenController;
        this.sessionController = sessionController;
        Unternehmensperson person = (Unternehmensperson) sessionController.getPerson();

        VerticalLayout frame = new VerticalLayout();

        Button back = new ZurueckButtonRundLayout("Unternehmen");
        RouterLink linkzuruck = new RouterLink(EinstellungenUebersichtUnternehmenView.class);
        linkzuruck.add(back);

        Label ueber = new Label("Accounteinstellungen");
        ueber.addClassName("accounteinstellungen");

        // Passwort ändern
        PasswortAendernDialog passwort = new PasswortAendernDialog(person, "Unternehmen", passwortService);
        Button passwortaendern = new Button("Passwort ändern", e -> passwort.open());
        passwortaendern.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        passwortaendern.addClassName("menu-button");

        // Deaktivieren
        DeaktivierenConfirmDialog deaktivierenDialog = new DeaktivierenConfirmDialog("Unternehmen",
                "Ihr Profil wird unsichtbar und Sie können keine ChatsView mehr erhalten. " +
                        "Das Unternehmensprofil bleibt sichtbar, solange mindestens ein verbundenes Profil aktiv ist." +
                        " Sie können Ihren Account jederzeit reaktivieren.");

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
        // Deaktiviere Unternehmen-Account
        Person person = sessionController.getPerson();
        if (profilDeaktivierenController.profilDeaktivierenPerson(person)) {
            System.out.printf("Profil '%s' deaktiviert.\n", person.getEmail());
        }
    }
}
