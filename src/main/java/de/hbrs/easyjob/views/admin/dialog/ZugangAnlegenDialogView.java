package de.hbrs.easyjob.views.admin.dialog;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.button.Button;
import de.hbrs.easyjob.controllers.AdminController;
import de.hbrs.easyjob.entities.Admin;
import de.hbrs.easyjob.views.components.DialogLayout;

public class ZugangAnlegenDialogView extends DialogLayout {

    private final AdminController adminController;
    private final EmailField email;
    private final PasswordField pw;
    private final PasswordField pw2;




    public ZugangAnlegenDialogView(boolean answerRequired, AdminController adminController) {
        super(true);
        this.adminController = adminController;

        Div zugangsdaten = new Div(
                email = new EmailField("E-Mail"),
                pw = new PasswordField("Passwort"),
                pw2 = new PasswordField("Passwort wiederholen")
                //TODO: Überprüfen ob pw und pw2 gleich sind
        );

        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setClassName("action-buttons");


        Button btnAdminAnlegen = new Button("Admin anlegen");
        btnAdminAnlegen.setClassName("button-adminanlegen");
        btnAdminAnlegen.addClickListener(e -> adminAnlegen());

        actionButtons.add(btnAdminAnlegen);

        zugangsdaten.add(actionButtons);

        insertContentDialogContent("Neuen Admin anlegen", zugangsdaten, "Abbrechen", "Test2");
    }

    private void adminAnlegen(){
        Admin admin = new Admin();
        admin.setEmail(email.getValue());
        admin.setPasswort(pw.getValue());
        adminController.createAdmin(admin);
        //eventuell könnte hier noch angezeigt werden, ob der Admin gespeichert wurde
        UI.getCurrent().getPage().getHistory().back();
    }

}
