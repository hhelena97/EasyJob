package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.views.unternehmen.EinstellungenAccountUnternehmenView;
import de.hbrs.easyjob.views.unternehmen.UnternehmenspersonProfilView;

@StyleSheet("DialogLayout.css")
@StyleSheet("Einstellungen.css")
public class EinstellungenUebersichtView extends Div {
    public EinstellungenUebersichtView(SessionController sessionController, String style) {
        // TODO: @Rafi Alle Links und Weiterleitungen fixen!

        VerticalLayout v = new VerticalLayout();
        v.addClassName("v");
        v.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Zurück Icon
        Icon zuruck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zuruck.getStyle().set("color", style);

        RouterLink linkzuruck = new RouterLink(UnternehmenspersonProfilView.class);
        linkzuruck.add(zuruck);

        // Einstellungen
        Details accounts = new Details("Accounts");
        RouterLink linkDea = new RouterLink(EinstellungenAccountUnternehmenView.class);
        linkDea.getStyle().set("text-decoration", "none");
        linkDea.add(accounts);
        accounts.addThemeVariants(DetailsVariant.REVERSE);

        // TODO: @Rafi ist nicht mit Figma gleich, wurde entfernt
        // createDetails wird erstmal nicht genutzt
        Details appBenachrichtigungen = createDetails("Benachrichtigungen");
        appBenachrichtigungen.addThemeVariants(DetailsVariant.REVERSE);

        Details sprache = createDetails("Sprache");
        sprache.addThemeVariants(DetailsVariant.REVERSE);


        Details impressum = createDetails("Impressum");
        impressum.addThemeVariants(DetailsVariant.REVERSE);


        // Dialog beim Ausloggen klicken
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Wirklich ausloggen? ");

        Button auslogButton = new Button("Ausloggen.");
        auslogButton.addClassName("confirm");
        auslogButton.addClickListener(e -> {
            dialog.close();
            auslogButton.getUI().ifPresent(ui -> {
                sessionController.logout();
                ui.navigate(LoginView.class);
            });
        });

        dialog.getFooter().add(auslogButton);

        Button cancelButton = new Button("Eingeloggt bleiben.", e -> dialog.close());
        cancelButton.addClassName("close-unternehmen");
        dialog.getFooter().add(cancelButton);

        // Ausloggen Button in Einstellungen Liste
        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT), e -> dialog.open());
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.getStyle().set("color", "#FF3A3A");
        ausloggen.getStyle().set("padding-left", "16px");
        ausloggen.addClassName("ausloggen");

        v.add(linkzuruck, linkDea, appBenachrichtigungen, sprache, impressum);
        add(v, ausloggen, dialog);
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
}
