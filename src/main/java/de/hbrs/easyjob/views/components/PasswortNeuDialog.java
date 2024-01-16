package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.PasswortService;

public class PasswortNeuDialog extends Dialog {

    private final String style;
    String name = "";

    public PasswortNeuDialog(Person person, String styleClass, PasswortService passwortService) {
        this.style = styleClass;

        addClassName(style);

        setHeaderTitle("Passwort ändern");

        if (person instanceof Admin) {
            name = person.getEmail();
        } else {
            name = person.getVorname() + " " + person.getNachname();
        }

        Paragraph info = new Paragraph("von " + name);

        PasswordField pwneu1 = new PasswordField("neues Passwort");
        PasswordField pwrp1 = new PasswordField("Passwort wiederholen");

        Paragraph p1 = new Paragraph("");
        Paragraph p2 = new Paragraph("");

        Div inhalt = new Div(info, p1, pwneu1, p2, pwrp1);

        add(inhalt);

        Button btnAbbruch = new Button("Abbrechen");
        btnAbbruch.addClassName("buttonAbbruch");
        btnAbbruch.addClickListener(e -> close());

        Button btnPasswortAendern = new Button("Passwort ändern");
        btnPasswortAendern.setClassName("buttonBestaetigen");
        btnPasswortAendern.addClickListener(e -> {
            if (passwortService.newPassword(person, pwneu1.getValue(), pwrp1.getValue())) {
                Notification.show("Passwort geändert");
                close();
            } else {
                Notification.show("Es gibt ein Problem.");
            };
        });

        Button cancelButton = new Button("abbrechen");
        cancelButton.addClickListener((e) -> close());
        cancelButton.addClassName("close");

        getFooter().add(cancelButton, btnPasswortAendern);
    }

}
