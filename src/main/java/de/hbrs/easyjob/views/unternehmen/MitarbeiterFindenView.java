package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.service.StudentService;
import de.hbrs.easyjob.services.StudentSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "unternehmen/mitarbeiter-finden", layout = UnternehmenLayout.class)
@StyleSheet("styles/MitarbeiterFindenView.css")
public class MitarbeiterFindenView extends VerticalLayout {

    private final StudentSearchService studentSearchService;

    private final StudentService studentService;

    private VerticalLayout studentenListLayout;

    @Autowired
    public MitarbeiterFindenView (StudentService studentService, StudentSearchService studentSearchService){
        this.studentSearchService = studentSearchService;
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

        // Options-Icon als Suffix zum Suchfeld hinzufügen
        Button optionButton = new Button(new Icon(VaadinIcon.OPTIONS));
        optionButton.addClickListener(e -> UI.getCurrent().navigate("unternehmen/filter"));
        optionButton.addClassName("options-button");

        // Layout für das Suchfeld
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, optionButton);
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
        List<Student> students = studentSearchService.istVollTextSuche(searchText) ?
                studentSearchService.vollTextSuche(searchText) :
                studentSearchService.teilZeichenSuche(searchText);
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
        Image profilePic = new Image("images/blank-profile-picture.png", "Profilbild");
        profilePic.addClassName("ellipse-profile-picture");
        foto.add(profilePic);

        VerticalLayout studienDetails = new VerticalLayout();
        studienDetails.setSpacing(false);
        studienDetails.setAlignItems(Alignment.START);
        studienDetails.addClassName("student-details");


        HorizontalLayout vorUndNachname = new HorizontalLayout();
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
