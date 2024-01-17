package de.hbrs.easyjob.views.allgemein;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobRepository;

@StyleSheet("JobDetails.css")
public abstract class AbstractJobDetails extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<Integer> {
    // Repositories
    protected final transient JobRepository jobRepository;

    // Controllers
    protected final transient SessionController sessionController;

    // Components
    public final VerticalLayout frame = new VerticalLayout();
    public final HorizontalLayout buttons = new HorizontalLayout();
    public final Div descriptionContainer = new Div();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, Integer parameter) {
        jobRepository.findById(parameter).ifPresentOrElse(job -> {
            if (Boolean.TRUE.equals(job.getAktiv())) {
                displayJob(job);
            } else if (sessionController.hasRole("ROLE_UNTERNEHMENSPERSON") &&
                    ((Unternehmensperson) sessionController.getPerson())
                            .getUnternehmen().getId_Unternehmen().equals(job.getUnternehmen().getId_Unternehmen())
            ) {
                displayJob(job);
            } else {
                add("Job nicht gefunden");
            }
        }, () -> add("Job nicht gefunden"));
    }

    public void displayJob(Job job) {
        Image image = new Image("https://picsum.photos/500/500", "Vorschaubild");
        //image = new Image(job.getBild(), "Vorschaubild");
        image.addClassName("job-details-image");

        // Buttons Container
        buttons.addClassName("job-details-buttons");

        // Zurück
        Button back = new Button("Zurück", FontAwesome.Solid.CHEVRON_LEFT.create());
        back.addClassName("job-details-back-button");

        buttons.add(back);

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
        Scroller description = new Scroller();
        Paragraph descriptionText = new Paragraph(job.getFreitext());

        descriptionText.addClassName("job-details-description");

        descriptionContainer.add(descriptionText);
        description.setContent(descriptionContainer);

        frame.add(image, buttons, company, title, tagsContainer, description);
    }

    protected AbstractJobDetails(
            SessionController sessionController,
            JobRepository jobRepository
    ) {
        this.jobRepository = jobRepository;
        this.sessionController = sessionController;

        frame.setClassName("container");
        add(frame);
    }
}
