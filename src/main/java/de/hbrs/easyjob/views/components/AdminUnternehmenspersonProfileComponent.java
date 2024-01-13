package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.JobProfilController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.admin.PersonenVerwaltenView;
import de.hbrs.easyjob.views.student.UnternehmenProfilView;
import de.hbrs.easyjob.views.unternehmen.JobDetailsView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@StyleSheet("JobList.css")
public class AdminUnternehmenspersonProfileComponent extends VerticalLayout {

    private Unternehmensperson person;
    private final String style;

    VerticalLayout jobListLayout = new VerticalLayout();

    private Unternehmen unternehmen;

    private final UnternehmenService unternehmenService;
    JobProfilController jobController = new JobProfilController();


    public AdminUnternehmenspersonProfileComponent(Unternehmensperson person, String styleClass, UnternehmenService unternehmenservice){
        this.person = person;
        this.unternehmen = person.getUnternehmen();
        this.style = styleClass;
        this.unternehmenService = unternehmenservice;
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
        Paragraph unternehmenProfil = new Paragraph("zum Unternehmensprofil");
        unternehmenProfil.addClassName("unternehmenProfil");
        /*RouterLink linkUnternehmen = new RouterLink(AdminUnternehmenComponent.class);
        linkUnternehmen.add(unternehmenProfil);*/


        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);
        personInfo.setAlignSelf(Alignment.END);

        //personKontakt
        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");
        Paragraph p = new Paragraph("Email: " + person.getEmail());
        Paragraph pa = new Paragraph("Telefon: " + person.getTelefon());
        p.addClassName("para");
        pa.addClassName("para");

        personInfo.add(kon,p,pa);

        // Job Section
        H3 jobsTitle = new H3("Eingestellte Jobs");
        jobsTitle.addClassName("Untertitel");

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
        RouterLink linkJobTitle = new RouterLink("", de.hbrs.easyjob.views.student.JobDetailsView.class, job.getId_Job());
        linkJobTitle.add(new H1(job.getTitel()));
        linkJobTitle.addClassName("job-title");

        // Jobdetails wie Unternehmen und Ort und Homeoffice
        HorizontalLayout companyAndLocation = new HorizontalLayout();
        companyAndLocation.setSpacing(false);
        companyAndLocation.setAlignItems(Alignment.CENTER);
        companyAndLocation.addClassName("company-location");

        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon ii =  i.create();
        ii.addClassName("iconsInJobIcons");

        RouterLink titleIconsUnternehmen = new RouterLink("", UnternehmenProfilView.class, job.getUnternehmen().getId_Unternehmen());
        titleIconsUnternehmen.addClassName("titleIcons");
        titleIconsUnternehmen.add(job.getUnternehmen().getName());

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
            companyAndLocation.add(ii,titleIconsUnternehmen,ll,titleIconsLocation,gg,homeOffice);
        }else {
            companyAndLocation.add(ii,titleIconsUnternehmen,ll,titleIconsLocation);
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


        jobCardLayout.add(linkJobTitle, companyAndLocation, jobDescription, postedTime);


        jobListLayout.add(jobCardLayout);

    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}




