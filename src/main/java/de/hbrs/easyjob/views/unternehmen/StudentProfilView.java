package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.FaehigkeitService;
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
public class StudentProfilView extends VerticalLayout implements HasUrlParameter<Integer>, BeforeEnterObserver {
    @Autowired
    private final StudentService studentService;
    private final FaehigkeitService faehigkeitService;

    public StudentProfilView(StudentService studentService, FaehigkeitService faehigkeitService) {
        this.studentService = studentService;
        this.faehigkeitService = faehigkeitService;
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
                .anyMatch(a -> a.getAuthority().equals("ROLE_UNTERNEHMENSPERSON"));
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
        return new StudentProfileComponent(student, "styles/UnternehmenStudentProfilView.css",studentService, faehigkeitService);
    }
}
