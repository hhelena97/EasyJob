package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.views.allgemein.AbstractJobDetails;
import de.hbrs.easyjob.views.components.UnternehmenLayout;

@Route(value = "unternehmen/job", layout = UnternehmenLayout.class)
@StyleSheet("JobDetailsUnternehmen.css")
public class JobDetailsView extends AbstractJobDetails {
    public JobDetailsView(SessionController sessionController, JobRepository jobRepository, MeldungController meldungController) {
        super(sessionController, jobRepository, meldungController);
    }

    @Override
    public void displayJob(Job job) {
        super.displayJob(job);
        // Editieren
        if (sessionController.getPerson().getId_Person().equals(job.getPerson().getId_Person())) {
            Button edit = new Button(FontAwesome.Solid.PENCIL.create());
            edit.addClassName("job-details-edit-button");
            edit.addClickListener(event -> UI.getCurrent().navigate("unternehmen/job/" + job.getId_Job() + "/edit"));
            buttons.add(edit);
        }
    }
}
