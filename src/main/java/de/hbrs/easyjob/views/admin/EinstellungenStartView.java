package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.AdminController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.views.admin.dialog.*;
import de.hbrs.easyjob.views.components.AdminLayout;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
//@RolesAllowed("ROLE_ADMIN")
public class EinstellungenStartView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    private PersonRepository personRepository;

    private final AdminController adminController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            //event.rerouteTo(LoginView.class);
        }
        //todo: prüfe, ob der Admin auch aktiv ist. Sonst zurück zu login.
    }

    public EinstellungenStartView(SessionController sessionController, AdminController adminController, PersonRepository repository) {
        this.sessionController = sessionController;
        this.adminController = adminController;
        this.personRepository = repository;

        //grüner Kasten oben
        HorizontalLayout willkommenBox = new HorizontalLayout();
        willkommenBox.addClassName("gruene-box");

        //Ausloggen
        AusloggenDialogView ausloggenDialog = new AusloggenDialogView(true);
        Button ausloggen = new Button(new Icon(VaadinIcon.SIGN_OUT));
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");
        ausloggen.addClickListener(e -> ausloggenDialog.openDialogOverlay());
        //Todo: ausloggen-Dialog Funktionen

        //Begrüßungstext
        Div willkommenText = new Div(
                ausloggen,
                new H3("Hallo,"),
                new Paragraph(sessionController.getPerson().getEmail())
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

        Icon edit = new Icon(VaadinIcon.EDIT);
        edit.addClassName("editAdmin");

        AdminPasswortAendernDialogView eigenespasswortAendern = new AdminPasswortAendernDialogView(true);
        Button editAdmin = new Button(edit, e-> eigenespasswortAendern.openDialogOverlay());
        //Todo: Passwort-Dialog Funktionen

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
