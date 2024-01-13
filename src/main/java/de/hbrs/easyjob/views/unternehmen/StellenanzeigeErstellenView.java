package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.controllers.StellenanzeigeController;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.PrefixUtil;
import de.hbrs.easyjob.views.components.StyledDialog;

import javax.annotation.security.RolesAllowed;
import java.time.ZoneId;
import java.util.Date;

// TODO: Weiterleitung von Unternehmensprofil fixen (siehe Testklasse)
@Route("unternehmen/stellenanzeige/erstellen")
@PageTitle("Stellenanzeige erstellen")
@StyleSheet("Variables.css")
@StyleSheet("StellenanzeigeErstellen.css")
@RolesAllowed({"ROLE_UNTERNEHMENSPERSON"})
public class StellenanzeigeErstellenView extends VerticalLayout implements BeforeEnterObserver {

    private final DatePicker eintrittsdatum;
    private final ComboBox<JobKategorie> berufsbezeichnung;
    private final ComboBox<Ort> standort;
    private final MultiSelectComboBox<Studienfach> studienfach;
    private final TextField titel;
    private final TextArea stellenbeschreibung;
    private final Checkbox homeOffice;
    private final transient StellenanzeigeController stellenanzeigeController;
    private final transient SessionController sessionController;
    private final transient Unternehmensperson unternehmensperson;

    /**
     * Prüft, ob der Nutzer eingeloggt ist und die Rolle ROLE_UNTERNEHMENSPERSON hat.
     * Wenn nicht, wird er auf die Login-Seite weitergeleitet.
     *
     * @param event Event, das vor dem Aufruf der View ausgelöst wird
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
            event.rerouteTo(LoginView.class);
        }
    }

    /**
     * Erstellt die View zum Erstellen einer Stellenanzeige.
     *
     * @param jobKategorieRepository   Repository für JobKategorien
     * @param studienfachRepository    Repository für Studienfächer
     * @param ortController            Controller für Orte
     * @param sessionController        Controller für die Session
     * @param stellenanzeigeController Controller für Stellenanzeigen
     */
    public StellenanzeigeErstellenView(
            JobKategorieRepository jobKategorieRepository,
            StudienfachRepository studienfachRepository,
            OrtController ortController,
            SessionController sessionController,
            StellenanzeigeController stellenanzeigeController
    ) {
        this.sessionController = sessionController;
        this.stellenanzeigeController = stellenanzeigeController;

        unternehmensperson = (Unternehmensperson) sessionController.getPerson();

        // Abbrechen-Dialog
        Button abbrechenClose = new Button("Nein");
        abbrechenClose.addClassName("close-unternehmen");

        Button abbrechenConfirm = new Button("Ja", e -> UI.getCurrent().getPage().getHistory().back());
        abbrechenConfirm.setClassName("confirm");

        StyledDialog abbrechenDialog = new StyledDialog(
                "Wollen Sie wirklich abbrechen?",
                "Ihre Änderungen werden nicht gespeichert und die Stellenanzeige wird nicht erstellt.",
                abbrechenClose,
                abbrechenConfirm
        );

        // Container
        VerticalLayout frame = new VerticalLayout();
        frame.setClassName("container");

        // Bild hinzufügen Box
        Button bildHinzufuegenButton = new Button("Bild hinzufügen", FontAwesome.Solid.PLUS_CIRCLE.create());
        bildHinzufuegenButton.setClassName("bild-hinzufuegen-button");
        frame.add(bildHinzufuegenButton);


        // Zurück Button
        Button zurueckButton = new Button("zurück", FontAwesome.Solid.CHEVRON_LEFT.create());
        zurueckButton.setClassName("zurueck-button");

        // TODO: Einträge speichern
        zurueckButton.addClickListener(e -> zurueckHandler());

        frame.add(zurueckButton);

        // Eintrittsdatum
        eintrittsdatum = new DatePicker("Eintrittsdatum");
        eintrittsdatum.setPlaceholder("Datum auswählen");
        frame.add(eintrittsdatum);

        // Berufsbezeichnung
        berufsbezeichnung = new ComboBox<>();
        berufsbezeichnung.setLabel("Berufsbezeichnung");
        berufsbezeichnung.setItems(jobKategorieRepository.findAll());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);
        berufsbezeichnung.setPlaceholder("Berufsbezeichnung(en) auswählen");
        frame.add(berufsbezeichnung);

        // Studienfach
        studienfach = new MultiSelectComboBox<>("Studienfach");
        studienfach.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
        studienfach.setItems(studienfachRepository.findAll());
        studienfach.setItemLabelGenerator(fach -> fach.getFach() + " (" + fach.getAbschluss() + ")");
        studienfach.setPlaceholder("Studienfach auswählen");

        frame.add(studienfach);


        // Standort
        standort = new ComboBox<>("Standort");
        standort.setClassName("standort");
        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setPlaceholder("Standort auswählen");

        PrefixUtil.setPrefixComponent(standort, FontAwesome.Solid.MAP_MARKER_ALT.create());

        frame.add(standort);

        // Titel
        titel = new TextField("Titel");
        titel.setPlaceholder("Titel eingeben");
        frame.add(titel);

        // Stellenbeschreibung
        stellenbeschreibung = new TextArea("Stellenbeschreibung");
        stellenbeschreibung.setPlaceholder("Tragen Sie hier die Stellenbeschreibung ein");
        stellenbeschreibung.setMaxLength(2500);
        stellenbeschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        stellenbeschreibung.setHelperText("Maximal 2500 Zeichen");

        // Zeichenanzahl anzeigen
        stellenbeschreibung.addValueChangeListener(
                event -> event.getSource().setHelperText(String.format("%d / 2500 Zeichen", event.getValue().length()))
        );
        frame.add(stellenbeschreibung);

        // Home-Office
        homeOffice = new Checkbox("Home-Office möglich");
        frame.add(homeOffice);

        // Buttons
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setClassName("action-buttons");

        Button abbrechenButton = new Button("Abbrechen", FontAwesome.Solid.TIMES.create());
        abbrechenButton.setClassName("abbrechen-button");
        abbrechenButton.addClickListener(e -> abbrechenDialog.open());

        Button fertigButton = new Button("Fertig", FontAwesome.Solid.CHECK.create());
        fertigButton.setClassName("fertig-button");
        fertigButton.addClickListener(e -> fertigHandler());

        actionButtons.add(abbrechenButton, fertigButton);
        frame.add(actionButtons);

        add(frame);
    }

    /**
     * Handler für den Fertig-Button.
     * Speichert die Stellenanzeige und ruft die Profilseite des Unternehmens auf.
     */
    private void fertigHandler() {
        // Convert LocalDate to Date
        Date eintrittsdatumDate = Date.from(eintrittsdatum.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        stellenanzeigeController.stellenanzeigeErstellen(
                titel.getValue(),
                stellenbeschreibung.getValue(),
                eintrittsdatumDate,
                unternehmensperson.getUnternehmen(),
                unternehmensperson,
                standort.getValue(),
                berufsbezeichnung.getValue(),
                studienfach.getValue(),
                homeOffice.getValue()
        );
        UI.getCurrent().navigate("unternehmen/unternehmensprofil");
    }

    /**
     * Handler für den Zurück-Button.
     * Speichert die Stellenanzeige nicht und ruft die Profilseite des Unternehmens auf.
     */
    private void zurueckHandler() {
        UI.getCurrent().navigate("unternehmen/unternehmensprofil");
    }
}
