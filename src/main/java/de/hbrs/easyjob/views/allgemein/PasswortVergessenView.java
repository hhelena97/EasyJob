package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.components.ZurueckButtonText;

@Route("passwortVergessen")
@PageTitle("Passwort vergessen")
@StyleSheet("Registrieren.css")
public class PasswortVergessenView extends VerticalLayout {
    protected VerticalLayout frame = new VerticalLayout();

    public PasswortVergessenView(){
        frame.setClassName("container");

        ZurueckButtonText back = new ZurueckButtonText();

        back.addClickListener(e -> back.getUI().ifPresent(ui -> ui.navigate("login")));

        Paragraph p = new Paragraph(
                "Wenn Sie ihr Passwort vergessen haben wenden Sie sich bitte an die Administratoren:\n" +
                "\n" +
                "admin@h-brs.de");

        frame.add(back, p);
        add(frame);
    }
}
