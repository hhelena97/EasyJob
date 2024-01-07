package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class EigenesPasswortAendernDialogView extends DialogLayout {

    Div passwoeterAendern = new Div(
            new PasswordField("Altes Passwort"),
            new PasswordField("Neues Passwort"),
            new PasswordField("Passwort wiederholen")
    );

    public EigenesPasswortAendernDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Passwort ändern", passwoeterAendern, "Abbrechen", "Passwort speichern");
    }
}
