package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.AdminController;
import de.hbrs.easyjob.controllers.PersonController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.views.admin.dialog.*;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.DialogLayout;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
//@RolesAllowed("ROLE_ADMIN")
public class EinstellungenStartView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    private PersonRepository personRepository;

    private final AdminController adminController;
    private final PersonController personController;
    private Admin admin;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            //event.rerouteTo(LoginView.class);
        }
        //todo: prüfe, ob der Admin auch aktiv ist. Sonst zurück zu login.
    }

    public EinstellungenStartView(SessionController sessionController, PersonController personController,
                                  AdminController adminController, PersonRepository repository) {
        this.sessionController = sessionController;
        this.adminController = adminController;
        this.personController = personController;
        this.personRepository = repository;
        this.admin = (Admin) sessionController.getPerson();

                //grüner Kasten oben
        HorizontalLayout willkommenBox = new HorizontalLayout();
        willkommenBox.addClassName("gruene-box");

        //Ausloggen
        //Inhalt des Dialogfensters
        Div dialogAuslogen = new Div();
        Paragraph wirklichAusloggen = new Paragraph("Wollen Sie sich wirklich ausloggen");
        dialogAuslogen.add(wirklichAusloggen);

        //DialogFenster
        DialogLayout ausloggenDialog = new DialogLayout(true);
        ausloggenDialog.insertContentDialogContent("", dialogAuslogen, "abbrechen", "ausloggen" );
        /*
        //TODO: Wo schreib ich, was bei confirm passiert?
            ausloggenDialog.addConfrimListener(e -> {
            sessionController.logout();
            UI.getCurrent().getPage().setLocation("/login");
            }
        );
         */

        //der Knopf um den Ausloggen-Dialog zu öffnen
        Button ausloggen = new Button(new Icon(VaadinIcon.SIGN_OUT));
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");
        ausloggen.addClickListener(e -> ausloggenDialog.openDialogOverlay());


        //Begrüßungstext
        Div willkommenText = new Div(
                ausloggen,
                new H3("Hallo,"),
                new Paragraph(admin.getEmail())
        );
        willkommenText.addClassName("willkommen-text");

        //Admins hinzufügen
        Icon userPlus = new Icon(VaadinIcon.PLUS);
        userPlus.addClassName("userPlus");

        ZugangAnlegenDialogView zugangAnlegen = new ZugangAnlegenDialogView(true, adminController);
        Button neuerAdmin = new Button(userPlus, e -> zugangAnlegen.openDialogOverlay());
        //Todo: Admin-anlegen-Dialog Funktionen

        //alles in den grünen Kasten
        willkommenBox.add(ausloggen, willkommenText, neuerAdmin);


        // der Bereich unter dem grünen Kasten
        Div adminListe = new Div();
        adminListe.addClassName("adminListe");


        Paragraph mailAdmin = new Paragraph(sessionController.getPerson().getEmail());
        mailAdmin.addClassName("text");

        //Dialog zum Passwort ändern
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Passwort ändern");
        PasswordField altesPasswort = new PasswordField("Altes Passwort");
        PasswordField neuesPasswort = new PasswordField("Neues Passwort");
        PasswordField passwortWiederholen = new PasswordField("Passwort wiederholen");

        Div passwoeterAendern = new Div(altesPasswort, neuesPasswort, passwortWiederholen);


        Button btnPasswortAendern = new Button("Passwort ändern");
        btnPasswortAendern.addClassName("confirm");
        btnPasswortAendern.addClickListener(e -> {
            personController.changePassword(altesPasswort.getValue(), neuesPasswort.getValue(), admin.getEmail());
            dialog.close();
        });

        dialog.getFooter().add(btnPasswortAendern);

        Button cancelButton = new Button("abbrechen", (e) -> dialog.close());
        cancelButton.addClassName("close-admin");
        dialog.getFooter().add(cancelButton);

        Icon edit = new Icon(VaadinIcon.EDIT);
        edit.addClassName("editAdmin");

        Button editAdmin = new Button(edit, e-> dialog.open());
        //Todo: Testen

        HorizontalLayout aktuellerAdmin = new HorizontalLayout(
                mailAdmin,
                editAdmin
        );
        aktuellerAdmin.addClassName("admins");
        adminListe.add(aktuellerAdmin);

        for (Admin a: personRepository.findAllAdmins()) {
            HorizontalLayout einAdmin = new HorizontalLayout();

            Paragraph mail = new Paragraph("" + a.getEmail());
            mail.addClassName("text");

            Div icons = new Div();

            Icon minus = new Icon(VaadinIcon.MINUS);
            minus.addClassName("minus");

            ZugangEntfernenDialogView adminLoeschen = new ZugangEntfernenDialogView(true);
            Button btnMinus = new Button(minus, e -> adminLoeschen.openDialogOverlay());
            //Todo: Admin löschen Dialog Funktionen

            PasswortAendernDialogView passwortAendern = new PasswortAendernDialogView(true);
            Button btnAdmin = new Button(edit, e-> passwortAendern.openDialogOverlay());
            //Todo: Passwort-Dialog Funktionen

            icons.add(btnMinus, btnAdmin);

            einAdmin.add(mail, icons);
            einAdmin.addClassName("admins");

            adminListe.add(einAdmin);
        }
        add(willkommenBox, adminListe);
    }

}
