package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.unternehmen.EinstellungenUebersichtUnternehmenView;
import de.hbrs.easyjob.views.unternehmen.UnternehmenProfil_Un;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "student-unternehmensprofilview" , layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
public class UnternehmenspersonProfilView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Unternehmensperson person;
    @Autowired
    private final PersonService personService;
    @Autowired
    private final UnternehmenService unternehmenService;
    VerticalLayout personKontakt = new VerticalLayout();

    public UnternehmenspersonProfilView(PersonService personService, UnternehmenService unternehmenService) {
        this.personService = personService;
        this.unternehmenService = unternehmenService;
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

        //Name
        H1 name = new H1();
        name.addClassName("name");
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

        personInfo.setAlignSelf(Alignment.END);

        //personKontakt
        personKontakt.setAlignItems(Alignment.STRETCH);


        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");
        completeZeile("Email:" , person.getEmail());
        completeZeile("Telefon:", person.getTelefon());

        completeZeile("Büroanschrift:" , unternehmenService.getUnternehmensOrte(person.getUnternehmen()));

        personInfo.add(profilBild,name,linkUnternehmen);

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
