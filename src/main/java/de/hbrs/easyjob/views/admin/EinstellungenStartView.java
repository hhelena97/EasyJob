package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.AdminController;
import de.hbrs.easyjob.controllers.PersonController;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.views.admin.dialog.*;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.DialogLayout;
import de.hbrs.easyjob.views.components.PasswortAendernDialog;
import de.hbrs.easyjob.views.components.PasswortNeuDialog;

import javax.annotation.security.RolesAllowed;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
@StyleSheet("AdminEinstellungenStart.css")
@RolesAllowed("ROLE_ADMIN")
public class EinstellungenStartView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    private PersonRepository personRepository;
    private final AdminController adminController;
    private final PersonController personController;

    private final ProfilDeaktivierenController profilDeaktivierenController;
    private Admin admin;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            event.rerouteTo(LoginView.class);
        }
    }

    public EinstellungenStartView(SessionController sessionController, PersonController personController,
                                  AdminController adminController, ProfilDeaktivierenController profilDeaktivierenController,
                                  PersonRepository repository) {
        this.sessionController = sessionController;
        this.adminController = adminController;
        this.personController = personController;
        this.profilDeaktivierenController = profilDeaktivierenController;
        this.personRepository = repository;
        this.admin = (Admin) sessionController. getPerson();

        //grüner Kasten oben
        VerticalLayout willkommenBox = new VerticalLayout();
        willkommenBox.addClassName("gruene-box");

        //Ausloggen
        Dialog dialogAusloggen = new Dialog();
        dialogAusloggen.add(new Paragraph("Wollen Sie sich wirklich ausloggen"));

        Button btnAbbruch = new Button ("Abbrechen");
        btnAbbruch.addClassName("buttonAbbruch");
        btnAbbruch.addClickListener(e -> dialogAusloggen.close());

        String bestaetigen = "Ausloggen";
        Button btnBestaetigen = new Button(bestaetigen);
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            sessionController.logout();
            UI.getCurrent().getPage().setLocation("/login");
        });
        dialogAusloggen.getFooter().add(btnAbbruch, btnBestaetigen);

        //der Knopf um den Ausloggen-Dialog zu öffnen
        Icon signout = new Icon(VaadinIcon.SIGN_OUT);
        signout.addClassName("signout");
        Button ausloggen = new Button(signout);
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");
        ausloggen.addClickListener(e -> dialogAusloggen.open());

        HorizontalLayout ausl = new HorizontalLayout(ausloggen);
        ausl.addClassName("ausl");

        //Begrüßungstext
        Div willkommenText = new Div();
        H3 titel = new H3("Hallo Admin");

        ausl.add(ausloggen);

        //Passwort ändern für den angemeldeten Admin
        PasswortAendernDialog passwortAendernDialog = new PasswortAendernDialog(admin, "AdminLayout.css", new PasswortService(personRepository));
        Icon editself = new Icon(VaadinIcon.EDIT);
        editself.addClassName("editAdmin");
        editself.addClickListener(e-> passwortAendernDialog.open());

        Paragraph eigenemail = new Paragraph(admin.getEmail());

        willkommenText.add(titel, eigenemail);
        willkommenText.addClassName("willkommen-text");


        //alles in den grünen Kasten
        HorizontalLayout begruessung = new HorizontalLayout(willkommenText, editself);
        begruessung.addClassName("begruessung");
        willkommenBox.add(ausl, begruessung);


        // der Bereich unter dem grünen Kasten: Liste aller Admins und + für neue Admins
        Div adminListe = new Div();
        adminListe.addClassName("adminListe");

        for (Admin a: personRepository.findAllAdmins()) {

            if(a.getAktiv() && !a.equals(admin) ) {
                HorizontalLayout einAdmin = new HorizontalLayout();

                //Zeige die E-Mail-Adresse:
                Paragraph mail = new Paragraph("" + a.getEmail());
                mail.addClassName("text");

                Div icons = new Div();

                //Passwort ändern für den entsprechenden Admin
                PasswortNeuDialog passwortNeuDialog = new PasswortNeuDialog(a, "AdminLayout.css", new PasswortService(personRepository));
                Icon edit = new Icon(VaadinIcon.EDIT);
                edit.addClassName("edit");
                edit.addClickListener(e-> passwortNeuDialog.open());

                //Admin entfernen
                Icon minus = new Icon(VaadinIcon.MINUS);
                minus.addClassName("minus");

                Dialog dialogAdminDeaktivieren = new Dialog();
                String adminMail = a.getEmail();
                dialogAdminDeaktivieren.add(new Paragraph("Wollen Sie den Admin " + adminMail + " wirklich entfernen?"));

                Button btnAbbruch2 = new Button("Abbrechen");
                btnAbbruch2.addClassName("buttonAbbruch");
                btnAbbruch2.addClickListener(e -> {
                    dialogAdminDeaktivieren.close();
                });

                Button btnBestaetigen2 = new Button("Admin löschen");
                btnBestaetigen2.addClassName("buttonBestaetigen");
                btnBestaetigen2.addClickListener(e -> {
                    profilDeaktivierenController.profilDeaktivierenPerson(a);
                    dialogAdminDeaktivieren.close();
                    UI.getCurrent().getPage().setLocation("/admin");
                });
                dialogAdminDeaktivieren.getFooter().add(btnAbbruch2, btnBestaetigen2);

                minus.addClickListener(e -> dialogAdminDeaktivieren.open());

                icons.add(minus, edit);
                icons.addClassName("icons");

                einAdmin.add(mail, icons);
                einAdmin.addClassName("admins");

                adminListe.add(einAdmin);
            }
        }

        HorizontalLayout addAdmin = new HorizontalLayout();
        addAdmin.addClassName("admins");

        Paragraph mail = new Paragraph("");
        mail.addClassName("text");

        //Admins hinzufügen
        Icon userPlus = new Icon(VaadinIcon.PLUS);
        userPlus.addClassName("userPlus");

        addAdmin.add(mail, userPlus);
        adminListe.add(addAdmin);

        Dialog dialogAdminHinzufuegen = new Dialog();
        dialogAdminHinzufuegen.setHeaderTitle("Neuen Admin anlegen");

        EmailField emailneu = new EmailField("E-Mail");
        PasswordField pwneu = new PasswordField("Passwort");
        PasswordField pwrp = new PasswordField("Passwort wiederholen");

        Paragraph p4 = new Paragraph("");
        Paragraph p5 = new Paragraph("");

        Div zugangsdaten = new Div(emailneu, p4, pwneu, p5, pwrp);

        dialogAdminHinzufuegen.add(zugangsdaten);

        Button btnAbbruch3 = new Button ("Abbrechen");
        btnAbbruch3.addClassName("buttonAbbruch");
        btnAbbruch3.addClickListener(e -> dialogAdminHinzufuegen.close());

        Button btnAdminAnlegen = new Button("Admin anlegen");
        btnAdminAnlegen.setClassName("buttonBestaetigen");
        btnAdminAnlegen.addClickListener(e -> {
            Admin adminNeu = new Admin();
            adminNeu.setAktiv(true);
            adminNeu.setGesperrt(false);
            adminNeu.setEmail(emailneu.getValue());
            PasswortService pws = new PasswortService(personRepository);
            boolean passwortPruefen = pws.newPassword(adminNeu, pwneu.getValue(), pwrp.getValue());
            if (passwortPruefen){
                adminNeu.setPasswort(pwneu.getValue());
                if(adminController.createAdmin(admin)){
                    dialogAdminHinzufuegen.close();
                } else {
                    Notification.show("Der Admin konnte nicht gespeichert werden.");
                }
            } else {
                Notification.show("Der Admin konnte nicht gespeichert werden.");
            }
            UI.getCurrent().getPage().setLocation("/admin");
        });

        dialogAdminHinzufuegen.getFooter().add(btnAbbruch3, btnAdminAnlegen);

        userPlus.addClickListener(e -> dialogAdminHinzufuegen.open());


        //Seite besteht aus WillkommenBox und AdminListe
        add(willkommenBox, adminListe);
    }

}
