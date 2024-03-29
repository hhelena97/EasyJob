package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.repositories.JobRepository;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Route("account-inaktiv")
@PageTitle("Inaktives Profil")
@RolesAllowed({"ROLE_UNTERNEHMENSPERSON", "ROLE_STUDENT"})
public class AccountIstInaktivView extends VerticalLayout {

    private final transient SessionController sessionController;

    private PersonRepository personRepository;
    private UnternehmenRepository unternehmenRepository;
    private JobRepository jobRepository;

    public AccountIstInaktivView(SessionController sessionController, PersonRepository personRepository,
                                 JobRepository jobRepository, UnternehmenRepository unternehmenRepository) {
        this.sessionController = sessionController;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.jobRepository = jobRepository;
        UI.getCurrent().getPage().addStyleSheet("AccountIstInaktiv.css");

        H2 inaktiv = new H2("Ihr Account ist inaktiv.");
        inaktiv.addClassName("inaktiv");

        H1 reaktivieren = new H1("Account reaktivieren");
        reaktivieren.addClassName("reaktivieren");
        Dialog d = new Dialog();
        d.setHeaderTitle("Account wirklich reaktivieren?");
        Div info = new Div(new Paragraph("Dein Profil wird für andere sichtbar" +
                " und du kannst wieder Nachrichten erhalten."));
        d.add(info);

        Button btnReaktivieren = new Button("Account reaktivieren");
        btnReaktivieren.addClassName("confirm");

        btnReaktivieren.addClickListener(e -> {
            ProfilDeaktivierenController entsperrenController = new ProfilDeaktivierenController(personRepository, unternehmenRepository, jobRepository);
            entsperrenController.profilReaktivierenPerson(sessionController.getPerson());
            d.close();
            UI.getCurrent().getPage().setLocation("/login");
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener((e) -> d.close());

        d.getFooter().add(btnReaktivieren, cancelButton);

        reaktivieren.addClickListener(e -> d.open());



        H1 oder = new H1("oder");
        oder.addClassName("oder");

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Wirklich ausloggen?");

        Button auslogButton = new Button("Ausloggen.");
        auslogButton.addClassName("confirm");
        auslogButton.addClickListener(e -> {
            if (sessionController.logout()) {
                UI.getCurrent().getPage().setLocation("/login");
            }
        });

        Button abbrechen = new Button("Abbrechen", (e) -> dialog.close());

        dialog.getFooter().add(abbrechen, auslogButton);

        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT));
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        ausloggen.addClickListener(e -> dialog.open());




/*
        ausloggen.addClickListener(e -> {

            HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
            HttpServletResponse response = VaadinServletResponse.getCurrent().getHttpServletResponse();

            System.out.println("Ausloggen-Knopf bei Inaktiv geklickt");
            System.out.println("Request: " + request);
            System.out.println("Response: " + response);
            System.out.println("Session: " + VaadinSession.getCurrent());

            // Dieser Logout funktioniert nicht ganz richtig, da hier nach das beforeLeave-Event der EinstellungenAccountView aufgerufen wird und die navigation zur LoginView somit überschrieben wird durch den Aufruf in der EinstellungenAccountView.
            // Um das Problem zu lösen müsste die Klasse DeaktivierenConfirmDialog in der Lage sein Buttons zu übernehmen statt nur die Navigation zu übernehmen.
            // Siehe die Umsetzung in den Registrieren-Views.
            // Es sollte im Idealfall ein Ticket erstellt werden, um das Problem zu lösen.
            if (sessionController.logout()) {
                UI.getCurrent().navigate(LoginView.class);
            }
            System.out.println("Ausgeloggt");
            System.out.println("Session: " + VaadinSession.getCurrent());
            }
        );


 */
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
