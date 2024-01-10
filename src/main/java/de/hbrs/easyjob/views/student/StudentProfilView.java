package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.StudentProfileComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;

@Route(value = "student" , layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
public class StudentProfilView extends VerticalLayout implements BeforeEnterObserver  {


    private final transient SessionController sessionController;
    private Student student;
    private final transient StudentService studentService;

    @Autowired
    public StudentProfilView(SessionController sessionController, StudentService studentService) {
        this.sessionController = sessionController;
        this.studentService = studentService;
        student= (Student) sessionController.getPerson();
        if(student == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        initializeView();

    }

    private void initializeView() {
        if (student == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        StudentProfileComponent studentProfile = new StudentProfileComponent(student, "StudentProfilView.css",studentService);
        add(studentProfile);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn()|| !sessionController.hasRole("ROLE_STUDENT")){
            event.rerouteTo(LoginView.class);
        }
    }

}
