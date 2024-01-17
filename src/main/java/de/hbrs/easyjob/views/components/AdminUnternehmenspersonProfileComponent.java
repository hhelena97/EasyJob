package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.PersonRepository;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@StyleSheet("JobList.css")
public class AdminUnternehmenspersonProfileComponent extends VerticalLayout {

    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final Unternehmensperson person;
    private final String style;

    VerticalLayout jobListLayout = new VerticalLayout();

    private final Unternehmen unternehmen;

    private final UnternehmenService unternehmenService;
    private final ProfilSperrenController profilSperrenController;


    public AdminUnternehmenspersonProfileComponent(PersonRepository personRepository, UnternehmenRepository unternehmenRepository,
            Unternehmensperson person, String styleClass, UnternehmenService unternehmenservice, ProfilSperrenController profilSperrenController){
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.person = person;
        this.unternehmen = person.getUnternehmen();
        this.style = styleClass;
        this.unternehmenService = unternehmenservice;
        this.profilSperrenController = profilSperrenController;
        initializeComponent();
    }

    private void initializeComponent() {

        if (person == null) {
            UI.getCurrent().navigate(PersonenVerwaltenView.class);
            return;
        }

        UI.getCurrent().getPage().addStyleSheet(style);

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //Link zu Unternehmen
        Dialog d1 = new Dialog();

        Div infos = new Div();
        infos.add(new AdminUnternehmenComponent(person.getUnternehmen() ,"AdminLayout.css",
                new ProfilDeaktivierenController(personRepository, unternehmenRepository),unternehmenService));


        d1.add(infos);

        Button dSchliessen = new Button ("Schließen");
        dSchliessen.addClassName("buttonAbbruch");
        dSchliessen.addClickListener(e -> d1.close());
        d1.getFooter().add(dSchliessen);

        Button unternehmenProfil = new Button("zum Unternehmensprofil", e-> d1.open());
        unternehmenProfil.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        unternehmenProfil.getStyle().set("color","var(--admin-dark");
        unternehmenProfil.addClassName("unternehmenProfil");

        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.setSpacing(false);
        personInfo.setAlignItems(Alignment.CENTER);
        personInfo.setAlignSelf(Alignment.CENTER);

        //personKontakt
        Paragraph p = new Paragraph("Email: " + person.getEmail());
        Paragraph pa = new Paragraph("Telefon: " + person.getTelefon());
        p.addClassName("para");
        pa.addClassName("para");
        personInfo.add(unternehmenProfil,p,pa);
        if(person.getAnschrift() != null){
            Paragraph paragraph = new Paragraph("Büroanschrift: "+ person.getAnschrift());
            paragraph.addClassName("para");
            personInfo.add(paragraph);
        }

        // Job Section
        H3 jobsTitle = new H3("Alle Stellenanzeigen:");
        jobsTitle.addClassName("untertitel");

        //hier
        jobListLayout.setWidthFull();
        jobListLayout.setAlignItems(Alignment.STRETCH);
        jobListLayout.setClassName("jobListLayout");
        List<Job> jobsUn =  unternehmenService.getAllJobsByUnternehmenspersonId(person.getId_Person());
        jobsUn.forEach(this::addJobComponentToLayout);

        add(personInfo, jobsTitle, jobListLayout);
    }


    private void addJobComponentToLayout(Job job) {
        VerticalLayout jobCardLayout = new VerticalLayout();
        jobCardLayout.addClassName("job-card");
        jobCardLayout.setPadding(false);
        jobCardLayout.setSpacing(false);
        jobCardLayout.setAlignItems(Alignment.STRETCH);
        jobCardLayout.setWidth("100%");


        // Jobtitel mit Begrenzung der Länge und RouterLink für die Details
        H1 jobTitle = new H1(job.getTitel());
        jobTitle.addClassName("job-title");

        // Jobdetails wie Unternehmen und Ort und Homeoffice
        HorizontalLayout companyAndLocation = new HorizontalLayout();
        companyAndLocation.setSpacing(false);
        companyAndLocation.setAlignItems(Alignment.CENTER);
        companyAndLocation.addClassName("company-location");

        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon ii =  i.create();
        ii.addClassName("iconsInJobIcons");


        Paragraph unternehmenProfil = new Paragraph("zum Unternehmensprofil");
        unternehmenProfil.getStyle().set("color","#289a32");
        Dialog d1 = new Dialog();

        Div infos = new Div();
        infos.add(new AdminUnternehmenComponent(person.getUnternehmen() ,"AdminLayout.css",
                new ProfilDeaktivierenController(personRepository, unternehmenRepository),unternehmenService));


        d1.add(infos);

        Button dSchliessenU = new Button ("Schließen");
        dSchliessenU.addClassName("buttonAbbruch");
        dSchliessenU.addClickListener(e -> d1.close());

        d1.getFooter().add(dSchliessenU);

        unternehmenProfil.addClickListener(e-> d1.open());


        H1 zumUnternhemen = new H1 (job.getUnternehmen().getName());
        zumUnternhemen.getStyle().set("color","#289a32");
        zumUnternhemen.addClassName("titleIcons");
        zumUnternhemen.addClickListener(e -> d1.open());

        Icon ll =  new Icon(VaadinIcon.MAP_MARKER);
        ll.addClassName("iconsInJobIcons");
        H1 titleIconsLocation = new H1();
        titleIconsLocation.addClassName("titleIcons");
        titleIconsLocation.add(job.getOrt().getOrt());

        if (job.isHomeOffice()){
            Icon gg =  new Icon(VaadinIcon.HOME);
            gg.addClassName("iconsInJobIcons");
            H1 homeOffice = new H1();
            homeOffice.addClassName("titleIcons");
            homeOffice.add("Homeoffice");
            companyAndLocation.add(ii,zumUnternhemen,ll,titleIconsLocation,gg,homeOffice);
        }else {
            companyAndLocation.add(ii,zumUnternhemen,ll,titleIconsLocation);
        }
        // Jobbeschreibung mit "...", wenn sie zu lang ist
        Paragraph jobDescription = new Paragraph(limitText(job.getFreitext(), 230));
        jobDescription.addClassName("job-description");

        // Veröffentlichungsdatum des Jobs
        LocalDate jobDate = job.getErstellt_am().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(jobDate, LocalDate.now());
        String daysAgoText = daysBetween == 0 ? "Heute" : daysBetween + (daysBetween == 1 ? " Tag" : " Tage");

        Span postedTime = new Span("Vor " + daysAgoText);
        postedTime.addClassName("posted-time");

        Button mehrInfos = new Button("mehr");
        mehrInfos.addClassName("mehrInfos");

            Dialog jobDialog = new Dialog();
            Div infosJ = new Div();
            infosJ.add(new AdminJobComponent(job, profilSperrenController));

            jobDialog.add(infosJ);

            Button dSchliessenJ = new Button ("Schließen");
            dSchliessenJ.addClassName("close-admin");
            dSchliessenJ.addClickListener(e -> jobDialog.close());

            jobDialog.getFooter().add(dSchliessenJ);

        mehrInfos.addClickListener(e -> jobDialog.open());


        jobCardLayout.add(jobTitle, companyAndLocation, jobDescription, postedTime, mehrInfos);


        jobListLayout.add(jobCardLayout);

    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}




