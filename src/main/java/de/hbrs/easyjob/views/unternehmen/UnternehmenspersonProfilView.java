package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;


@Route(value = "unternehmen/unternehmenperson", layout = UnternehmenLayout.class)
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("UnternehmenRegistrieren.css")
public class UnternehmenspersonProfilView extends VerticalLayout {

    private Unternehmensperson person;
    private final transient SessionController sessionController;
    @Autowired
    private final UnternehmenService unternehmenService;
    VerticalLayout personKontakt = new VerticalLayout();


    public UnternehmenspersonProfilView(SessionController sessionController, UnternehmenService unternehmenService) {
        this.sessionController = sessionController;
        this.unternehmenService = unternehmenService;
        person = (Unternehmensperson) sessionController.getPerson();
        initializeView();
    }

    private void initializeView(){
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");



        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);


        //Icons Einstelungen und Bearbeitung
        HorizontalLayout iconsProf = new HorizontalLayout();
        iconsProf.setPadding(false);
        iconsProf.setMargin(false);
        iconsProf.setJustifyContentMode(FlexComponent.JustifyContentMode.END);



        //EinstellungenUebersichtUnternehmenView Icons
        Icon cog = new Icon(VaadinIcon.COG);
        cog.addClassName("iconsProf");

        RouterLink link = new RouterLink(EinstellungenUebersichtUnternehmenView.class);
        link.add(cog);


        Icon pen =new Icon(VaadinIcon.PENCIL);
        pen.addClassName("iconsProf");

        RouterLink link2 = new RouterLink(UnternehmenspersonProfilBearbeitungView.class);
        link2.add(pen);

        iconsProf.add(link,link2);


        //Profil Bild
        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        boolean hasBild = person.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? person.getFoto(): "images/blank-profile-picture.png", "EasyJob");
        Image profilBild2 = platzhalterBild;
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);





        //Name
        H1 name = new H1();
        name.addClassName("name");
        //name.add("Max Mustermann");
        name.add(person.getVorname()+" "+ person.getNachname());


        //Link zu Unternehmen
        H2 unternehmenProfil = new H2("zum Unternehmensprofil");
        unternehmenProfil.addClassName("unternehmenProfil");
        unternehmenProfil.getStyle().set("color", "#323232");
        RouterLink linkUnternehmen = new RouterLink(UnternehmenProfil_Un.class);
        linkUnternehmen.add(unternehmenProfil);


        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);

        personInfo.setAlignSelf(Alignment.END,iconsProf);



        //personKontakt
        personKontakt.setAlignItems(Alignment.STRETCH);


        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");
        completeZeile("Email:" , person.getEmail());
        completeZeile("Telefon:", person.getTelefon());

        Unternehmen u = person.getUnternehmen();


        completeZeile("Büroanschrift:" , u.getKontaktdaten());



        personInfo.add(iconsProf,rahmen,name,linkUnternehmen);

        add(personInfo,kon,personKontakt);

    }
    private void completeZeile(String title, String wert){

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");
        wertH.add(wert);
        HorizontalLayout completeZeile = new HorizontalLayout(titleH,wertH);
        completeZeile.setAlignItems(Alignment.STRETCH);
        completeZeile.addClassName("completeZeile");

        personKontakt.add(completeZeile);

    }
}
