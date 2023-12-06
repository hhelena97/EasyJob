package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.UnternehmensProfilController;
import de.hbrs.easyjob.controllers.UnternehmensperonProfilController;
import de.hbrs.easyjob.views.components.UnternehmenLayout;


@Route(value = "unternehmen/unternehmenperson", layout = UnternehmenLayout.class)
@RouteAlias(value = "p" , layout = UnternehmenLayout.class)
public class UnternehmenspersonProfilView extends VerticalLayout {

    UnternehmensperonProfilController person = new UnternehmensperonProfilController();
    VerticalLayout personKontakt = new VerticalLayout();
    UnternehmenspersonProfilView(){
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

        iconsProf.add(link,pen);


        //Profil Bild
        Div profilBild = new Div();
        profilBild.addClassName("profilBild");
        if(person.getFoto() != null){
            profilBild.add(new Image(person.getFoto(), "EasyJob"));
        }





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

        UnternehmensProfilController  u = new UnternehmensProfilController();
        completeZeile("Büroanschrift:" , u.getUnternehmensOrte(person.getUnternehmen()));




        personInfo.add(iconsProf,profilBild,name,linkUnternehmen);

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
