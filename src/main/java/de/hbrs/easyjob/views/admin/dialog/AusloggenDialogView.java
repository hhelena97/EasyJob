package de.hbrs.easyjob.views.admin.dialog;

import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.views.components.DialogLayout;

public class AusloggenDialogView extends DialogLayout {
    public AusloggenDialogView(boolean answerRequired) {
        super(true);

        insertDialogContent("Wirklich ausloggen?","","Eingeloggt bleiben.", "Ausloggen.", "admin", "login" );
    }
}
