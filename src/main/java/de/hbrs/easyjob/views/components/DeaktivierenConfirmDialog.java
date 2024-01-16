package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.dependency.StyleSheet;

@StyleSheet("DialogLayout.css")
public class DeaktivierenConfirmDialog extends DialogLayout {

    public DeaktivierenConfirmDialog(String user, String text) {
        super(true);
        String title = null;
        if(user.equals("Student")) title = "Möchtest Du den Account wirklich deaktivieren?";
        else if(user.equals("Unternehmen")) title = "Möchten Sie den Account wirklich deaktivieren?";
        insertDialogContent(title, text, "Nein, abbrechen.", "Ja, deaktivieren.", user, "account-inaktiv");
    }
}
