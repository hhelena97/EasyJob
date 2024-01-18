package de.hbrs.easyjob.views.allgemein;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.components.ZurueckButtonText;
import de.hbrs.easyjob.views.unternehmen.JobDetailsView;

import java.util.Date;
import java.util.List;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

public abstract class AbstractUnternehmenProfilView extends VerticalLayout implements HasUrlParameter<Integer>, BeforeEnterObserver {

    Unternehmen unternehmen;
    HorizontalLayout jobs = new HorizontalLayout();
    boolean istUnternehmensperson;

    //Services
    private final UnternehmenService unternehmenService;

    //Controller
    private final transient SessionController sessionController;
    private final MeldungController meldungController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() ||
                (!sessionController.hasRole("ROLE_STUDENT", "ROLE_UNTERNEHMENSPERSON"))) {
            event.rerouteTo(LoginView.class);
        }
        System.out.println(sessionController.getPerson().getId_Person());
        if (sessionController.hasRole("ROLE_STUDENT")) {
            UI.getCurrent().getPage().addStyleSheet("UnternehmenProfilStudent.css");
            istUnternehmensperson = false;
        } else {
            UI.getCurrent().getPage().addStyleSheet("UnternehmenProfil.css");
            istUnternehmensperson = true;
        }
        add(createUnternehmenLayout(unternehmen));
    }

    public AbstractUnternehmenProfilView(UnternehmenService unternehmenService, SessionController sessionController, MeldungController meldungController) {
        this.unternehmenService = unternehmenService;
        this.sessionController = sessionController;
        this.meldungController = meldungController;
        UI.getCurrent().getPage().addStyleSheet("UnternehmenProfilStudent.css");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer unternehmenID) {

        if (unternehmenID != null) {
            unternehmen = unternehmenService.findByID(unternehmenID);
        } else {
            unternehmen = ((Unternehmensperson) sessionController.getPerson()).getUnternehmen();
        }
        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }

    private Component createUnternehmenLayout(Unternehmen unternehmen) {

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("v");
        mainLayout.setSizeFull();
        mainLayout.setHeight("100%");
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.getStyle().set("gap", "var(--lumo-space-s)");
        mainLayout.setAlignItems(START);
        setJustifyContentMode(JustifyContentMode.START);

        //Zurück
        ZurueckButtonText zurueck = new ZurueckButtonText();
        zurueck.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());

        //Profilbanner
        String bannerSrc = unternehmen.getBild() != null ? unternehmen.getBild() : "images/default-banner.jpg";
        Image banner = new Image(bannerSrc, "Banner");
        Div bildUnternehmen = new Div(banner);
        bildUnternehmen.addClassName("bildUnternehmen");
        bildUnternehmen.setWidth(getMaxWidth());

        HorizontalLayout menu = new HorizontalLayout(zurueck);
        menu.addClassName("unternehmenInfoAndMenu");

        // -------------------------------------------------------------------------------------------------------------
        if (!istUnternehmensperson) {
            // Code für Melde-Funktion:

            VerticalLayout dotsLayout = new VerticalLayout();
            dotsLayout.setPadding(true);
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
                meldungController.saveMeldung(meldung, unternehmen);
                Notification.show("Gemeldet", 3000, Notification.Position.TOP_STRETCH);
            });

            melden.getElement().getStyle().set("color", "red");

            dotsLayout.add(dots);
            menu.add(dotsLayout);

        } else {
            Icon pen = new Icon(VaadinIcon.PENCIL);
            pen.addClassName("iconsProf");
            VerticalLayout penLayout = new VerticalLayout(pen);
            penLayout.setWidth("fit-content");
            penLayout.addClickListener(e -> UI.getCurrent().navigate("unternehmen/unternehmensprofilbearbeiten"));
            menu.add(penLayout);
        }
        // -------------------------------------------------------------------------------------------------------------

        VerticalLayout top = new VerticalLayout(bildUnternehmen, menu);
        top.setSpacing(false);
        top.setPadding(false);


        // -------------------------------------------------------------------------------------------------------------
        //Section Unternehmen Info
        HorizontalLayout unternehmenInfo = new HorizontalLayout();
        unternehmenInfo.setSizeFull();
        unternehmenInfo.setHeight("14%");
        unternehmenInfo.setPadding(false);
        unternehmenInfo.setMargin(false);
        unternehmenInfo.setSpacing(false);
        unternehmenInfo.setAlignItems(Alignment.CENTER);

        //Logo
        String logoSrc = unternehmen.getLogo() != null ? unternehmen.getLogo() : "images/blank-logo.jpeg";
        Image logo = new Image(logoSrc, "Logo");
        Div unternehmenLogo = new Div(logo);
        unternehmenLogo.addClassName("unternehmenLogo");

        //Info
        VerticalLayout unternehmenInfoLayout = new VerticalLayout();
        unternehmenInfoLayout.addClassName("unternehmenInfoLayout");
        unternehmenInfoLayout.setPadding(false);
        unternehmenInfoLayout.setMargin(false);
        unternehmenInfoLayout.setSpacing(true);

        //Name
        H1 grosseTitleUnternehmen = new H1();
        grosseTitleUnternehmen.addClassName("grosseTitleUnternehmen");
        grosseTitleUnternehmen.add(unternehmen.getName());

        //Verifiziert
        IconFactory iconFactory = FontAwesome.Solid.CHECK_CIRCLE;
        Icon verIcon = iconFactory.create();
        verIcon.addClassName("verIcon");
        Span verifiziert = new Span(verIcon, new Span("Verifiziertes Unternehmen"));
        verifiziert.getElement().getThemeList().add("badge");
        verifiziert.addClassName("verifiziert");

        //Standort und Anzahl Jobs
        HorizontalLayout locationPlusAnzahl = new HorizontalLayout();
        locationPlusAnzahl.addClassName("locationPlusAnzahl");
        locationPlusAnzahl.setSpacing(false);
        locationPlusAnzahl.setPadding(false);
        locationPlusAnzahl.setMargin(false);

        IconFactory iconFactory1 = FontAwesome.Solid.MAP_MARKER_ALT;
        Icon loc = iconFactory1.create();
        loc.addClassName("iconsInUnternehmen");

        H1 stadt = new H1();
        stadt.addClassName("stadt");
        stadt.add(unternehmenService.getFirstStandort(unternehmen).getOrt());


        if (!istUnternehmensperson) {
            H1 anzahlAngebote = new H1();
            anzahlAngebote.addClassName("anzahlAngebote");
            int angebote = unternehmenService.anzahlJobs(unternehmen);
            anzahlAngebote.add((angebote == 0 || angebote > 1) ? (angebote + " Stellenangebote") : (angebote + " Stellenangebot"));

            locationPlusAnzahl.add(loc, stadt, anzahlAngebote);
        } else {
            H1 neu = new H1("Stellenangebote hinzufügen");
            neu.addClassName("neuStellen");
            neu.addClickListener(e-> UI.getCurrent().navigate("unternehmen/job/create"));
            locationPlusAnzahl.add(neu);
        }
        // -------------------------------------------------------------------------------------------------------------

        unternehmenInfoLayout.add(grosseTitleUnternehmen, verifiziert, locationPlusAnzahl);
        unternehmenInfo.add(unternehmenLogo, unternehmenInfoLayout);

        //unternehmen Beschreibung
        H1 unternehmenBeschreibungText = new H1();
        unternehmenBeschreibungText.add(unternehmen.getBeschreibung());
        unternehmenBeschreibungText.addClassName("jobBText");
        HorizontalLayout unternehmenBeschreibung = new HorizontalLayout(unternehmenBeschreibungText);
        unternehmenBeschreibung.addClassName("unternehmenBeschreibung");

        // Job Section
        H1 jobsTitle = new H1("Stellenangebote:");
        jobsTitle.addClassName("title");

        Section sec = new Section();
        sec.setMaxWidth("100%");
        sec.setWidth("100%");

        Scroller scroller = new Scroller();
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.addClassName("scroller");

        List<Job> jobsUn = unternehmenService.getAllJobs(unternehmen.getId_Unternehmen());
        jobsUn.forEach(job -> {
            //RouterLink jobLink = new RouterLink(AbstractJobDetails.class, job.getId_Job());
            if (!job.getGesperrt() && job.getAktiv()) {

                RouterLink jobLink;
                if(istUnternehmensperson){
                    //Klasse aus package unternehmen
                    jobLink = new RouterLink(JobDetailsView.class, job.getId_Job());
                }
                else {
                    //Klasse aus package student
                    jobLink = new RouterLink(de.hbrs.easyjob.views.student.JobDetailsView.class, job.getId_Job());
                }
                jobLink.add(createStellenanzeigenVorschau(job));
                jobs.add(jobLink);
            }
        });

        jobs.setPadding(true);
        jobs.getStyle().set("display", "inline-flex");

        scroller.setContent(jobs);
        sec.add(jobsTitle, scroller);

        mainLayout.add(top, unternehmenInfo, unternehmenBeschreibung, sec);
        return mainLayout;
    }


    private VerticalLayout createStellenanzeigenVorschau(Job job) {
        String STELLENANZEIGE_TAG = "stellenanzeige-tag";
        VerticalLayout stellenanzeigeFrame = new VerticalLayout();
        stellenanzeigeFrame.addClassName("stellenanzeige");

        H1 titel = new H1(job.getTitel());
        titel.addClassName("stellenanzeige-titel");
        stellenanzeigeFrame.add(titel);

        // Tags
        HorizontalLayout tags = new HorizontalLayout();
        tags.addClassName("stellenanzeige-tags");

        // Tag: Unternehmen
        Div tagUnternehmen = new Div();
        tagUnternehmen.addClassName(STELLENANZEIGE_TAG);
        Span unternehmensName = new Span(job.getUnternehmen().getName());
        tagUnternehmen.add(FontAwesome.Solid.BRIEFCASE.create(), unternehmensName);

        // Tag: Ort
        Div tagOrt = new Div();
        tagOrt.addClassName(STELLENANZEIGE_TAG);
        Span ort = new Span(job.getOrt().getOrt());
        tagOrt.add(FontAwesome.Solid.MAP_MARKER_ALT.create(), ort);

        // Tag: Kategorie
        Div tagKategorie = new Div();
        tagKategorie.addClassName(STELLENANZEIGE_TAG);
        Span kategorie = new Span(job.getJobKategorie().getKategorie());
        tagKategorie.add(FontAwesome.Solid.GRADUATION_CAP.create(), kategorie);

        tags.add(tagUnternehmen, tagOrt, tagKategorie);
        stellenanzeigeFrame.add(tags);

        // Jobbeschreibung
        Paragraph beschreibung = new Paragraph(shortenDescription(job.getFreitext()));
        beschreibung.addClassName("stellenanzeige-beschreibung");
        stellenanzeigeFrame.add(beschreibung);

        // Online seit
        Span onlineSeit = new Span(getRelativeTime(job.getErstellt_am()));
        onlineSeit.addClassName("stellenanzeige-online-seit");
        stellenanzeigeFrame.add(onlineSeit);

        return stellenanzeigeFrame;
    }

    private String shortenDescription(String description) {
        if (description.length() > 130) {
            String subtstr = description.substring(0, 130);
            int lastSpace = subtstr.lastIndexOf(" ");
            return subtstr.substring(0, lastSpace) + "...";
        } else {
            return description;
        }
    }

    private String getRelativeTime(Date date) {
        long difference = new Date().getTime() - date.getTime();
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Gerade eben";
        } else if (minutes < 60) {
            return "vor " + minutes + " Minuten";
        } else if (hours < 24) {
            return "vor " + hours + " Stunden";
        } else {
            return "vor " + days + " Tagen";
        }
    }


}
