package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;
import de.hbrs.easyjob.views.student.EinstellungenAccountStudentView;
import de.hbrs.easyjob.views.student.StudentProfilView;
import de.hbrs.easyjob.views.unternehmen.EinstellungenAccountUnternehmenView;
import de.hbrs.easyjob.views.unternehmen.UnternehmenspersonProfilView;

import javax.annotation.security.RolesAllowed;

@StyleSheet("DialogLayout.css")
@StyleSheet("Einstellungen.css")
@RolesAllowed({"ROLE_STUDENT", "ROLE_UNTERNEHMENSPERSON"})

public class EinstellungenUebersichtView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        if (!sessionController.isLoggedIn() ||
                (!sessionController.hasRole("ROLE_STUDENT")) && !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
            event.rerouteTo(LoginView.class);
        }
    }
    public EinstellungenUebersichtView(SessionController sessionController, String user) {
        this.sessionController = sessionController;

        UI.getCurrent().getPage().addStyleSheet("Einstellungen.css");

        VerticalLayout frame = new VerticalLayout();

        //Links
        RouterLink linkzuruck;
        RouterLink linkDea;
        if (user.equals("Student")){
            linkzuruck = new RouterLink(StudentProfilView.class);
            linkDea = new RouterLink(EinstellungenAccountStudentView.class);
        }
        else {
            linkzuruck = new RouterLink(UnternehmenspersonProfilView.class);
            linkDea = new RouterLink(EinstellungenAccountUnternehmenView.class);
        }

        //Zurück Button-----------------------------------------------------
        Button zuruck = new ZurueckButtonRundLayout(user);
        linkzuruck.add(zuruck);

        //Einstellungen--------------------------------------------------------

        Label ueber = new Label("Einstellungen");
        ueber.addClassName("accounteinstellungen");

        Button accounts = new Button("Account");
        accounts.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        accounts.addClassName("menu-button");
        linkDea.add(accounts);
        linkDea.getStyle().set("text-decoration","none");

        //TODO: Impressum erstellen und verlinken
        Button impressum = new Button("Impressum");
        impressum.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        impressum.addClassName("menu-button");

        //Dialog beim Ausloggen klicken
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Wirklich ausloggen?");


        Button auslogButton = new Button("Ausloggen.");
        auslogButton.addClassName("confirm");
        auslogButton.addClickListener(e -> {
            if (sessionController.logout()) {
                UI.getCurrent().navigate(LoginView.class);
            }
        });

        dialog.getFooter().add(auslogButton);

        Button cancelButton = new Button("Eingeloggt bleiben.", (e) -> dialog.close());
        if (user.equals("Student")) cancelButton.addClassName("close-student");
        else cancelButton.addClassName("close-unternehmen");
        dialog.getFooter().add(cancelButton);


        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT),e -> dialog.open());
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.getStyle().set("color", "#FF3A3A");
        ausloggen.getStyle().set("padding-left", "16px");
        ausloggen.addClassName("ausloggen");

        VerticalLayout buttons = new VerticalLayout(linkDea, impressum, ausloggen);
        buttons.setSpacing(false);

        frame.add(linkzuruck,ueber, buttons);
        add(frame,dialog);
    }

}
