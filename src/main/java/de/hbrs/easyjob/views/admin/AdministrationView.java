package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.entities.Admin;

@Route(value = "administration", layout = AdminLayout.class)
@PageTitle("Administration")
@StyleSheet("Variables.css")
@StyleSheet("AdministrationView.css")
//@RolesAllowed("ROLE_ADMIN")
public class AdministrationView extends VerticalLayout implements BeforeEnterObserver {

    private PersonRepository personRepository;
    private final SessionController sessionController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            //event.rerouteTo(LoginView.class);
        }
    }

    public AdministrationView(SessionController sessionController, PersonRepository personRepository) {
        this.sessionController = sessionController;
        this.personRepository = personRepository;

        Icon back = new Icon(VaadinIcon.CHEVRON_LEFT);
        back.addClassName("backArrow");

        Icon userPlus = new Icon(VaadinIcon.PLUS);
        userPlus.addClassName("userPlus");

        HorizontalLayout willkommenText = new HorizontalLayout(back, userPlus);
        willkommenText.addClassName("willkommen-text");

        Div willkommen = new Div(willkommenText);
        willkommen.addClassName("willkommen-box");

        Div adminListe = new Div();
        adminListe.addClassName("adminListe");

        //TODO: es gibt noch keine Emails bei Admins
        H3 mailAdmin = new H3("E-Mail Adresse" /*+ sessionController.getPerson().getEmail()*/);
        mailAdmin.addClassName("text");

        HorizontalLayout aktuellerAdmin = new HorizontalLayout(
                 mailAdmin,
                new Div(new Icon(VaadinIcon.EDIT))
        );
        aktuellerAdmin.addClassName("admins");
        adminListe.add(aktuellerAdmin);

        for (Admin a: personRepository.findAllAdmins()) {
            HorizontalLayout einAdmin = new HorizontalLayout(
                    new H2("" /*+ a.getMail()*/), //TODO: es gibt noch keine Emails bei Admins
                    new Div(new Icon(VaadinIcon.MINUS),
                    new Icon(VaadinIcon.EDIT))
            );
            einAdmin.addClassName("admins");

            adminListe.add(einAdmin);
        }
        add(willkommen, adminListe);
    }

}
