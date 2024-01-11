package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;

import javax.annotation.security.RolesAllowed;
import javax.sound.sampled.Line;

@Route(value = "admin", layout = AdminLayout.class)
@PageTitle("Admin")
@StyleSheet("Variables.css")
@StyleSheet("AdminEinstellungenStart.css")
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
    }

    public EinstellungenStartView(SessionController sessionController, AdminController adminController, PersonRepository repository) {
        this.sessionController = sessionController;
        this.adminController = adminController;
        this.personRepository = repository;

        //grüner Kasten oben
        HorizontalLayout willkommenBox = new HorizontalLayout();
        willkommenBox.addClassName("willkommen-box");

        //Ausloggen
        Button ausloggen = new Button(new Icon(VaadinIcon.SIGN_OUT));
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");
        ausloggen.addClickListener(e -> {
            if (sessionController.logout()) {
                UI.getCurrent().navigate(LoginView.class);
            }
        });

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

        //alles in den grünen Kasten
        willkommenBox.add(ausloggen, willkommenText, neuerAdmin);



        Div adminListe = new Div();
        adminListe.addClassName("adminListe");


        H3 mailAdmin = new H3(sessionController.getPerson().getEmail());
        mailAdmin.addClassName("text");

        HorizontalLayout aktuellerAdmin = new HorizontalLayout(
                mailAdmin,
                new Div(new Icon(VaadinIcon.EDIT))
        );
        aktuellerAdmin.addClassName("admins");
        adminListe.add(aktuellerAdmin);

        for (Admin a: personRepository.findAllAdmins()) {
            HorizontalLayout einAdmin = new HorizontalLayout(
                    new H2("" + a.getEmail()),
                    new Div(new Icon(VaadinIcon.MINUS),
                            new Icon(VaadinIcon.EDIT))
            );
            einAdmin.addClassName("admins");

            adminListe.add(einAdmin);
        }
        add(willkommenBox, adminListe);

        /*
        Div buttonAuswahl = new Div();
        buttonAuswahl.addClassName("buttonAuswahl");

        Details administration = new Details("Administration");
        administration.addClassName("buttons");
        administration.addThemeVariants(DetailsVariant.REVERSE);

        Details agb = new Details("AGB und Datenschutzerklärung");
        agb.addClassName("buttons");
        agb.addThemeVariants(DetailsVariant.REVERSE);

        Details impressum = new Details("Impressum");
        impressum.addClassName("buttons");
        impressum.addThemeVariants(DetailsVariant.REVERSE);



        buttonAuswahl.add(administration, agb, impressum, ausloggen);

        add(willkommen, buttonAuswahl);

         */
    }

}
