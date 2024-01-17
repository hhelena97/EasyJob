package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hbrs.easyjob.controllers.SessionController;

@StyleSheet("DialogLayout.css")
@StyleSheet("AdminLayout.css")
public class AdminAusloggen extends Div {

    public AdminAusloggen(SessionController sessionController) {


        Dialog dialogAusloggen = new Dialog();
        dialogAusloggen.setHeaderTitle("Wirklich ausloggen?");

        Button btnAbbruch = new Button("Eingeloggt bleiben");
        btnAbbruch.addClassName("close-admin");
        btnAbbruch.addClickListener(e -> dialogAusloggen.close());

        String bestaetigen = "Ausloggen";
        Button btnBestaetigen = new Button(bestaetigen);
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            sessionController.logout();
            UI.getCurrent().getPage().setLocation("/login");
        });
        dialogAusloggen.getFooter().add(btnBestaetigen, btnAbbruch);

        //der Knopf um den Ausloggen-Dialog zu öffnen
        Icon signout = new Icon(VaadinIcon.SIGN_OUT);
        signout.addClassName("signout");
        Button ausloggen = new Button(signout);
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.addClassName("ausloggen");
        ausloggen.addClickListener(e -> dialogAusloggen.open());

        add(ausloggen);
    }
}
