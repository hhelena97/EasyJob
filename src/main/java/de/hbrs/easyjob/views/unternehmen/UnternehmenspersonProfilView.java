package de.hbrs.easyjob.views.unternehmen;

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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.views.allgemein.AccountIstInaktivView;
import de.hbrs.easyjob.views.allgemein.GesperrtePersonView;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.UnternehmenLayout;

import javax.annotation.security.RolesAllowed;


@Route(value = "unternehmen/unternehmenperson", layout = UnternehmenLayout.class)
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
@StyleSheet("UnternehmenspersonProfilView.css")
@StyleSheet("DialogLayout.css")

public class UnternehmenspersonProfilView extends VerticalLayout implements BeforeEnterObserver {

    private final transient Unternehmensperson person;
    private final transient VerticalLayout personKontakt = new VerticalLayout();

    private final transient SessionController sessionController;

    public UnternehmenspersonProfilView(SessionController sessionController) {
        this.sessionController = sessionController;
        person = (Unternehmensperson) sessionController.getPerson();
        initializeView();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn()|| !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")){
            event.rerouteTo(LoginView.class);
        }
        if(! sessionController.getPerson().getAktiv()){
            event.rerouteTo(AccountIstInaktivView.class);
        }
        if(sessionController.getPerson().getGesperrt()){
            event.rerouteTo(GesperrtePersonView.class);
        }
    }

    private void initializeView(){


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
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);





        // Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(person.getVorname()+" "+ person.getNachname());


        //Link zu Unternehmen
        H2 unternehmenProfil = new H2("zum Unternehmensprofil");
        unternehmenProfil.addClassName("unternehmenProfil");
        unternehmenProfil.getStyle().set("color", "#323232");
        RouterLink linkUnternehmen = new RouterLink(UnternehmensProfil.class);
        linkUnternehmen.add(unternehmenProfil);


        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);
        personInfo.setAlignSelf(Alignment.END,iconsProf);
        personInfo.add(iconsProf,rahmen,name,linkUnternehmen);

        //Kontaktinfo
        personKontakt.setAlignSelf(Alignment.CENTER);
        personKontakt.setMaxWidth("100em");

        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");
        HorizontalLayout konLayout = new HorizontalLayout(kon);
        konLayout.setPadding(false);
        konLayout.setAlignItems(Alignment.START);
        konLayout.setSizeFull();
        VerticalLayout infoLayout = new VerticalLayout(
                completeZeile("Email:" , person.getEmail()),
                completeZeile("Telefon:", person.getTelefon()),
                //Änderung bitte lassen! Hier nicht die Adresse vom Unternehmen sondern die Adresse von der Person!
                completeZeile("Büroanschrift:" , person.getAnschrift()));
        infoLayout.setAlignItems(Alignment.STRETCH);
        personKontakt.add(konLayout,infoLayout);
        personKontakt.setAlignItems(Alignment.START);

        add(personInfo,personKontakt);

    }
    private HorizontalLayout completeZeile(String title, String wert){

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

        return completeZeile;
    }
}
