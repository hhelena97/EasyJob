package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.controllers.JobProfilController;
import de.hbrs.easyjob.controllers.UnternehmenProfilController;
import de.hbrs.easyjob.controllers.UnternehmensperonProfilController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repositories.JobRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

// TODO: Zusammenführen mit ProfilView.java

@PageTitle("Unternehmen Profil")
@AnonymousAllowed
public class UnternehmenProfil_Un extends VerticalLayout {

    UnternehmensperonProfilController person = new UnternehmensperonProfilController();
    Unternehmen unternehmenPerson = person.getUnternehmen();

    private final JobRepository jobRepository;
    UnternehmenProfilController unternehmen;

    JobProfilController jobController = new JobProfilController();

    //Job Methode
    Section sec = new Section();
    Scroller scroller = new Scroller();
    HorizontalLayout jobs = new HorizontalLayout();

    public UnternehmenProfil_Un(JobRepository jobRepository){
        UI.getCurrent().getPage().addStyleSheet("unternehmenProfil_Un.css");
        this.jobRepository = jobRepository;
        unternehmen = new UnternehmenProfilController(jobRepository);

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        VerticalLayout v = new VerticalLayout();
        v.addClassName("v");
        v.setSizeFull();
        v.setHeight("30%");
        v.setPadding(false);
        v.setMargin(false);
        v.setAlignItems(START);
        setJustifyContentMode(JustifyContentMode.START);


        Div bildUnternehmen = new Div();
        bildUnternehmen.addClassName("bildUnternehmen");
        bildUnternehmen.setWidth(getMaxWidth());


        //Section Unternehmen Info
        HorizontalLayout unternehmenInfo = new HorizontalLayout();
        unternehmenInfo.addClassName("unternehmenInfo");
        unternehmenInfo.setSizeFull();
        unternehmenInfo.setHeight("14%");
        unternehmenInfo.setPadding(false);
        unternehmenInfo.setMargin(false);
        unternehmenInfo.setAlignItems(Alignment.CENTER);

        Div unternehmenIcon = new Div();
        unternehmenIcon.addClassName("unternehmenIcon");
        if(unternehmen.getUnternehmensLogo(unternehmenPerson)  != null){
            unternehmenIcon.add(new Image(unternehmen.getUnternehmensLogo(unternehmenPerson), "Logo"));
        }
        unternehmenIcon.add();


        VerticalLayout unternehmenInfoRecht = new VerticalLayout();
        unternehmenInfoRecht.addClassName("unternehmenInfoRecht");
        unternehmenInfoRecht.setWidth(getMaxWidth());
        unternehmenInfoRecht.setHeight(getMaxHeight());
        unternehmenInfoRecht.setPadding(false);
        unternehmenInfoRecht.setMargin(false);
        unternehmenInfoRecht.setSpacing(false);

        H1 grosseTitleUnternehmen = new H1();
        grosseTitleUnternehmen.addClassName("grosseTitleUnternehmen");
        grosseTitleUnternehmen.add(unternehmen.getUnternehmensName(unternehmenPerson));


        HorizontalLayout verifiziertplusIcon = new HorizontalLayout();
        verifiziertplusIcon.addClassName("verifiziertplusIcon");
        verifiziertplusIcon.setSpacing(false);
        verifiziertplusIcon.setAlignItems(Alignment.CENTER);
        verifiziertplusIcon.setWidth(getMaxWidth());
        verifiziertplusIcon.setPadding(false);
        verifiziertplusIcon.setMargin(false);

        IconFactory ver = FontAwesome.Solid.CHECK_CIRCLE;
        Icon vv =  ver.create();
        vv.addClassName("verIcon");

        H1 verifiziert = new H1();
        verifiziert.addClassName("verifiziert");
        verifiziert.add("Verifiziertes Unternehmen");

        verifiziertplusIcon.add(vv,verifiziert);


        H1 neuStellen = new H1("Stellenangebote hinzufügen");
        //hier muss link angepasst werden
        RouterLink linkneuStellen = new RouterLink(JobDetailsView.class);
        linkneuStellen.add(neuStellen);
        linkneuStellen.getStyle().set("text-decoration","none");
        neuStellen.addClassName("neuStellen");



        unternehmenInfoRecht.add(grosseTitleUnternehmen,verifiziertplusIcon,linkneuStellen);



        IconFactory pen = VaadinIcon.PENCIL;
        Icon penIcon =  pen.create();
        penIcon.addClassName("penIcon");



        unternehmenInfo.add(unternehmenIcon,unternehmenInfoRecht,penIcon);


        //unternehmen Beschreibung
        Div unternehmenBeschreibung = new Div();
        unternehmenBeschreibung.addClassName("job");
        H1 unternehmenBeschreibungText = new H1();
        unternehmenBeschreibungText.addClassName("jobBText");
        unternehmenBeschreibungText.add(unternehmen.getUnternehmensBeschreibung(unternehmenPerson));
        unternehmenBeschreibung.add(unternehmenBeschreibungText);





        // Job Section

        H1 jobsTitle = new H1("JobsUebersichtView");
        jobsTitle.addClassName("title");


        Set<Job> jobsUn =  unternehmen.getJobSet(unternehmenPerson);
        for (Job jobSet:jobsUn
             ) {

            jobMeth(jobSet);

        }


        //Job jobTest = new Job();
        //jobMeth(jobTest);

        //jobMeth();






        //kopie von job
/*
        Div job2 = new Div();
        job2.addClassName("job");
        job2.setWidth(getMaxWidth());
        job2.add("hdsgfhasgjhfa hdfjfhjas hjdhjsfdhs fdsjha");
        RouterLink linkJobDetails2 = new RouterLink(JobDetailsView.class);
        linkJobDetails2.add(job2);



        //job2.add(jobBeschreibung,jobIcons,jobBText,jobDatum);



        Div job3 = new Div();
        job3.addClassName("job");
        //job3.setWidth(getMaxWidth());

        job3.add(jobBeschreibung,jobIcons,jobBText,jobDatum);

        //Ende kopie von job


        //Scroller
        Section sec = new Section();
        sec.setMaxWidth("100%");
        sec.setWidth("360px");
        sec.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");


        Scroller scroller = new Scroller();
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);

        HorizontalLayout jobs = new HorizontalLayout(job,job);
        jobs.setPadding(true);
        jobs.getStyle().set("display", "inline-flex");

        scroller.setContent(jobs);
        sec.add(scroller);





 */

        // tag::snippet[]


        sec.setMaxWidth("100%");
        sec.setWidth("100%");
        sec.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");



        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.addClassName("scroller");



        jobs.setPadding(true);
        jobs.getStyle().set("display", "inline-flex");

        scroller.setContent(jobs);
        sec.add(scroller);
        //add(scroller);
        // end::snippet[]



        //Bewertung Section

        H1 bewertungTitle = new H1("Bewertungen");
        bewertungTitle.addClassName("title");


        Div bewertung = new Div();
        bewertung.addClassName("bewertung");

        HorizontalLayout sternePlusDatum = new HorizontalLayout();
        sternePlusDatum.addClassName("sternePlusDatum");
        sternePlusDatum.setMargin(false);
        sternePlusDatum.setPadding(false);
        //.....

        H1 bewertungBeschreibung = new H1();
        bewertungBeschreibung.addClassName("bewertungBeschreibung");
        bewertungBeschreibung.add("Bachelorarbeit Informatik");

        H1 bewertungText = new H1();
        bewertungText.addClassName("bewertungText");
        bewertungText.add("Die Masterarbeit bei EasyQube im Bereich Cybersecurity war eine herausragende Erfahrung! Als Student konnte ich von Anfang an spüren, dass EasyQube eine ...");

        bewertung.add(bewertungBeschreibung,bewertungText);







        add(bildUnternehmen,unternehmenInfo,unternehmenBeschreibung,jobsTitle,sec/*,bewertungTitle,bewertung*/);




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
        titleIconsUnternehmen.add(unternehmen.getUnternehmensName(unternehmenPerson));

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



        RouterLink linkJobDetails = new RouterLink(JobDetailsView.class);
        linkJobDetails.add(job);
        jobs.add(linkJobDetails);
    }


    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

}
