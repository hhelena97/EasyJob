package de.hbrs.easyjob.views.student;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.entities.Meldung;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.ZurueckButtonRundLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "student-unternehmensprofilview" , layout = StudentLayout.class)
@StyleSheet("StudentProfilView.css")
@StyleSheet("UnternehmenspersonProfilView.css")
@StyleSheet("JobList.css")
@StyleSheet("DialogLayout.css")
@RolesAllowed("ROLE_STUDENT")
public class UnternehmensPersonProfilView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Unternehmensperson person;
    @Autowired
    private final PersonService personService;
    @Autowired
    private final UnternehmenService unternehmenService;

    private final MeldungController meldungController;
    VerticalLayout personKontakt = new VerticalLayout();

    public UnternehmensPersonProfilView(PersonService personService, UnternehmenService unternehmenService, MeldungController meldungController) {
        this.personService = personService;
        this.unternehmenService = unternehmenService;
        this.meldungController = meldungController;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer personID) {
        if ( personID != null) {
            person = (Unternehmensperson) personService.getPersonByID(personID);
            if(person==null){
                throw new RuntimeException("Etwas schief gelaufen!");
            }
            initializeView();
        } else {
            add(new H3("Job-Details konnten nicht geladen werden."));
        }
    }

    private void initializeView(){

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //Profil Bild
        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        boolean hasBild = person.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? person.getFoto(): "images/blank-profile-picture.png", "EasyJob");
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        //Zurück Button
        ZurueckButtonRundLayout zurueck = new ZurueckButtonRundLayout("Student");
        zurueck.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());

        // Code für Melde-Funktion:
        HorizontalLayout frame = new HorizontalLayout();
        frame.setPadding(false);
        frame.setMargin(false);
        frame.setWidthFull();
        frame.setAlignItems(Alignment.CENTER);
        frame.setJustifyContentMode(JustifyContentMode.BETWEEN);
        VerticalLayout dotsLayout = new VerticalLayout();
        dotsLayout.setWidth("fit-content");

        // Drei-Punkte-Icon für das Dropdown-Menü
        Icon dots = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        dots.getStyle().set("cursor", "pointer");
        dots.setSize("1em");

        // Dropdown-Menü erstellen
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(dots);
        contextMenu.setOpenOnClick(true);
        MenuItem melden = contextMenu.addItem("Melden", e -> {
            Meldung meldung = new Meldung();
            meldungController.saveMeldung(meldung, person);
            Notification.show("Gemeldet", 3000, Notification.Position.TOP_STRETCH);
        });
        melden.getElement().getStyle().set("color", "red");

        dotsLayout.add(dots);
        frame.add(zurueck, dotsLayout);
        add(frame);

        // Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(person.getVorname()+" "+ person.getNachname());


        //Link zu Unternehmen
        HorizontalLayout unternehmenProfil = new HorizontalLayout();
        unternehmenProfil.setSpacing(false);
        unternehmenProfil.setAlignItems(Alignment.CENTER);
        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon briefcase =  i.create();
        briefcase.addClassName("iconsInJobIcons");
        RouterLink linkUnternehmen = new RouterLink(UnternehmenProfilView.class);
        linkUnternehmen.add(person.getUnternehmen().getName());
        unternehmenProfil.add(briefcase, linkUnternehmen);

        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);

        personInfo.setAlignSelf(Alignment.END,frame);


        //personKontakt
        personKontakt.setAlignItems(Alignment.STRETCH);

        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");
        completeZeile("Email:" , person.getEmail());
        completeZeile("Telefon:", person.getTelefon());

        Unternehmen u = person.getUnternehmen();

        completeZeile("Büroanschrift:" , u.getKontaktdaten());

        personInfo.add(frame,rahmen,name,unternehmenProfil);

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