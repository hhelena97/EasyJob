package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.UnternehmensperonProfilController;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;


@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("UnternehmenRegistrieren.css")
@Route(value = "unternehmen/unternehmenpersonbearbeitung", layout = UnternehmenLayout.class)
public class UnternehmenspersonProfilBearbeitungView extends VerticalLayout {
    private Unternehmensperson person;
    @Autowired
    private final PersonService personService;
    @Autowired
    private final UnternehmenService unternehmenService;

    TextField vorname;
    TextField nachname;
    TextField email;
    TextField telefon;
    VerticalLayout personKontakt = new VerticalLayout();

    UnternehmenspersonProfilBearbeitungView(PersonService personService, UnternehmenService unternehmenService){
        this.personService = personService;
        this.unternehmenService = unternehmenService;
        person = (Unternehmensperson) personService.getCurrentPerson();


        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilBearbeitungView.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");
        UI.getCurrent().getPage().addStyleSheet("Registrieren.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");


        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);


        //Profil Bild
        Div profilBild = new Div();
        profilBild.addClassName("profilBild");
        if(person.getFoto() != null){
            profilBild.add(new Image(person.getFoto(), "EasyJob"));
        }


        //Link zu Bild Bearbeitung
        H2 bildBearbeiten = new H2("Bild bearbeiten");
        bildBearbeiten.addClassName("bildBearbeiten");


        // RouterLink linkUnternehmen = new RouterLink(UnternehmenProfil_Un.class);
     //   linkUnternehmen.add(unternehmenProfil);


        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);


        //personKontakt
        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");

        personKontakt.setAlignItems(Alignment.STRETCH);



        vorname = completeZeile("Vorname:" , person.getVorname());
        nachname = completeZeile("Nachname:" , person.getNachname());
        email = completeZeile("Email:", person.getEmail());
        telefon = completeZeile("Telefon:" , person.getTelefon());



        // Buttons
        Button next = new Button("fertig", new Icon(VaadinIcon.ARROW_RIGHT));
        next.addClassName("next");
        next.addClickListener(e -> updateInfos());

        Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
        back.addClassName("back");
        back.addClickListener(e -> UI.getCurrent().getPage().setLocation("/unternehmen/unternehmenperson"));

        Button cancel = new Button("Abbrechen");
        //cancel.addClickListener(e -> abbrechenDialog.openDialogOverlay());
        cancel.addClassName("cancel");

        // Buttons Container
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);

        personKontakt.add(actions);
        personKontakt.setAlignSelf(Alignment.CENTER,actions);






        personInfo.add(profilBild,bildBearbeiten);

        add(personInfo,kon,personKontakt);
    }


    private TextField completeZeile(String title, String wert){

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");



        TextField textField = new TextField();
        textField.addClassName("feld");
        textField.setPlaceholder(wert);
        //textField.setClearButtonVisible(true);


        wertH.add(textField);
        HorizontalLayout completeZeile = new HorizontalLayout(titleH,wertH);
        completeZeile.setAlignItems(Alignment.STRETCH);
        completeZeile.addClassName("completeZeile");

        personKontakt.add(completeZeile);

        return textField;


    }

    private void updateInfos(){
        System.out.println("Hi" + vorname.getValue());
        if(vorname.getValue() != null){
            person.setVorname(vorname.getValue());
        }

        if(nachname.getValue() != null){
            person.setNachname(nachname.getValue());
        }
        if(email.getValue() != null){
            person.setEmail(email.getValue());
        }
        if(telefon.getValue() != null){
            person.setTelefon(telefon.getValue());
        }

        UI.getCurrent().getPage().setLocation("/unternehmen/unternehmenperson");
    }




}
