package de.hbrs.easyjob.views.student;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.control.JobProfilController;
import de.hbrs.easyjob.control.UnternehmensProfilController;
import de.hbrs.easyjob.control.UnternehmensperonProfilController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.service.UnternehmenService;
import de.hbrs.easyjob.views.unternehmen.Jobdetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

@Route(value = "UnternehmenProfile" , layout = StudentenLayout.class)
@RouteAlias(value = "u", layout = StudentenLayout.class)
@PageTitle("Unternehmen Profile")
@AnonymousAllowed
public class UnternehmenProfil extends VerticalLayout implements HasUrlParameter<Integer> {
    //Job Methode
    Section sec = new Section();
    Scroller scroller = new Scroller();
    HorizontalLayout jobs = new HorizontalLayout();


    private UnternehmenService unetrnehmenService;



    JobProfilController jobController = new JobProfilController();



    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer unternehmenID) {
        if (unternehmenID != null) {
            Unternehmen unternehmen = unetrnehmenService.findByID(unternehmenID);
            addClassName("all");
            setSizeFull();
            setPadding(false);
            setSpacing(false);
            add(createUnternehmenLayout(unternehmen));
        } else {
            add(new H3("unternehmen-Details konnte nicht geladen werden."));
        }
    }

    private Component createUnternehmenLayout(Unternehmen unternehmen){

        VerticalLayout v = new VerticalLayout();
        v.addClassName("v");
        v.setSizeFull();
        v.setHeight("100%");
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


        VerticalLayout unternehmenInfoRecht = new VerticalLayout();
        unternehmenInfoRecht.addClassName("unternehmenInfoRecht");
        unternehmenInfoRecht.setWidth(getMaxWidth());
        unternehmenInfoRecht.setHeight(getMaxHeight());
        unternehmenInfoRecht.setPadding(false);
        unternehmenInfoRecht.setMargin(false);
        unternehmenInfoRecht.setSpacing(false);

        H1 grosseTitleUnternehmen = new H1();
        grosseTitleUnternehmen.addClassName("grosseTitleUnternehmen");
        grosseTitleUnternehmen.add(unternehmen.getName());


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


        HorizontalLayout locationPlusAnzahl = new HorizontalLayout();
        locationPlusAnzahl.addClassName("locationPlusAnzahl");
        locationPlusAnzahl.setSpacing(false);
        locationPlusAnzahl.setPadding(false);
        locationPlusAnzahl.setMargin(false);

        IconFactory lo = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon loc =  lo.create();
        loc.addClassName("iconsInUnternehmen");

        H1 stadt = new H1();
        stadt.addClassName("stadt");
        stadt.add(unetrnehmenService.getFirstStandort(unternehmen).getOrt());

        H1 anzahlAngebote = new H1();
        anzahlAngebote.addClassName("anzahlAngebote");
        anzahlAngebote.add(unetrnehmenService.anzahlJobs(unternehmen)+" Stellenangebot");

        locationPlusAnzahl.add(loc, stadt, anzahlAngebote);


        unternehmenInfoRecht.add(grosseTitleUnternehmen,verifiziertplusIcon,locationPlusAnzahl);



        IconFactory bel = VaadinIcon.BELL_SLASH_O;
        Icon bellIcon =  bel.create();
        bellIcon.addClassName("bellIcon");



        unternehmenInfo.add(unternehmenIcon,unternehmenInfoRecht,bellIcon);

        //unternehmen Beschreibung
        Div unternehmenBeschreibung = new Div();
        unternehmenBeschreibung.addClassName("job");
        H1 unternehmenBeschreibungText = new H1();
        unternehmenBeschreibungText.addClassName("jobBText");
        unternehmenBeschreibungText.add(unternehmen.getBeschreibung());
        unternehmenBeschreibung.add(unternehmenBeschreibungText);





        // Job Section

        H1 jobsTitle = new H1("Jobs");
        jobsTitle.addClassName("title");


       List<Job> jobsUn= unetrnehmenService.getAllJobs(unternehmen.getId_Unternehmen());
        for (Job jobSet:jobsUn
        ) {

            jobMeth(jobSet, unternehmen);

        }








        //kopie von job
/*
        Div job2 = new Div();
        job2.addClassName("job");
        job2.setWidth(getMaxWidth());
        job2.add("hdsgfhasgjhfa hdfjfhjas hjdhjsfdhs fdsjha");
        RouterLink linkJobDetails2 = new RouterLink(Jobdetails.class);
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







        v.add(bildUnternehmen,unternehmenInfo,unternehmenBeschreibung,jobsTitle,sec/*,bewertungTitle,bewertung*/);
        return v;
    }
    public UnternehmenProfil(@Autowired UnternehmenService unternehmenService){
        this.unetrnehmenService=unternehmenService;
        UI.getCurrent().getPage().addStyleSheet("unternehmenProfil_Student.css");
    }


    private void jobMeth(Job jobSet,Unternehmen unternehmen){

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




        // Veröffentlichungsdatum des Jobs
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



        RouterLink linkJobDetails = new RouterLink(Jobdetails.class);
        linkJobDetails.add(job);
        jobs.add(linkJobDetails);
    }


    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

}
