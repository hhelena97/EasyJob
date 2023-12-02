package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.dependency.StyleSheet;

@StyleSheet("DialogLayout.css")
public class DeaktivierenConfirmDialogView extends DialogLayout {

    public DeaktivierenConfirmDialogView(boolean answerRequired, String user, String text) {
        super(true);
        insertDialogContent("Möchten Sie den Account wirklich deaktivieren?", text, "Nein, abbrechen.", "Ja, deaktivieren.", user, "account-inaktiv");
    }
}
