package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentProfileComponent;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;


@Route(value = "studnet-Profil",layout = UnternehmenLayout.class)
@PageTitle("Student Profile")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class StudentProfilView extends VerticalLayout implements HasUrlParameter<Integer> {
    @Autowired
    private final StudentService studentService;

    private final MeldungController meldungController;

    public StudentProfilView(StudentService studentService, MeldungController meldungController) {
        this.studentService = studentService;
        this.meldungController = meldungController;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer studentID) {
        if ( studentID != null) {
            Student student = studentService.getStudentByID(studentID);
            if(student==null){
                throw new RuntimeException("Etwas schief gelaufen!");
            }
            add(createStudentDetailLayout(student));
        } else {
            add(new H3("Job-Details konnten nicht geladen werden."));
        }
    }

    private Component createStudentDetailLayout(Student student) {
        return new StudentProfileComponent(student, "styles/UnternehmenStudentProfilView.css",studentService, meldungController);
    }
}
