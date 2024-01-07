package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class ZugangEntfernenDialogView extends DialogLayout {

    Div passwortConfirm = new Div(
            new PasswordField("Passwort des Zugangs"),
            new PasswordField("Passwort wiederholen")
    );

    public ZugangEntfernenDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Zugang entfernen", passwortConfirm, "Abbrechen", "Entfernen");
    }
}
