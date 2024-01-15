package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hbrs.easyjob.controllers.SessionController;


@StyleSheet("AdminLayout.css")
public class AdminAusloggen extends Div {
    private final SessionController sessionController;
    private String style;

    public AdminAusloggen(SessionController sessionController, String styleDatei) {
        this.sessionController = sessionController;
        this.style = styleDatei;

        addClassName(style);


        Dialog dialogAusloggen = new Dialog();
        dialogAusloggen.add(new Paragraph("Wollen Sie sich wirklich ausloggen"));

        Button btnAbbruch = new Button("Abbrechen");
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

        add(ausloggen);
    }
}
