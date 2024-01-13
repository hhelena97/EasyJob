package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.StudentSucheService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "unternehmen/mitarbeiter-finden", layout = UnternehmenLayout.class)
@StyleSheet("styles/MitarbeiterFindenView.css")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class MitarbeiterFindenView extends VerticalLayout implements BeforeEnterObserver {

    private final StudentSucheService studentSucheService;

    private final StudentService studentService;

    private VerticalLayout studentenListLayout;

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

    @Autowired
    public MitarbeiterFindenView (StudentService studentService, StudentSucheService studentSucheService){
        this.studentSucheService = studentSucheService;
        this.studentService = studentService;
        initializeView();
    }

    private void initializeView(){
        addClassName("studentList-view");

        //setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        // Suchfeld mit Enter-Aktivierung und Options-Icon
        TextField searchField = new TextField();
        searchField.setPlaceholder("Suche");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassName("search-field");
        searchField.addKeyPressListener(Key.ENTER, e -> searchStudent(searchField.getValue()));

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.addClassName("search-layout");

        // Liste für Studentanzeigen
        studentenListLayout = new VerticalLayout();
        studentenListLayout.setWidthFull();
        studentenListLayout.setAlignItems(Alignment.STRETCH);
        studentenListLayout.setClassName("student-list-layout");

        // Student laden und anzeigen
        loadstudent();

        // Reihenfolge der Komponenten festlegen
        add(searchLayout, studentenListLayout);
    }

    private void loadstudent() {
        Object sessionAttribute = VaadinSession.getCurrent().getAttribute("filteredStudentIds");
        if (sessionAttribute instanceof List) {
            List<Integer> studentIds = (List<Integer>) sessionAttribute;
            if (!studentIds.isEmpty()) {
                List<Student> studenten = studentService.getStudentenByIds(studentIds);
                studenten.forEach(this::addStudentComponentToLayout);

                // Entfernen der IDs aus der Sitzung, um zukünftige Konflikte zu vermeiden
                VaadinSession.getCurrent().setAttribute("filteredStudentIds", null);
            }
        }else {
            List<Student> studenten = studentService.getAllStudent();
            studenten.forEach(this::addStudentComponentToLayout);
            VaadinSession.getCurrent().setAttribute("searchedStudents", studenten);
        }

    }
    private void searchStudent(String searchText) {
        studentenListLayout.removeAll();
        List<Student> students = studentSucheService.istVollTextSuche(searchText) ?
                studentSucheService.vollTextSuche(searchText) :
                studentSucheService.teilZeichenSuche(searchText);
        students.forEach(this::addStudentComponentToLayout);
        VaadinSession.getCurrent().setAttribute("searchedStudents", students);
    }

    private void addStudentComponentToLayout(Student student) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("student-card");
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.STRETCH);
        card.setWidth("100%");

        HorizontalLayout frame = new HorizontalLayout();


        VerticalLayout foto = new VerticalLayout();
        foto.setWidth("63px");
        boolean hasProfileImage = student.getFoto() != null;

        Image profilePic = new Image( hasProfileImage ? student.getFoto() : "images/blank-profile-picture.png", "Profilbild");
        profilePic.addClassName("ellipse-profile-picture");
        foto.add(profilePic);

        VerticalLayout studienDetails = new VerticalLayout();
        studienDetails.setSpacing(false);
        studienDetails.setAlignItems(Alignment.START);
        studienDetails.addClassName("student-details");


        RouterLink vorUndNachname = new RouterLink("", StudentProfilView.class, student.getId_Person());
        vorUndNachname.addClassName("name-label");
        vorUndNachname.add(student.getVorname() + " " + student.getNachname());

        HorizontalLayout studyFieldLayout = new HorizontalLayout();
        Icon studyIcon = VaadinIcon.ACADEMY_CAP.create();
        studyIcon.addClassName("iconsInStudentIcons");
        Label studyFieldLabel = new Label(student.getStudienfach().getFach()+"("+(student.getStudienfach().getAbschluss()
                .equals("Bachelor") ? "B.Sc." : "M.Sc.") +")");
        studyFieldLabel.addClassName("detail-label");
        studyFieldLayout.add(studyIcon, studyFieldLabel);

        HorizontalLayout locationLayout = new HorizontalLayout();
        Icon locationIcon = VaadinIcon.MAP_MARKER.create();
        locationIcon.addClassName("iconsInStudentIcons");
        Label locationLabel = new Label(studentService.getAllOrte(student.getId_Person()).stream().map(Ort::getOrt)
                .collect(Collectors.joining(", ")));
        locationLabel.addClassName("detail-label");
        locationLayout.add(locationIcon, locationLabel);

        HorizontalLayout jobCategoryLayout = new HorizontalLayout();
        Icon jobIcon = VaadinIcon.SEARCH.create();
        jobIcon.addClassName("iconsInStudentIcons");
        Label jobCategoryLabel = new Label(studentService.getAllJobKategorien(student.getId_Person()).stream()
                .map(JobKategorie::getKategorie).collect(Collectors.joining(",")));
        jobCategoryLabel.addClassName("detail-label");
        jobCategoryLayout.add(jobIcon, jobCategoryLabel);
        studienDetails.add(vorUndNachname,studyFieldLayout,locationLayout,jobCategoryLayout);
        frame.add(foto,studienDetails);

        card.add(frame);
        studentenListLayout.add(card);
    }


}
