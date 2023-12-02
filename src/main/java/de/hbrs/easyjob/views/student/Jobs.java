package de.hbrs.easyjob.views.student;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.service.JobFilterService;
import de.hbrs.easyjob.service.JobService;
import de.hbrs.easyjob.service.JobSucheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Route(value = "student/jobs-finden", layout = StudentenLayout.class)
@StyleSheet("JobList.css")
public class Jobs extends VerticalLayout {

    private final JobService jobService;
    private final JobSucheService jobSucheService;
    private final JobFilterService jobFilterService;
    private VerticalLayout jobListLayout;


    @Autowired
    public Jobs(JobService jobService, JobSucheService jobSucheService, JobFilterService jobFilterService) {
        this.jobService = jobService;
        this.jobSucheService = jobSucheService;
        this.jobFilterService = jobFilterService;
        initializeView();
    }

    private void initializeView() {
        addClassName("jobs-view");

        //setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        // Suchfeld mit Enter-Aktivierung und Options-Icon
        TextField searchField = new TextField();
        searchField.setPlaceholder("Job suchen");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> searchJobs(searchField.getValue()));

        // Options-Icon als Suffix zum Suchfeld hinzufügen
        Button optionButton = new Button(new Icon(VaadinIcon.OPTIONS));
        optionButton.addClickListener(e -> UI.getCurrent().navigate("student/filter"));
        optionButton.addClassName("options-button");


        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, optionButton);
        searchLayout.addClassName("search-layout");

        // Liste für Jobanzeigen
        jobListLayout = new VerticalLayout();
        jobListLayout.setWidthFull();
        jobListLayout.setAlignItems(Alignment.STRETCH);
        jobListLayout.setClassName("job-list-layout");

        // Jobs laden und anzeigen
        loadJobs();

        // Reihenfolge der Komponenten festlegen
        add(searchLayout, jobListLayout); // Suchleiste und Jobliste hinzufügen
    }

    private void loadJobs() {
        Object sessionAttribute = VaadinSession.getCurrent().getAttribute("filteredJobIds");
        if (sessionAttribute instanceof List) {
            List<Integer> jobIds = (List<Integer>) sessionAttribute;
            if (!jobIds.isEmpty()) {
                List<Job> jobs = jobService.getJobsByIds(jobIds);
                jobs.forEach(this::addJobComponentToLayout);

                // Entfernen der IDs aus der Sitzung, um zukünftige Konflikte zu vermeiden
                VaadinSession.getCurrent().setAttribute("filteredJobIds", null);
            }
        } else {
            List<Job> jobs = jobService.getAllJobs();
            jobs.forEach(this::addJobComponentToLayout);
        }
    }

    private void searchJobs(String searchText) {
        jobListLayout.removeAll();
        List<Job> jobs;
        if (searchText == null || searchText.isEmpty()) {
            jobs = jobService.getAllJobs();
        } else {
            jobs = jobSucheService.isVollTextSuche(searchText) ?
                    jobSucheService.vollTextSuche(searchText) :
                    jobSucheService.teilZeichenSuche(searchText);
        }
        jobs.forEach(this::addJobComponentToLayout);
    }

    private void addJobComponentToLayout(Job job) {
        VerticalLayout jobCardLayout = new VerticalLayout();
        jobCardLayout.addClassName("job-card");
        jobCardLayout.setPadding(false);
        jobCardLayout.setSpacing(false);
        jobCardLayout.setAlignItems(Alignment.STRETCH);
        jobCardLayout.setWidth("100%");


        // Jobtitel mit Begrenzung der Länge und RouterLink für die Details
        RouterLink linkJobTitle = new RouterLink("", JobdetailsView.class, job.getId_Job());
        linkJobTitle.add(new H1(job.getTitel()));
        linkJobTitle.addClassName("job-title");

        // Jobdetails wie Unternehmen und Ort und Homeoffice
        HorizontalLayout companyAndLocation = new HorizontalLayout();
        companyAndLocation.setSpacing(false);
        companyAndLocation.setAlignItems(Alignment.CENTER);
        companyAndLocation.addClassName("company-location");

        IconFactory i = FontAwesome.Solid.BRIEFCASE;
        Icon ii =  i.create();
        ii.addClassName("iconsInJobIcons");

        RouterLink titleIconsUnternehmen = new RouterLink("", UnternehmenProfil.class, job.getUnternehmen().getId_Unternehmen());
        titleIconsUnternehmen.addClassName("titleIcons");
        titleIconsUnternehmen.add(job.getUnternehmen().getName());

        Icon ll =  new Icon(VaadinIcon.MAP_MARKER);
        ll.addClassName("iconsInJobIcons");
        H1 titleIconsLocation = new H1();
        titleIconsLocation.addClassName("titleIcons");
        titleIconsLocation.add(job.getOrt().getOrt());

        if (job.isHomeOffice()){
            IconFactory g = FontAwesome.Solid.HOME;
            Icon gg =  new Icon(VaadinIcon.HOME);
            gg.addClassName("iconsInJobIcons");
            H1 homeOffice = new H1();
            homeOffice.addClassName("titleIcons");
            homeOffice.add("Homeoffice");
            companyAndLocation.add(ii,titleIconsUnternehmen,ll,titleIconsLocation,gg,homeOffice);
        }else {
            companyAndLocation.add(ii,titleIconsUnternehmen,ll,titleIconsLocation);
        }
        // Jobbeschreibung mit "...", wenn sie zu lang ist
        Paragraph jobDescription = new Paragraph(limitText(job.getFreitext(), 230));
        jobDescription.addClassName("job-description");

        // Veröffentlichungsdatum des Jobs
        LocalDate jobDate = job.getErstellt_am().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(jobDate, LocalDate.now());
        String daysAgoText = daysBetween == 0 ? "Heute" : daysBetween + (daysBetween == 1 ? " Tag" : " Tage");

        Span postedTime = new Span("Vor " + daysAgoText);
        postedTime.addClassName("posted-time");


        jobCardLayout.add(linkJobTitle, companyAndLocation, jobDescription, postedTime);


        jobListLayout.add(jobCardLayout);

    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}

