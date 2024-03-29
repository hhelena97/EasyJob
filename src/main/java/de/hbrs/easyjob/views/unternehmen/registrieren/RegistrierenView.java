package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.UnternehmenspersonService;
import de.hbrs.easyjob.views.components.DialogLayout;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("UnternehmenRegistrieren.css")
public class RegistrierenView extends VerticalLayout {

    // Repositories
    UnternehmenspersonRepository unternehmenspersonRepository;
    UnternehmenRepository unternehmenRepository;
    OrtRepository ortRepository;
    JobRepository jobRepository;

    //Services
    @Autowired
    private final UnternehmenspersonService unternehmenspersonService;

    // Entities
    private final Unternehmen unternehmen = new Unternehmen();
    private final Unternehmensperson unternehmensperson;

    // Components
    private final VerticalLayout frame = new VerticalLayout();
    private final H1 header = new H1("Ist Ihr Unternehmen bereits bei EasyJob registriert?"); // TODO: Update based on view
    private final Button next;

    // Views
    private final RegistrierenSchritt[] views = new RegistrierenSchritt[5];
    private int currentView = 0;
    private boolean neuesUnternehmen = false;

    public RegistrierenView(
            OrtController ortController,
            BrancheRepository brancheRepository,
            JobRepository jobRepository,
            UnternehmenRepository unternehmenRepository,
            OrtRepository ortRepository,
            UnternehmenspersonRepository unternehmenspersonRepository, UnternehmenspersonService unternehmenspersonService,
            Unternehmensperson unternehmensperson
    ) {
        this.ortRepository = ortRepository;
        this.jobRepository = jobRepository;
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.unternehmenspersonService = unternehmenspersonService;
        this.unternehmensperson = unternehmensperson;
        this.unternehmenRepository = unternehmenRepository;

        // Layout
        addClassName("body");
        frame.setClassName("container");
        frame.setAlignItems(Alignment.CENTER);
        frame.setAlignSelf(Alignment.CENTER);

        // Header
        header.addClassName("header");
        frame.add(header);

        // Main Content
        views[0] = new Schritt1View(unternehmenRepository, unternehmensperson, unternehmen);
        views[1] = new Schritt2View(brancheRepository, ortController, unternehmen);
        views[2] = new Schritt3View(unternehmen);
        views[3] = new Schritt4View(ortController, unternehmensperson);
        views[4] = new Schritt5View(unternehmensperson);

        frame.add(views[currentView]);

        // Abbrechen Dialog
        DialogLayout abbrechenDialog = new DialogLayout(true);
        abbrechenDialog.insertDialogContent(
                "Möchten Sie den Vorgang wirklich abbrechen?",
                "Ihre Eingaben werden nicht gespeichert!",
                "Nein, hier bleiben.",
                "Ja, abbrechen.",
                "Unternehmen",
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
            header.setText("Über das Unternehmen...");
        } else if (currentView == 2) {
            header.setText("Ihr Logo...");
        } else if (currentView == 3) {
            header.setText("Ich bin...");
        } else if (currentView == 4) {
            header.setText("Ihr persönliches Profilbild...");
        } else {
            header.setText("Ist Ihr Unternehmen bereits bei EasyJob registriert?");
        }
    }

    // Next View
    private void nextView() {
        if (!views[currentView].checkRequirementsAndSave()) {
            return;
        }

        if (currentView == 0) {
            if (unternehmensperson.getUnternehmen() != null) {
                frame.replace(views[currentView], views[currentView + 3]);
                currentView += 3;
                neuesUnternehmen = false;
            } else {
                frame.replace(views[currentView], views[++currentView]);
                neuesUnternehmen = true;
            }
        } else if (currentView < views.length - 1) {
            frame.replace(views[currentView], views[++currentView]);
        } else {
            if(neuesUnternehmen) {
                unternehmensperson.setUnternehmen(unternehmen);
            }
            unternehmenspersonService.saveUnternehmensperson(unternehmensperson);

            DialogLayout finishDialog = new DialogLayout(true);
            Button weiterZumLogin = new Button("Weiter zum Login");
            weiterZumLogin.addClassName("close-unternehmen");
            finishDialog.simpleDialog("Account erfolgreich angelegt!", weiterZumLogin, "login");
            finishDialog.openDialogOverlay();
        }
        updateHeader();
        updateButton();
    }

    // Previous View
    private void previousView() {
        if (currentView > 0) {
            if (!neuesUnternehmen && currentView == 3) {
                frame.replace(views[currentView], views[currentView - 3]);
                currentView -= 3;
            } else {
                frame.replace(views[currentView], views[--currentView]);
            }
        } else {
            getUI().ifPresent(ui -> ui.navigate("login"));
        }
        updateHeader();
        updateButton();
    }
}
