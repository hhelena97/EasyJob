package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.LogoutController;
import de.hbrs.easyjob.repositories.PersonRepository;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Route("account-inaktiv")
@PageTitle("Inaktives Profil")
@RolesAllowed({"ROLE_UNTERNEHMENSPERSON", "ROLE_STUDENT"})
public class AccountIstInaktivView extends VerticalLayout {

    private LogoutController logoutController;
    private PersonRepository personRepository;

    public AccountIstInaktivView(LogoutController logoutController) {
        this.logoutController = logoutController;
        UI.getCurrent().getPage().addStyleSheet("AccountIstInaktiv.css");

        H2 inaktiv = new H2("Ihr Account ist inaktiv.");
        inaktiv.addClassName("inaktiv");

        H1 reaktivieren = new H1("Account reaktivieren");
        reaktivieren.addClassName("reaktivieren");

        H1 oder = new H1("oder");
        oder.addClassName("oder");

        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT));
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");

        ausloggen.addClickListener(e -> {

            HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
            HttpServletResponse response = VaadinServletResponse.getCurrent().getHttpServletResponse();

            System.out.println("Ausloggen-Knopf bei Inaktiv geklickt");
            System.out.println("Request: " + request);
            System.out.println("Response: " + response);
            System.out.println("Session: " + VaadinSession.getCurrent());

            this.logoutController.logout(request, response);
            UI.getCurrent().navigate(LoginView.class);      // mit dieser Zeile wird die Session invalidiert
                                                            // ohne die Zeile wird die Session nicht invalidiert... :(
            System.out.println("Ausgeloggt");
            System.out.println("Session: " + VaadinSession.getCurrent());
            }
        );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(inaktiv, reaktivieren, oder, ausloggen);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setSpacing(false);

        add(verticalLayout);
    }

    /*
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Wenn Aufruf durch Dialog erfolgt ist, führe Deaktivierung aus
        if (event.getTrigger() == NavigationTrigger.UI_NAVIGATE) {

        } else {
            // Leite zurück zur vorherigen Seite
        }
    }*/
}
