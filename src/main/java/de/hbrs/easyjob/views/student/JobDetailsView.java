package de.hbrs.easyjob.views.student;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.views.allgemein.AbstractJobDetails;
import de.hbrs.easyjob.views.components.StudentLayout;

@Route(value = "student/job", layout = StudentLayout.class)
@StyleSheet("JobDetailsStudent.css")
public class JobDetailsView extends AbstractJobDetails {



    public JobDetailsView(SessionController sessionController, JobRepository jobRepository, MeldungController meldungController) {
        super(sessionController, jobRepository, meldungController);
    }

    @Override
    public void displayJob(Job job) {
        super.displayJob(job);

        // Contact info
        Div contactContainer = new Div(
                new H5("Ansprechperson:"));
        RouterLink name = new RouterLink("", UnternehmensPersonProfilView.class, job.getPerson().getId_Person());
        name.add(job.getPerson().getVorname() + " " + job.getPerson().getNachname());
        contactContainer.addClassName("job-details-contact");
        descriptionContainer.add(contactContainer);
        contactContainer.add(name);

        // Action buttons
        VerticalLayout actionButtons = new VerticalLayout();
        actionButtons.addClassName("job-details-action-buttons");

        Button applyButton = new Button("Jetzt Bewerben",e -> UI.getCurrent().navigate("chat/" + "Job"+ "/"+job.getId_Job()));
        applyButton.addClassName("job-details-apply-button");

        Button moreJobsButton = new Button("mehr Jobs entdecken", FontAwesome.Solid.CHEVRON_RIGHT.create(), e -> UI.getCurrent().navigate("student/jobs-finden"));
        moreJobsButton.addClassName("job-details-more-jobs-button");
        moreJobsButton.setIconAfterText(true);

        actionButtons.add(applyButton, moreJobsButton);
        frame.add(actionButtons);
    }
}
