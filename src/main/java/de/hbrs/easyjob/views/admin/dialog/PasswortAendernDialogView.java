package de.hbrs.easyjob.views.admin.dialog;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class PasswortAendernDialogView extends DialogLayout {

    Div passwoerterEingabe = new Div(
            new PasswordField("Neues Passwort"),
            new PasswordField("Passwort wiederholen")
    );

    public PasswortAendernDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Passwort Ändern", passwoerterEingabe, "Abbrechen", "Passwort speichern");
    }
}
