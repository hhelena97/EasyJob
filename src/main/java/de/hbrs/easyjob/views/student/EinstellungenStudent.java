package de.hbrs.easyjob.views.student;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.control.LogoutControl;
import de.hbrs.easyjob.views.allgemein.DialogLayout;
import org.springframework.beans.factory.annotation.Autowired;


@Route("einstellungenSt")
@StyleSheet("DialogLayout.css")
public class EinstellungenStudent extends Div {

    @Autowired
    private LogoutControl logoutControl;

    public EinstellungenStudent() {
        UI.getCurrent().getPage().addStyleSheet("Einstellungen.css");

        VerticalLayout v = new VerticalLayout();
        v.addClassName("v");
        v.setAlignItems(FlexComponent.Alignment.STRETCH);

        //Zurück Icon-----------------------------------------------------
        Icon zuruck = new Icon(VaadinIcon.CHEVRON_CIRCLE_LEFT);
        zuruck.addClassName("zuruckStudent");
        zuruck.getStyle().set("color", "#A3336F");



        RouterLink linkzuruck = new RouterLink(studierendProfil.class);
        linkzuruck.add(zuruck);







        //Einstellungen--------------------------------------------------------


        Details accounts = new Details("Accounts");
        RouterLink linkDea = new RouterLink(EinstellungenAccountStudent.class);
        linkDea.add(accounts);
        linkDea.getStyle().set("text-decoration","none");
        accounts.addThemeVariants(DetailsVariant.REVERSE);

        //createDetails wird erstmal nicht genutzt
        Details app_Benachrichtigungen = createDetails("App-Benachrichtigungen");
        app_Benachrichtigungen.addThemeVariants(DetailsVariant.REVERSE);

        Details sprache = createDetails("Sprache");
        sprache.addThemeVariants(DetailsVariant.REVERSE);


        Details impressum = createDetails("Impressum");
        impressum.addThemeVariants(DetailsVariant.REVERSE);



        //Dialog beim Ausloggen klicken
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Wirklich ausloggen? ");



        Button auslogButton = new Button("Ausloggen.");
        auslogButton.addClassName("confirm");
        auslogButton.addClickListener(e -> {
            dialog.close();
            auslogButton.getUI().ifPresent(ui ->{
                logoutControl.logout();
            }
        );
        });

        dialog.getFooter().add(auslogButton);

        Button cancelButton = new Button("Eingeloggt bleiben.", (e) -> dialog.close());
        cancelButton.addClassName("close-student");
        dialog.getFooter().add(cancelButton);


        Button ausloggen = new Button("Ausloggen", new Icon(VaadinIcon.SIGN_OUT),e -> dialog.open());
        ausloggen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ausloggen.getStyle().set("color", "#FF3A3A");
        ausloggen.getStyle().set("padding-left", "16px");
        ausloggen.addClassName("ausloggen");







        v.add(linkzuruck,linkDea, app_Benachrichtigungen, sprache,impressum);
        add(v,ausloggen,dialog);

    }

    private Details createDetails(String summary, Anchor... anchors) {
        Details details = new Details(summary, createContent(anchors));
        return details;
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
