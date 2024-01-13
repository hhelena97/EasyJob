package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.StudentProfileComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "student" , layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("StudentRegistrieren.css")
@StyleSheet("StudentProfilView.css")
public class StudentProfilView extends VerticalLayout implements BeforeEnterObserver  {


    private final transient SessionController sessionController;
    private Student student;
    private final transient StudentService studentService;
    private final MeldungController meldungController;
    private final FaehigkeitService faehigkeitService;

    @Autowired
    public StudentProfilView(SessionController sessionController,
                             StudentService studentService,
                             MeldungController meldungController,
                             FaehigkeitService faehigkeitService) {
        this.sessionController = sessionController;
        this.studentService = studentService;
        this.faehigkeitService = faehigkeitService;
        student = (Student) sessionController.getPerson();
        this.meldungController = meldungController;
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
        StudentProfileComponent studentProfile = new StudentProfileComponent(student, "StudentProfilView.css",studentService,meldungController,faehigkeitService);
        add(studentProfile);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn()|| !sessionController.hasRole("ROLE_STUDENT")){
            event.rerouteTo(LoginView.class);
        }
    }

}
