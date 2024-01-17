package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.PasswortService;

@StyleSheet("DialogLayout.css")
public class PasswortNeuDialog extends Dialog {

    String name;

    public PasswortNeuDialog(Person person, PasswortService passwortService) {

        setHeaderTitle("Passwort ändern");

        if (person instanceof Admin) {
            name = person.getEmail();
        } else {
            name = person.getVorname() + " " + person.getNachname();
        }

        Paragraph info = new Paragraph("von " + name);

        PasswordField pwneu1 = new PasswordField("Neues Passwort");
        PasswordField pwrp1 = new PasswordField("Passwort wiederholen");

        Paragraph p1 = new Paragraph("");
        Paragraph p2 = new Paragraph("");

        Div inhalt = new Div(info, p1, pwneu1, p2, pwrp1);

        add(inhalt);

        Button btnAbbruch = new Button("Abbrechen");
        btnAbbruch.addClassName("close-admin");
        btnAbbruch.addClickListener(e -> close());

        Button btnPasswortAendern = new Button("Passwort ändern");
        btnPasswortAendern.setClassName("confirm");
        btnPasswortAendern.addClickListener(e -> {
            if (passwortService.newPassword(person, pwneu1.getValue(), pwrp1.getValue())) {
                Notification.show("Passwort geändert");
                close();
            } else {
                Notification.show("Es gibt ein Problem.");
            }
        });

        getFooter().add(btnPasswortAendern, btnAbbruch);
    }

}
