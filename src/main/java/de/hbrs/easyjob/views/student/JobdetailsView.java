package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Optional;

@Route(value = "job-details",layout = StudentenLayout.class)
@PageTitle("Job Details")

public class JobdetailsView extends VerticalLayout implements HasUrlParameter<Integer> {
    private JobService jobService;

    public JobdetailsView(@Autowired JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer jobID) {
        if (jobID != null) {
            Job job = jobService.getJobById(jobID);
            add(createJobDetailLayout(job));
        } else {
            add(new H3("Job-Details konnten nicht geladen werden."));
        }
    }

    private Component createJobDetailLayout(Job job) {
        // Layout für das Jobfoto
        Image jobImage = new Image("path/to/job/image.jpg", "Job Image");
        jobImage.setWidth(getMaxWidth());
        jobImage.setHeight("auto");

        // Titel des Jobs
        H1 jobTitle = new H1(job.getTitel());
        jobTitle.addClassName("job-title");

        // Unternehmen und Standort
        H2 companyAndLocation = new H2(job.getUnternehmen().getName() + " - " + job.getOrt().getOrt());
        companyAndLocation.addClassName("company-location");

        // Freitext des Jobs
        Paragraph jobDescription = new Paragraph(job.getFreitext());
        jobDescription.addClassName("job-description");

        // Erstellungsdatum
        Paragraph jobDate = new Paragraph("Veröffentlicht am: " + formatDate(job.getErstellt_am()));
        jobDate.addClassName("job-date");

        // Weitere Details wie Anforderungen und was geboten wird, können als Paragraphen hinzugefügt werden

        // Button zur Bewerbung
        Button applyButton = new Button("Jetzt bewerben", e -> { /* Logik für Bewerbung */ });
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Button, um mehr Jobs zu entdecken
        Button discoverMoreButton = new Button("mehr Jobs entdecken", e -> { /* Logik für weitere Jobs */ });

        // Layout zusammenbauen
        VerticalLayout layout = new VerticalLayout(jobImage, jobTitle, companyAndLocation, jobDescription, jobDate, applyButton, discoverMoreButton);
        layout.addClassName("job-detail-layout");
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);

        return layout;
    }

    private String formatDate(Date date) {
        // Formatieren Sie das Datum nach Ihren Wünschen
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }
}
