package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.StudentProfileComponent;

import javax.annotation.security.RolesAllowed;

@Route(value = "student" , layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
@PageTitle("Profil")
public class StudentProfilView extends VerticalLayout implements BeforeEnterObserver  {
    // Controller
    private final transient SessionController sessionController;

    // Service
    private final transient StudentService studentService;

    // Entity
    private final transient Student student;

    public StudentProfilView(StudentService studentService, SessionController sessionController) {
        this.studentService = studentService;
        this.sessionController = sessionController;
        student = (Student) sessionController.getPerson();
        initializeView();
    }

    private void initializeView() {
        StudentProfileComponent studentProfile = new StudentProfileComponent(student, "StudentProfilView.css", studentService);
        add(studentProfile);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_STUDENT")) {
            event.rerouteTo(LoginView.class);
        }
    }

}
