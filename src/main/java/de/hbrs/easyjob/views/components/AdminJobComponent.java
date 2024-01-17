package de.hbrs.easyjob.views.components;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.entities.Job;

@StyleSheet("DialogLayout.css")
public class AdminJobComponent extends VerticalLayout {

    private final Job job;
    private final ProfilSperrenController profilSperrenController;


    public AdminJobComponent(Job job, ProfilSperrenController sperrenController){
        this.job = job;
        this.profilSperrenController = sperrenController;
        initializeComponent();
    }

    private void initializeComponent(){

        // Unternehmen
        H3 company = new H3(job.getUnternehmen().getName());
        company.addClassName("job-details-company");

        // Titel
        H2 title = new H2(job.getTitel());
        title.addClassName("job-details-title");

        // Tags
        HorizontalLayout tagsContainer = new HorizontalLayout();
        tagsContainer.addClassName("job-details-tags");

        Div[] tags = new Div[3];

        for (int i = 0; i < tags.length; i++) {
            tags[i] = new Div();
            tags[i].addClassName("job-details-tag");
        }

        // Tag: Datum
        tags[0].add(FontAwesome.Solid.CALENDAR_DAYS.create(), new Span("Datum"));
        // tags[0].add(FontAwesome.Solid.CALENDAR_DAYS.create(), new Span(job.getEintritt().toString()));

        // Tag: Ort
        tags[1].add(FontAwesome.Solid.MAP_MARKER_ALT.create(), new Span(job.getOrt().getOrt()));

        // Tag: Typ
        tags[2].add(FontAwesome.Solid.GRADUATION_CAP.create(), new Span(job.getJobKategorie().getKategorie()));

        // Beschreibung
        Paragraph descriptionText = new Paragraph(job.getFreitext());
        descriptionText.addClassName("job-details-description");

        // Sperrbutton
        Dialog d = new Dialog();

        String sperrbutton;
        if (job.getGesperrt()) {
            sperrbutton = "Job entsperren";
            jobEntsperrenDialog(job, d);
        } else {
            sperrbutton = "Job sperren";
            jobSperrenDialog(job, d);
        }

        Button btnSperren = new Button(sperrbutton);
        btnSperren.addClassName("btnSperren");
        btnSperren.addClickListener(e -> d.open());

        add(company, title, tagsContainer, descriptionText, btnSperren);
    }

    private void jobSperrenDialog(Job job, Dialog d) {
        d.add(new Paragraph("Möchten Sie diesen Job wirklich sperren?"));

        Button btnBestaetigen = new Button("Job sperren");
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            if (profilSperrenController.jobSperren(job)) {
                d.close();
            } else {
                Notification.show("Der Job konnte nicht gesperrt werden");
            }
        });
        Button btnAbbruch2 = new Button("Abbrechen");
        btnAbbruch2.addClassName("close-admin");
        btnAbbruch2.addClickListener(e -> d.close());
        d.getFooter().add(btnBestaetigen, btnAbbruch2);
    }

    private void jobEntsperrenDialog(Job job, Dialog d){
        d.add(new Paragraph("Möchten Sie diesen Job wirklich entsperren?"));

        Button btnBestaetigen = new Button("Job entsperren");
        btnBestaetigen.addClassName("confirm");
        btnBestaetigen.addClickListener(e -> {
            if (profilSperrenController.jobEntsperren(job)){
                d.close();
            } else {
                Notification.show("Der Job konnte nicht entsperrt werden");
            }
        });
        Button btnAbbruch2 = new Button("Abbrechen");
        btnAbbruch2.addClassName("close-admin");
        btnAbbruch2.addClickListener(e -> d.close());
        d.getFooter().add(btnBestaetigen, btnAbbruch2);
    }
}
