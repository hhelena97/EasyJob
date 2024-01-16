package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.views.components.ZurueckButtonText;

@Route("gesperrtePerson")
@PageTitle("Es gibt ein Problem")
@StyleSheet("Registrieren.css")
public class GesperrtePersonView extends VerticalLayout{
    protected VerticalLayout frame = new VerticalLayout();

    public GesperrtePersonView(){
        frame.setClassName("container");

        ZurueckButtonText back = new ZurueckButtonText();

        back.addClickListener(e -> back.getUI().ifPresent(ui -> ui.navigate("login")));

        Paragraph p = new Paragraph(
                "Es gibt ein Problem. Bitte wenden Sie sich an die Administratoren:\n" +
                        "\n" +
                        "admin@h-brs.de");

        frame.add(back, p);
        add(frame);
    }
}
