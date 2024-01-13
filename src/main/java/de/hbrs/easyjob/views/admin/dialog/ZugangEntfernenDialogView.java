package de.hbrs.easyjob.views.admin.dialog;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.PasswordField;
import de.hbrs.easyjob.views.components.DialogLayout;

public class ZugangEntfernenDialogView extends DialogLayout {


    String email = "Email hier automatisch einfügen";
    Paragraph p = new Paragraph("Wollen Sie " + email + " wirklich löschen?");

    Div hinweis = new Div(p);

    public ZugangEntfernenDialogView(boolean answerRequired) {
        super(true);
        insertContentDialogContent("Zugang entfernen", hinweis, "Abbrechen", "Entfernen");
    }
}
