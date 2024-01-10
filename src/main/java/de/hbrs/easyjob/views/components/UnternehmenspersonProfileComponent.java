package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.JobProfilController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.PersonSuchenService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.unternehmen.JobDetailsView;
import de.hbrs.easyjob.views.unternehmen.UnternehmenProfil_Un;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UnternehmenspersonProfileComponent extends VerticalLayout {

    private Unternehmensperson person;
    private final String style;

    VerticalLayout personKontakt = new VerticalLayout();

    private Unternehmen unternehmen;

    private final UnternehmenService unternehmenService;
    JobProfilController jobController = new JobProfilController();

    //Job Methode
    Section sec = new Section();
    Scroller scroller = new Scroller();
    HorizontalLayout jobs = new HorizontalLayout();

    public UnternehmenspersonProfileComponent(Unternehmensperson person, String styleClass, UnternehmenService unternehmenservice){
        this.person = person;
        this.style = styleClass;
        this.unternehmenService = unternehmenservice;
        initializeComponent();
    }

    private void initializeComponent() {

        if (person == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }

        UI.getCurrent().getPage().addStyleSheet(style);

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        Div infos = new Div();

        //Profil Bild
        Div profilBild = new Div();
        profilBild.addClassName("profilBild");
        profilBild.add(new Image(person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png", "EasyJob"));

        //Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(person.getVorname() +" "+ person.getNachname());

        //Link zu Unternehmen
        H2 unternehmenProfil = new H2("zum Unternehmensprofil");
        unternehmenProfil.addClassName("unternehmenProfil");
        unternehmenProfil.getStyle().set("color", "#323232");
        //TODO: hier noch ändern zur Admin-Sicht auf UnternehmenProfil
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

        infos.add(profilBild,name,linkUnternehmen, personInfo, kon, personKontakt);

        // Job Section
        H1 jobsTitle = new H1("JobsUebersichtView");
        jobsTitle.addClassName("title");


        List<Job> jobsUn =  unternehmenService.getAllJobs(unternehmen.getId_Unternehmen());
        jobsUn.forEach(this::jobMeth);

        sec.setMaxWidth("100%");
        sec.setWidth("100%");
        sec.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");



        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.addClassName("scroller");



        jobs.setPadding(true);
        jobs.getStyle().set("display", "inline-flex");

        scroller.setContent(jobs);
        sec.add(scroller);

        add(infos, jobsTitle, sec);

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

    private void jobMeth(Job jobSet){

        Div job = new Div();
        job.addClassName("job");
        job.setWidth(getMaxWidth());

        H1 jobBeschreibung = new H1();
        jobBeschreibung.addClassName("jobBeschreibung");
        jobBeschreibung.add(jobController.getTitel(jobSet));

        HorizontalLayout jobIcons = new HorizontalLayout();
        jobIcons.setSpacing(false);
        jobIcons.setAlignItems(Alignment.CENTER);

        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon ii =  i.create();
        ii.addClassName("iconsInJobIcons");

        H1 titleIconsUnternehmen = new H1();
        titleIconsUnternehmen.addClassName("titleIcons");
        titleIconsUnternehmen.add(unternehmen.getName());

        IconFactory l = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon ll =  l.create();
        ll.addClassName("iconsInJobIcons");
        H1 titleIconsLocation = new H1();
        titleIconsLocation.addClassName("titleIcons");
        titleIconsLocation.add(jobController.getOrt(jobSet));

        IconFactory g = FontAwesome.Solid.GRADUATION_CAP;
        Icon gg =  g.create();
        gg.addClassName("iconsInJobIcons");
        H1 titleIconsJob = new H1();
        titleIconsJob.addClassName("titleIcons");
        titleIconsJob.add(jobController.getJobKategorie(jobSet));

        jobIcons.add(ii,titleIconsUnternehmen,ll,titleIconsLocation,gg,titleIconsJob);


        Paragraph jobDescription = new Paragraph(limitText(jobSet.getFreitext(), 230));

        H1 jobBText = new H1();
        jobBText.addClassName("jobBText");

        jobBText.add(jobDescription);




        // Veröffentlichungsdatum des JobsUebersichtView
        LocalDate jobDate = jobSet.getErstellt_am().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(jobDate, LocalDate.now());
        String daysAgoText = daysBetween == 0 ? "Heute" : daysBetween + (daysBetween == 1 ? " Tag" : " Tage");

        Span postedTime = new Span("Vor " + daysAgoText);


        H1 jobDatum = new H1();
        jobDatum.addClassName("jobDatum");
        jobDatum.add(postedTime);


        job.add(jobBeschreibung,jobIcons,jobBText,jobDatum);


//TODO: Link zu AdminSicht auf Job
        RouterLink linkJobDetails = new RouterLink(JobDetailsView.class);
        linkJobDetails.add(job);
        jobs.add(linkJobDetails);
    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

}
