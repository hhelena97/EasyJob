package de.hbrs.easyjob.views.admin.dialog;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class AdminPasswortAendernDialogView extends DialogLayout {

    Div passwoeterAendern = new Div(
            new PasswordField("Altes Passwort"),
            new PasswordField("Neues Passwort"),
            new PasswordField("Passwort wiederholen")
    );

    public AdminPasswortAendernDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Passwort ändern", passwoeterAendern, "Abbrechen", "Passwort speichern");
    }
}
