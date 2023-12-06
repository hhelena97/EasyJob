package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.dependency.StyleSheet;

@StyleSheet("DialogLayout.css")
public class DeaktivierenConfirmDialog extends DialogLayout {

    public DeaktivierenConfirmDialog(boolean answerRequired, String user, String text) {
        super(true);
        insertDialogContent("Möchten Sie den Account wirklich deaktivieren?", text, "Nein, abbrechen.", "Ja, deaktivieren.", user, "account-inaktiv");
    }
}
