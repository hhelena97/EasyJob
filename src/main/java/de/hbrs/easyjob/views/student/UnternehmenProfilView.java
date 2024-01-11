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
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.unternehmen.JobDetailsView;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

@Route(value = "UnternehmenProfile", layout = StudentLayout.class)
@RouteAlias(value = "u", layout = StudentLayout.class)
@PageTitle("Unternehmen Profile")
@RolesAllowed("ROLE_STUDENT")
public class UnternehmenProfilView extends VerticalLayout implements HasUrlParameter<Integer>, BeforeEnterObserver {
    // Job Methode
    private final Section sec = new Section();
    private final Scroller scroller = new Scroller();
    private final HorizontalLayout jobs = new HorizontalLayout();

    // Service
    private final transient UnternehmenService unternehmenService;

    // Controller
    private final transient SessionController sessionController;

    // Konstanten
    private static final String ICONS_IN_JOB_ICONS = "iconsInJobIcons";
    private static final String TITLE_ICONS = "titleIcons";

    public UnternehmenProfilView(UnternehmenService unternehmenService, SessionController sessionController) {
        this.unternehmenService = unternehmenService;
        this.sessionController = sessionController;
        UI.getCurrent().getPage().addStyleSheet("unternehmenProfil_Student.css");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_STUDENT")) {
            event.rerouteTo(LoginView.class);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer unternehmenID) {
        if (unternehmenID != null) {
            Unternehmen unternehmen = unternehmenService.findByID(unternehmenID);
            addClassName("all");
            setSizeFull();
            setPadding(false);
            setSpacing(false);
            add(createUnternehmenLayout(unternehmen));
        } else {
            add(new H3("unternehmen-Details konnte nicht geladen werden."));
        }
    }

    private Component createUnternehmenLayout(Unternehmen unternehmen) {

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
        Icon vv = ver.create();
        vv.addClassName("verIcon");

        H1 verifiziert = new H1();
        verifiziert.addClassName("verifiziert");
        verifiziert.add("Verifiziertes Unternehmen");

        verifiziertplusIcon.add(vv, verifiziert);

        HorizontalLayout locationPlusAnzahl = new HorizontalLayout();
        locationPlusAnzahl.addClassName("locationPlusAnzahl");
        locationPlusAnzahl.setSpacing(false);
        locationPlusAnzahl.setPadding(false);
        locationPlusAnzahl.setMargin(false);

        IconFactory lo = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon loc = lo.create();
        loc.addClassName("iconsInUnternehmen");

        H1 stadt = new H1();
        stadt.addClassName("stadt");
        stadt.add(unternehmenService.getFirstStandort(unternehmen).getOrt());

        H1 anzahlAngebote = new H1();
        anzahlAngebote.addClassName("anzahlAngebote");
        anzahlAngebote.add(unternehmenService.anzahlJobs(unternehmen) + " Stellenangebot");

        locationPlusAnzahl.add(loc, stadt, anzahlAngebote);

        unternehmenInfoRecht.add(grosseTitleUnternehmen, verifiziertplusIcon, locationPlusAnzahl);

        IconFactory bel = VaadinIcon.BELL_SLASH_O;
        Icon bellIcon = bel.create();
        bellIcon.addClassName("bellIcon");

        unternehmenInfo.add(unternehmenIcon, unternehmenInfoRecht, bellIcon);

        // Unternehmen Beschreibung
        Div unternehmenBeschreibung = new Div();
        unternehmenBeschreibung.addClassName("job");
        H1 unternehmenBeschreibungText = new H1();
        unternehmenBeschreibungText.addClassName("jobBText");
        unternehmenBeschreibungText.add(unternehmen.getBeschreibung());
        unternehmenBeschreibung.add(unternehmenBeschreibungText);

        // Job Section
        H1 jobsTitle = new H1("JobsUebersichtView");
        jobsTitle.addClassName("title");

        List<Job> jobsUn = unternehmenService.getAllJobs(unternehmen.getId_Unternehmen());
        for (Job jobSet : jobsUn) {

            jobMeth(jobSet, unternehmen);

        }

        sec.setMaxWidth("100%");
        sec.setWidth("100%");
        sec.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");

        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.addClassName("scroller");

        jobs.setPadding(true);
        jobs.getStyle().set("display", "inline-flex");

        scroller.setContent(jobs);
        sec.add(scroller);

        // Bewertung Section
        H1 bewertungTitle = new H1("Bewertungen");
        bewertungTitle.addClassName("title");

        Div bewertung = new Div();
        bewertung.addClassName("bewertung");

        HorizontalLayout sternePlusDatum = new HorizontalLayout();
        sternePlusDatum.addClassName("sternePlusDatum");
        sternePlusDatum.setMargin(false);
        sternePlusDatum.setPadding(false);

        H1 bewertungBeschreibung = new H1();
        bewertungBeschreibung.addClassName("bewertungBeschreibung");
        bewertungBeschreibung.add("Bachelorarbeit Informatik");

        H1 bewertungText = new H1();
        bewertungText.addClassName("bewertungText");
        bewertungText.add("Die Masterarbeit bei EasyQube im Bereich Cybersecurity war eine herausragende Erfahrung! Als Student konnte ich von Anfang an spüren, dass EasyQube eine ...");

        bewertung.add(bewertungBeschreibung, bewertungText);

        v.add(bildUnternehmen, unternehmenInfo, unternehmenBeschreibung, jobsTitle, sec/*,bewertungTitle,bewertung*/);
        return v;
    }

