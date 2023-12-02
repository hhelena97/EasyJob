package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.StellenanzeigeControl;
import de.hbrs.easyjob.entities.Job;

import java.util.Date;
import java.util.List;

/**
 * View für das Profil des Unternehmens
 */
@Route(value = "unternehmen", layout = UnternehmenLayout.class)
@StyleSheet("Variables.css")
@StyleSheet("ProfilView.css")
public class ProfilView extends VerticalLayout implements HasUrlParameter<Integer> {
    private final StellenanzeigeControl stellenanzeigeControl;

    @Override
    public void setParameter(BeforeEvent event, Integer parameter) {
        // Container
        VerticalLayout frame = new VerticalLayout();
        frame.setClassName("container");

        // Jobs
        VerticalLayout jobsFrame = new VerticalLayout();
        jobsFrame.addClassName("jobs");

        Span jobsSection = new Span("Jobs");
        jobsSection.addClassName("section-title");
        jobsFrame.add(jobsSection);

        Scroller stellenanzeigenScroller = new Scroller();
        HorizontalLayout stellenanzeigen = new HorizontalLayout();
        stellenanzeigenScroller.addClassName("stellenanzeigen-scroller");

        List<Job> jobs = stellenanzeigeControl.stellenanzeigenEinesUnternehmens(parameter);
        for (Job job : jobs) {
            stellenanzeigen.add(createStellenanzeigenVorschau(job));
        }
        stellenanzeigenScroller.setContent(stellenanzeigen);

        jobsFrame.add(stellenanzeigenScroller);

        frame.add(jobsFrame);
        add(frame);
    }
    public ProfilView(StellenanzeigeControl stellenanzeigeControl) {
        this.stellenanzeigeControl = stellenanzeigeControl;
    }

    private Component createStellenanzeigenVorschau(Job job) {
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
        tagUnternehmen.addClassName("stellenanzeige-tag");
        Span unternehmensName = new Span(job.getUnternehmen().getName());
        tagUnternehmen.add(FontAwesome.Solid.BRIEFCASE.create(), unternehmensName);

        // Tag: Ort
        Div tagOrt = new Div();
        tagOrt.addClassName("stellenanzeige-tag");
        Span ort = new Span(job.getOrt().getOrt());
        tagOrt.add(FontAwesome.Solid.MAP_MARKER_ALT.create(), ort);

        // Tag: Kategorie
        Div tagKategorie = new Div();
        tagKategorie.addClassName("stellenanzeige-tag");
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
