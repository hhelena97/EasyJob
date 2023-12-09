package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.registrieren.StudentRegistrierenController;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.views.components.DialogLayout;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("StudentRegistrieren.css")
public class RegistrierenView extends VerticalLayout {
    // Controller
    private final StudentRegistrierenController studentRegistrierenController;

    // Entities
    private final Student student;

    // Components
    private final VerticalLayout frame = new VerticalLayout();
    private final H1 header = new H1("Ich bin..."); // TODO: Update based on view
    private final Button next;

    // Views
    private final RegistrierenSchritt[] views = new RegistrierenSchritt[3];
    private int currentView = 0;

    public RegistrierenView(
            BerufsFeldRepository berufsFeldRepository,
            BrancheRepository brancheRepository,
            JobKategorieRepository jobKategorieRepository,
            OrtRepository ortRepository,
            PersonRepository personRepository,
            StudienfachRepository studienfachRepository,
            OrtController ortController,
            Student student
    ) {

        this.studentRegistrierenController = new StudentRegistrierenController(
                berufsFeldRepository,
                brancheRepository,
                jobKategorieRepository,
                ortRepository,
                personRepository,
                studienfachRepository
        );

        this.student = student;

        // Layout
        addClassName("body");
        frame.setClassName("container");
        frame.setAlignItems(Alignment.CENTER);
        frame.setAlignSelf(Alignment.CENTER);

        // Header
        header.addClassName("header");
        frame.add(header);

        // Main Content
        views[0] = new Schritt1View(studienfachRepository, student);
        views[1] = new Schritt2View(berufsFeldRepository, brancheRepository, jobKategorieRepository, ortController, student);
        views[2] = new Schritt3View(student);

        frame.add(views[currentView]);

        // Abbrechen Dialog
        DialogLayout abbrechenDialog = new DialogLayout(true);
        abbrechenDialog.insertDialogContent(
                "Möchtest du den Vorgang wirklich abbrechen?",
                "Deine Eingaben werden nicht gespeichert!",
                "Nein, hier bleiben.",
                "Ja, abbrechen.",
                "Student",
                "login"
        );

        // Buttons
        next = new Button("Weiter", new Icon(VaadinIcon.ARROW_RIGHT));
        next.addClassName("next");
        next.addClickListener(e -> nextView());

        Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
        back.addClassName("back");
        back.addClickListener(e -> previousView());

        Button cancel = new Button("Abbrechen");
        cancel.addClickListener(e -> abbrechenDialog.openDialogOverlay());
        cancel.addClassName("cancel");

        // Buttons Container
        VerticalLayout buttonsContainer = new VerticalLayout();
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);

        buttonsContainer.add(actions, cancel);
        buttonsContainer.setSpacing(false);
        buttonsContainer.setAlignItems(Alignment.CENTER);

        frame.add(buttonsContainer);
        add(frame);
    }

    public void updateButton() {
        if (currentView == views.length - 1) {
            next.setText("Fertig");
            next.setIcon(new Icon(VaadinIcon.CHECK));
        } else {
            next.setText("Weiter");
            next.setIcon(new Icon(VaadinIcon.ARROW_RIGHT));
        }
    }

    private void updateHeader() {
        if (currentView == 1) {
            header.setText("Ich suche...");
        } else if (currentView == 2) {
            header.setText("Fast geschafft...");
        } else {
            header.setText("Ich bin...");
        }
    }

    private void nextView() {
        if (!views[currentView].checkRequirementsAndSave()) {
            return;
        }

        if (currentView < views.length - 1) {
            frame.replace(views[currentView], views[++currentView]);
        } else {
            if (studentRegistrierenController.createStudent(student, true)) {
                DialogLayout finishDialog = new DialogLayout(true);
                Button weiterZumLogin = new Button("Weiter zum Login");
                weiterZumLogin.addClassName("close-student");
                finishDialog.simpleDialog("Account erfolgreich angelegt!", weiterZumLogin, "login");
                finishDialog.openDialogOverlay();
            } else {
                // TODO: Fehlermeldung
                System.err.println("Registrierung fehlgeschlagen");
            }
        }
        updateHeader();
        updateButton();
    }

    private void previousView() {
        if (currentView > 0) {
            frame.replace(views[currentView], views[--currentView]);
        } else {
            getUI().ifPresent(ui -> ui.navigate("login"));
        }
        updateHeader();
        updateButton();
    }
}
