package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.MeldungController;
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

    private Student student;
    private final StudentService studentService;
    private final PersonService personService;

    private final MeldungController meldungController;

    @Autowired
    public StudentProfilView(StudentService studentService, PersonService personService, MeldungController meldungController) {
        this.studentService = studentService;
        this.personService = personService;
        this.meldungController = meldungController;
        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if(context != null) {
            Authentication auth = context.getAuthentication();
            if (auth != null && auth.isAuthenticated() && hasRole(auth)) {
                    //Sarah: das wirft einen Fehler, ich weiß nicht warum
                    //student = (Student) personService.getCurrentPerson();
                    initializeView();
            } else {
                UI.getCurrent().navigate(LoginView.class);
            }
        } else {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    private void initializeView() {
        if (student == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        StudentProfileComponent studentProfile = new StudentProfileComponent(student, "StudentProfilView.css",studentService, meldungController);
        add(studentProfile);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        SecurityContext context = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        if(context != null) {
            Authentication auth = context.getAuthentication();
            if (auth == null || !auth.isAuthenticated() || !hasRole(auth)) {
                event.rerouteTo(LoginView.class);
            }
        } else {
            event.rerouteTo(LoginView.class);
        }
    }


    private boolean hasRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
    }

}
