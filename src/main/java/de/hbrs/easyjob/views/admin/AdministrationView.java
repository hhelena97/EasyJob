package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
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

        //TODO: erster Admin, aktueller Nutzer mit anderen Zeichen

        //TODO: für jeden Admin den es gibt mache folgendes:
        /*Div admins = new Div(
                new H2(adminRepository.getMail()),
                new Icon(VaadinIcon.EDIT),
                new Icon(VaadinIcon.MINUS)
        );

        adminListe.add(admins);

        */


        add(willkommen, adminListe);
    }

}