    private void jobMeth(Job jobSet, Unternehmen unternehmen) {
        Div job = new Div();
        job.addClassName("job");
        job.setWidth(getMaxWidth());

        H1 jobBeschreibung = new H1();
        jobBeschreibung.addClassName("jobBeschreibung");
        jobBeschreibung.add(jobSet.getTitel());

        HorizontalLayout jobIcons = new HorizontalLayout();
        jobIcons.setSpacing(false);
        jobIcons.setAlignItems(Alignment.CENTER);

        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon ii = i.create();
        ii.addClassName(ICONS_IN_JOB_ICONS);

        H1 titleIconsUnternehmen = new H1();
        titleIconsUnternehmen.addClassName(TITLE_ICONS);
        titleIconsUnternehmen.add(unternehmen.getName());

        IconFactory l = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon ll = l.create();
        ll.addClassName(ICONS_IN_JOB_ICONS);
        H1 titleIconsLocation = new H1();
        titleIconsLocation.addClassName(TITLE_ICONS);
        titleIconsLocation.add(jobSet.getOrt().getOrt());

        IconFactory g = FontAwesome.Solid.GRADUATION_CAP;
        Icon gg = g.create();
        gg.addClassName(ICONS_IN_JOB_ICONS);
        H1 titleIconsJob = new H1();
        titleIconsJob.addClassName(TITLE_ICONS);
        titleIconsJob.add(jobSet.getJobKategorie().getKategorie());

        jobIcons.add(ii, titleIconsUnternehmen, ll, titleIconsLocation, gg, titleIconsJob);

        Paragraph jobDescription = new Paragraph(limitText(jobSet.getFreitext()));

        H1 jobBText = new H1();
        jobBText.addClassName("jobBText");

        jobBText.add(jobDescription);

        // Veröffentlichungsdatum der JobsUebersichtView
        LocalDate jobDate = jobSet.getErstellt_am().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(jobDate, LocalDate.now());
        String tagTage = daysBetween == 1 ? " Tag" : " Tage";
        String daysAgoText = daysBetween == 0 ? "Heute" : daysBetween + tagTage;

        Span postedTime = new Span("Vor " + daysAgoText);

        H1 jobDatum = new H1();
        jobDatum.addClassName("jobDatum");
        jobDatum.add(postedTime);

        job.add(jobBeschreibung, jobIcons, jobBText, jobDatum);

        RouterLink linkJobDetails = new RouterLink(JobDetailsView.class);
        linkJobDetails.add(job);
        jobs.add(linkJobDetails);
    }

    /**
     * kürzt den eingegebenen Text
     * @param text - zu kürzender Text
     * @return gekürzten Text
     */
    private String limitText(String text) {
        return text.length() > 230 ? text.substring(0, 227) + "..." : text;
    }
}
