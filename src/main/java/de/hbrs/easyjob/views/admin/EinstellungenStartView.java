package de.hbrs.easyjob.views.admin;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.AdminController;
import de.hbrs.easyjob.controllers.PersonController;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminAusloggen;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.PasswortAendernDialog;
import de.hbrs.easyjob.views.components.PasswortNeuDialog;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@StyleSheet("Variables.css")
@StyleSheet("AdminLayout.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("AdminEinstellungenStart.css")
@RolesAllowed("ROLE_ADMIN")
public class EinstellungenStartView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    private final PersonRepository personRepository;
    private final AdminController adminController;
    private final PersonController personController;
    private final ProfilDeaktivierenController profilDeaktivierenController;

    private final Admin admin;

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
        Div btnAusloggen = new AdminAusloggen(sessionController);
        HorizontalLayout ausloggen = new HorizontalLayout(btnAusloggen);
        ausloggen.addClassName("ausloggenFenster");


        //Begrüßungstext
        VerticalLayout willkommenText = new VerticalLayout();
        willkommenText.setSpacing(false);
        H3 titel = new H3("Hallo,");
        HorizontalLayout accountAndEdit = new HorizontalLayout();

        //Icons
        IconFactory[] iconFactory = new IconFactory[] {FontAwesome.Solid.USER_EDIT, FontAwesome.Solid.USER_MINUS, FontAwesome.Solid.USER_PLUS};


        //Passwort ändern für den angemeldeten Admin
        PasswortAendernDialog passwortAendernDialog = new PasswortAendernDialog(admin, "Admin", new PasswortService(personRepository));
        Icon editself = iconFactory[0].create();
        editself.addClassName("editAdmin");
        editself.addClickListener(e-> passwortAendernDialog.open());

        Label eigenemail = new Label(admin.getEmail());
        accountAndEdit.add(eigenemail, editself);
        willkommenText.add(ausloggen, titel, accountAndEdit);
        willkommenText.addClassName("willkommen-text");


        //alles in den grünen Kasten
        HorizontalLayout begruessung = new HorizontalLayout(willkommenText);
        begruessung.addClassName("begruessung");
        willkommenBox.add(ausloggen, begruessung);


        // der Bereich unter dem grünen Kasten: Liste aller Admins und + für neue Admins
        VerticalLayout adminListe = new VerticalLayout();
        adminListe.setAlignItems(Alignment.CENTER);
        adminListe.setSpacing(false);
        adminListe.addClassName("adminListe");

        List<Admin> allAdmins = personRepository.findAllAdmins();
        int lineCount = allAdmins.size() -2;

        for (Admin admin: allAdmins) {

            if(admin.getAktiv() && !admin.getGesperrt() && !admin.equals(this.admin) ) {
                HorizontalLayout einAdmin = new HorizontalLayout();

                //Zeige die E-Mail-Adresse:
                Paragraph mail = new Paragraph(admin.getEmail());
                mail.addClassName("text");

                Div icons = new Div();

                //Passwort ändern für den entsprechenden Admin
                PasswortNeuDialog passwortNeuDialog = new PasswortNeuDialog(admin, new PasswortService(personRepository));
                Icon edit = iconFactory[0].create();
                edit.addClassName("edit");
                edit.addClickListener(e-> passwortNeuDialog.open());

                //Admin entfernen
                Icon minus = iconFactory[1].create();
                minus.addClassName("minus");

                Dialog dialogAdminDeaktivieren = new Dialog();
                dialogAdminDeaktivieren.add(new Paragraph("Möchten Sie den Zugang für " + admin.getEmail() + " wirklich entfernen?"));

                Button btnAbbruch2 = new Button("Abbrechen");
                btnAbbruch2.addClassName("close-admin");
                btnAbbruch2.addClickListener(e -> dialogAdminDeaktivieren.close());

                Button btnBestaetigen2 = new Button("Zugangsrechte entfernen");
                btnBestaetigen2.addClassName("confirm");
                btnBestaetigen2.addClickListener(e -> {
                    profilDeaktivierenController.profilDeaktivierenPerson(admin);
                    dialogAdminDeaktivieren.close();
                    UI.getCurrent().getPage().setLocation("/admin");
                });
                dialogAdminDeaktivieren.getFooter().add(btnBestaetigen2, btnAbbruch2);

                minus.addClickListener(e -> dialogAdminDeaktivieren.open());

                icons.add(minus, edit);
                icons.addClassName("icons");

                einAdmin.add(mail, icons);
                einAdmin.addClassName("admins");

                adminListe.add(einAdmin);

                if (lineCount > 0) {
                    Div line = new Div();
                    line.addClassName("line");
                    adminListe.add(line);
                    lineCount--;
                }

            }
        }

        HorizontalLayout addAdmin = new HorizontalLayout();
        addAdmin.addClassName("admins");

        Paragraph mail = new Paragraph("");
        mail.addClassName("text");

        //Admins hinzufügen
        Icon userPlus = iconFactory[2].create();
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
