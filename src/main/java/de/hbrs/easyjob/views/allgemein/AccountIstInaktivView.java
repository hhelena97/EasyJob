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
import de.hbrs.easyjob.control.LogoutControl;
import de.hbrs.easyjob.repository.PersonRepository;

@Route("account-inaktiv")
@PageTitle("Inaktives Profil")
public class AccountIstInaktivView extends VerticalLayout {

    private LogoutControl logoutControl;
    private PersonRepository personRepository;

    public AccountIstInaktivView() {
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

        ausloggen.addClickListener(e -> ausloggen.getUI().ifPresent(ui -> {
                    new LogoutControl(personRepository).logout();
                    System.out.println("Ausgeloggt"); ui.navigate("login");
                }
        ));

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
