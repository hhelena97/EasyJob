package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.PasswortService;

/**
 * Eine Klasse für ein Dialog-Fenster das nach dem alten Passwort fragt und ein neues Passwort
 * zur mitgegebenen Person speichert.
 * im mitgegebenen Stylesheet kann man
 *      - die Passwort-Felder mit .passwortFeld definieren
 *      - den confirm-Button mit .confirm definieren
 *      - den Abbrechen-Button mit .close
 */

@StyleSheet("DialogLayout.css")
public class PasswortAendernDialog extends Dialog {


    public PasswortAendernDialog(Person person, String user, PasswortService passwortService){


        setHeaderTitle("Passwort ändern");

        PasswordField altesPasswort = new PasswordField("Altes Passwort");
        PasswordField neuesPasswort = new PasswordField("Neues Passwort");
        PasswordField passwortWiederholen = new PasswordField("Passwort wiederholen");
        Paragraph p1 = new Paragraph("");
        Paragraph p2 = new Paragraph("");

        altesPasswort.addClassName("passwortFeld");
        neuesPasswort.addClassName("passwortFeld");
        passwortWiederholen.addClassName("passwortFeld");

        Div passwortFelder = new Div(altesPasswort, p1, neuesPasswort, p2, passwortWiederholen);

        add(passwortFelder);

        Button btnPasswortAendern = new Button("Passwort speichern");
        btnPasswortAendern.addClassName("confirm");

        btnPasswortAendern.addClickListener(e -> {
            passwortService.changePassword(
                    person.getEmail(), altesPasswort.getValue(), neuesPasswort.getValue(), passwortWiederholen.getValue());
            close();
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener((e) -> close());
        if(user.equals("Student")) cancelButton.addClassName("close-student");
        if (user.equals("Admin")) cancelButton.addClassName("close-admin");
        else cancelButton.addClassName("close-unternehmen");

        getFooter().add(btnPasswortAendern, cancelButton);
    }

}
