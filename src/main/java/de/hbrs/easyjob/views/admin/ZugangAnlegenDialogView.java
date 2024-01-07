package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class ZugangAnlegenDialogView extends DialogLayout {

    Div zugangsdaten = new Div(
            new EmailField("E-Mail"),
            new PasswordField("Passwort"),
            new PasswordField("Passwort wiederholen")
    );

    public ZugangAnlegenDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Neuen Zugang anlegen", zugangsdaten, "Abbrechen", "Admin anlegen");
    }
}
