package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.service.JobFilterService;
import de.hbrs.easyjob.service.JobService;
import de.hbrs.easyjob.service.UnternehmenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "student/filter")
@StyleSheet("styles/jobFiltering.css")
public class JobFiltering extends VerticalLayout {

    private MultiSelectComboBox<Ort> standorte;
    private MultiSelectComboBox<Studienfach> Studienfaecher;
    private MultiSelectComboBox<JobKategorie> berufsbezeichnung;
    private Checkbox homeOfficeCheckbox;
    private MultiSelectComboBox<Branche> branchen;

    private final UnternehmenService unternehmenService;
    private final JobFilterService jobFilterService;
    private final JobService jobService;
    @Autowired
    public JobFiltering(UnternehmenService unternehmenService, JobFilterService jobFilterService, JobService jobService) {
        this.unternehmenService = unternehmenService;
        this.jobFilterService = jobFilterService;
        this.jobService = jobService;
        initializeView();
    }

    public void initializeView(){
        VerticalLayout jobfilter = new VerticalLayout();
        addClassName("SFiltern");
        jobfilter.setAlignItems(Alignment.START);
        jobfilter.setAlignSelf(Alignment.CENTER);

        Image imageWidget = new Image("images/filter.png", "bild");
        HorizontalLayout imageLayout = new HorizontalLayout(imageWidget);
        imageLayout.setWidthFull();
        imageLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        imageWidget.addClassName("image-widget");
        add(imageLayout);




        standorte = new MultiSelectComboBox<>("Standorte");
        standorte.setItems(jobService.getAllStandorte());
        standorte.setItemLabelGenerator(Ort::getOrt);

        berufsbezeichnung = new MultiSelectComboBox<>("Berufsbezeichnung");
        berufsbezeichnung.setItems(jobService.getAllJobKategorien());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);

        branchen = new MultiSelectComboBox<>("Branche");
        branchen.setItems(unternehmenService.getAllBranchen());
        branchen.setItemLabelGenerator(Branche::getName);
        Studienfaecher = new MultiSelectComboBox<>("Studienfächer");
        Studienfaecher.setItems(jobService.getAllStudienfacher());
        Studienfaecher.setItemLabelGenerator(Studienfach::getFach);

        MultiSelectComboBox berufsfeld = new MultiSelectComboBox<>("Berufsfeld");
        // Checkbox für Home-Office
        homeOfficeCheckbox = new Checkbox("Ich bin offen für Home-Office");
        homeOfficeCheckbox.addClassName("checkbox");

        // Datum-Auswahl
        DatePicker datePicker = new DatePicker("Eintrittstermin");
        datePicker.addClassName("date-picker");
        datePicker.setLocale(Locale.GERMANY);


        // Buttons für Aktionen
        Button showJobsButton = new Button("Jobs anzeigen");
        showJobsButton.addClassName("button-primary");
        showJobsButton.addClickListener(e -> filterAndNavigate());



        Button resetFilterButton = new Button("Filter Zurücksetzen");
        resetFilterButton.addClassName("button-secondary");
        //ToDo logik für abbrechen

        // Hinzufügen der Buttons zum Layout
        VerticalLayout buttonLayout = new VerticalLayout(showJobsButton, resetFilterButton);
        jobfilter.add(standorte,berufsbezeichnung,branchen,berufsfeld,Studienfaecher,homeOfficeCheckbox,datePicker,buttonLayout);
        add(jobfilter);
    }
    private void filterAndNavigate() {
        // die Filterung
        Set<Ort> selectedOrte = standorte.getValue();
        Set<JobKategorie> selectedKategorien = berufsbezeichnung.getValue();
        boolean homeOffice = homeOfficeCheckbox.getValue();
        Set<Branche> selectedBranches = branchen.getValue();
        Set<Studienfach> selectedStudifach = Studienfaecher.getValue();
        List<Job> gefilterteJobs = jobFilterService.filterJobs(selectedOrte, selectedKategorien, selectedStudifach, homeOffice, selectedBranches); // Ergebnis der Filterung

        // Extrahieren der Job-IDs als List<Integer>
        List<Integer> jobIds = gefilterteJobs.stream()
                .map(Job::getId_Job)
                .collect(Collectors.toList());

        // Speichern der Liste der Job-IDs in der Vaadin-Session
        VaadinSession.getCurrent().setAttribute("filteredJobIds", jobIds);

        // Navigieren zur Jobs-Ansicht
        UI.getCurrent().navigate("student/jobs-finden");
    }


}
