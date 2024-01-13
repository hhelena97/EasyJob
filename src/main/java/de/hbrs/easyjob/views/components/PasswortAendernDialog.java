package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.PasswortService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Eine Klasse für ein Dialog-Fenster das nach dem alten Passwort fragt und ein neues Passwort
 * zur mitgegebenen Person speichert.
 * im mitgegebenen Stylesheet kann man
 *      - die Passwort-Felder mit .passwortFeld definieren
 *      - den confirm-Button mit .confirm definieren
 *      - den Abbrechen-Button mit .close
 */

public class PasswortAendernDialog extends Dialog {


    private final String style;



    public PasswortAendernDialog(Person person, String styleClass, PasswortService passwortService){
        this.style = styleClass;



        addClassName(style);

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

        Button btnPasswortAendern = new Button("Passwort ändern");
        btnPasswortAendern.addClassName("confirm");

        btnPasswortAendern.addClickListener(e -> {
            passwortService.changePassword(
                    person.getEmail(), altesPasswort.getValue(), neuesPasswort.getValue(), passwortWiederholen.getValue());
            close();
        });

        Button cancelButton = new Button("abbrechen");
        cancelButton.addClickListener((e) -> close());
        cancelButton.addClassName("close");

        getFooter().add(cancelButton, btnPasswortAendern);
    }

}
