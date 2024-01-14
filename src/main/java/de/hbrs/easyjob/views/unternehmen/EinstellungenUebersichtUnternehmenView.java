package de.hbrs.easyjob.views.unternehmen;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;
import de.hbrs.easyjob.views.student.StudentProfilView;

import javax.annotation.security.RolesAllowed;


@Route("unternehmen/einstellungen")
@StyleSheet("DialogLayout.css")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class EinstellungenUebersichtUnternehmenView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
            event.rerouteTo(LoginView.class);
        }
    }

    public EinstellungenUebersichtUnternehmenView(SessionController sessionController) {
        this.sessionController = sessionController;
        UI.getCurrent().getPage().addStyleSheet("Einstellungen.css");

        VerticalLayout frame = new VerticalLayout();

        //Zurück Button-----------------------------------------------------
        Button zuruck = new ZurueckButtonRundLayout("Unternehmen");
        RouterLink linkzuruck = new RouterLink(StudentProfilView.class);
        linkzuruck.add(zuruck);


        //Einstellungen--------------------------------------------------------

        Label ueber = new Label("Einstellungen");
        ueber.addClassName("accounteinstellungen");

        Button accounts = new Button("Account");
        accounts.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        accounts.addClassName("menu-button");
        RouterLink linkDea = new RouterLink(EinstellungenAccountUnternehmenView.class);
        linkDea.getStyle().set("text-decoration","none");
        linkDea.add(accounts);

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
        cancelButton.addClassName("close-unternehmen");
        dialog.getFooter().add(cancelButton);



        //Ausloggen Button in Einstellungen Liste
        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT),e -> dialog.open());
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.getStyle().set("color", "#FF3A3A");
        ausloggen.getStyle().set("padding-left", "16px");
        ausloggen.addClassName("ausloggen");

        VerticalLayout buttons = new VerticalLayout(linkDea, impressum, ausloggen);
        buttons.setSpacing(false);

        frame.add(linkzuruck,ueber, buttons);
        add(frame, dialog);

    }

    private Details createDetails(String summary, Anchor... anchors) {
        return new Details(summary, createContent(anchors));
    }

    private VerticalLayout createContent(Anchor... anchors) {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);
        content.add(anchors);

        return content;
    }

    private Anchor createStyledAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);
        anchor.getStyle().set("color", "#FF3A3A");
        anchor.getStyle().set("text-decoration", "underline");

        return anchor;
    }



}
