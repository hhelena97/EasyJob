package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.PersonController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.StudentProfileComponentBearbeitung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;

@Route(value = "student/studentbearbeitung" , layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("StudentRegistrieren.css")
@StyleSheet("StudentProfilView.css")
public class StudentProfilBearbeitungView extends VerticalLayout implements BeforeEnterObserver  {

    private Student student;
    private final StudentService studentService;
    private final SessionController sessionController;

    final FaehigkeitService faehigkeitService;

    @Autowired
    StudienfachRepository studienfachRepository;
    @Autowired
    BerufsFeldRepository berufsFeldRepository;
    @Autowired
    BrancheRepository brancheRepository;
    @Autowired
    JobKategorieRepository jobKategorieRepository;
    final FaehigkeitRepository faehigkeitRepository;
    @Autowired
    OrtController ortController;

    @Autowired
    PasswortService passwortService;

    @Autowired
    public StudentProfilBearbeitungView(StudentService studentService,
                                        SessionController sessionController,
                                        StudienfachRepository studienfachRepository,
                                        BerufsFeldRepository berufsFelderRepository,
                                        BrancheRepository brancheRepository,
                                        JobKategorieRepository jobKategorieRepository,
                                        OrtController ortController,
                                        FaehigkeitRepository faehigkeitRepository,
                                        FaehigkeitService faehigkeitService,
                                        PasswortService passwortService) {

        this.studentService = studentService;
        this.sessionController = sessionController;
        this.studienfachRepository = studienfachRepository;
        this.berufsFeldRepository = berufsFelderRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortController = ortController;
        this.faehigkeitRepository = faehigkeitRepository;
        this.faehigkeitService = faehigkeitService;
        this.passwortService = passwortService;
        student = (Student) sessionController.getPerson();
        initializeView();


    }

    private void initializeView() {
        if (student == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        StudentProfileComponentBearbeitung studentProfile = new StudentProfileComponentBearbeitung(student,
                "StudentProfilView.css",
                studentService,
                faehigkeitService,
                studienfachRepository,
                berufsFeldRepository,
                brancheRepository,
                jobKategorieRepository,
                faehigkeitRepository,
                ortController,
                passwortService
        );
        add(studentProfile);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_STUDENT")){
            event.rerouteTo(LoginView.class);
        }
    }

}
