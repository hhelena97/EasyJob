package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
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

        Set<Ort> savedOrte = (Set<Ort>) VaadinSession.getCurrent().getAttribute("selectedOrte");
        Set<JobKategorie> savedKategorien = (Set<JobKategorie>) VaadinSession.getCurrent().getAttribute("selectedKategorien");
        Set<Studienfach> savedStudifach = (Set<Studienfach>) VaadinSession.getCurrent().getAttribute("selectedStudifach");
        Boolean savedHomeOffice = (Boolean) VaadinSession.getCurrent().getAttribute("homeOffice");
        Set<Branche> savedBranches = (Set<Branche>) VaadinSession.getCurrent().getAttribute("selectedBranches");

        if (savedOrte != null) standorte.setValue(savedOrte);
        if (savedKategorien != null) berufsbezeichnung.setValue(savedKategorien);
        if (savedStudifach != null) Studienfaecher.setValue(savedStudifach);
        if (savedHomeOffice != null) homeOfficeCheckbox.setValue(savedHomeOffice);
        if (savedBranches != null) branchen.setValue(savedBranches);

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
        resetFilterButton.addClickListener(e -> resetFiltersAndLoadJobs());

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
        List<Job> searchedJobs = (List<Job>) VaadinSession.getCurrent().getAttribute("searchedJobs");
        List<Job> gefilterteJobs = jobFilterService.filterJobs(searchedJobs,selectedOrte, selectedKategorien, selectedStudifach, homeOffice, selectedBranches);
        // Extrahieren der Job-IDs als List<Integer>
        List<Integer> jobIds = gefilterteJobs.stream()
                .map(Job::getId_Job)
                .collect(Collectors.toList());

        VaadinSession.getCurrent().setAttribute("selectedOrte", selectedOrte);
        VaadinSession.getCurrent().setAttribute("selectedKategorien", selectedKategorien);
        VaadinSession.getCurrent().setAttribute("selectedBranches", selectedBranches);
        VaadinSession.getCurrent().setAttribute("selectedStudifach", selectedStudifach);
        VaadinSession.getCurrent().setAttribute("homeOffice", homeOffice);
        // Speichern der Liste der Job-IDs in der Vaadin-Session
        VaadinSession.getCurrent().setAttribute("filteredJobIds", jobIds);

        // Navigieren zur Jobs-Ansicht
        UI.getCurrent().navigate("student/jobs-finden");
    }

    private void resetFiltersAndLoadJobs() {
        // Setzen Sie alle Filterkomponenten zurück
        standorte.clear();
        berufsbezeichnung.clear();
        Studienfaecher.clear();
        homeOfficeCheckbox.setValue(false);
        branchen.clear();

        // Löschen der gespeicherten Filterwerte in der Session
        VaadinSession.getCurrent().setAttribute("selectedOrte", null);
        VaadinSession.getCurrent().setAttribute("selectedKategorien", null);
        VaadinSession.getCurrent().setAttribute("selectedStudifach", null);
        VaadinSession.getCurrent().setAttribute("homeOffice", null);
        VaadinSession.getCurrent().setAttribute("selectedBranches", null);

        VaadinSession.getCurrent().setAttribute("filteredJobIds", null);

        UI.getCurrent().navigate("student/jobs-finden");
    }

}
