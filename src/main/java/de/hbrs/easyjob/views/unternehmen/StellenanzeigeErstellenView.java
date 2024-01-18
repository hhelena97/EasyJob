package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.controllers.StellenanzeigeController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.JobRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.PrefixUtil;
import de.hbrs.easyjob.views.components.StyledDialog;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Route("unternehmen/job/create")
@RouteAlias("unternehmen/job/:jobId/edit")
@PageTitle("Stellenanzeige erstellen")
@StyleSheet("Variables.css")
@StyleSheet("StellenanzeigeErstellen.css")
@RolesAllowed({"ROLE_UNTERNEHMENSPERSON"})
public class StellenanzeigeErstellenView extends VerticalLayout implements BeforeEnterObserver {

    // Components
    private final DatePicker eintrittsdatum;
    private final ComboBox<JobKategorie> berufsbezeichnung;
    private final ComboBox<Ort> standort;
    private final MultiSelectComboBox<Studienfach> studienfach;
    private final TextField titel;
    private final TextArea stellenbeschreibung;
    private final Checkbox homeOffice;

    // Entities
    private transient Job job;
    private int jobId;
    private final transient Unternehmensperson unternehmensperson;

    // Repositories
    private final transient JobRepository jobRepository;

    // Controllers
    private final transient SessionController sessionController;
    private final transient StellenanzeigeController stellenanzeigeController;

    // Constants
    private static final String NEXT_PAGE = "unternehmen";

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
        } else {
            try {
                jobId = Integer.parseInt(event.getRouteParameters().get("jobId").orElse("0"));
            } catch (NumberFormatException e) {
                jobId = -1;
            }
            loadJobData();
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
            JobRepository jobRepository,
            StudienfachRepository studienfachRepository,
            OrtController ortController,
            SessionController sessionController,
            StellenanzeigeController stellenanzeigeController
    ) {
        this.jobRepository = jobRepository;
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

        /*
        // Bild hinzufügen Box
        Div imageContainer = new Div();
        imageContainer.addClassName("bild-upload-container");

        Button bildHinzufuegenButton = new Button("Bild hinzufügen", FontAwesome.Solid.PLUS_CIRCLE.create());
        bildHinzufuegenButton.setClassName("bild-upload-button");



        MemoryBuffer imageBuffer = new MemoryBuffer();
        Upload bildUpload = new Upload(imageBuffer);
        bildUpload.addClassName("bild-upload");
        bildUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/webp");
        bildUpload.setUploadButton(bildHinzufuegenButton);
        bildUpload.setDropAllowed(false);
        bildUpload.addSucceededListener(e -> {
            InputStream imageStream = imageBuffer.getInputStream();
            Image image = FileUpload.imageUpload(imageStream, "job.jpg");
            job.setBild(image.getSrc());
            imageContainer.getStyle().set("background-image", "url(" + image.getSrc() + ")");
        });

        imageContainer.add(bildUpload, bildHinzufuegenButton);

        frame.add(imageContainer);
        */

        // Zurück Button
        Button zurueckButton = new Button("zurück", FontAwesome.Solid.CHEVRON_LEFT.create());
        zurueckButton.setClassName("zurueck-button");

        zurueckButton.addClickListener(e -> saveEntity(false));

        frame.add(zurueckButton);

        // Eintrittsdatum
        eintrittsdatum = new DatePicker("Eintrittsdatum");
        eintrittsdatum.setPlaceholder("Datum auswählen");
        eintrittsdatum.setId("DatumAuswaehlenId");
        eintrittsdatum.setMin(LocalDate.now(ZoneId.systemDefault()));
        eintrittsdatum.setMax(LocalDate.now(ZoneId.systemDefault()).plusYears(1));
        eintrittsdatum.setHelperText("Eintrittsdatum darf maximal ein Jahr in der Zukunft liegen");
        frame.add(eintrittsdatum);

        // Berufsbezeichnung
        berufsbezeichnung = new ComboBox<>();
        berufsbezeichnung.setLabel("Berufsbezeichnung");
        berufsbezeichnung.setId("BerufsbezeichnungWaehlenID");
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
        standort.setId("standortAuswaehlenId");
        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setPlaceholder("Standort auswählen");

        PrefixUtil.setPrefixComponent(standort, FontAwesome.Solid.MAP_MARKER_ALT.create());

        frame.add(standort);

        // Titel
        titel = new TextField("Titel");
        titel.setPlaceholder("Titel eingeben");
        titel.setId("titleID");
        frame.add(titel);

        // Stellenbeschreibung
        stellenbeschreibung = new TextArea("Stellenbeschreibung");
        stellenbeschreibung.setId("stellenbeschreibungEinfuegenId");
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
        homeOffice.setId("HomeofficeJaNeinId");
        frame.add(homeOffice);

        // Buttons
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setClassName("action-buttons");

        Button abbrechenButton = new Button("Abbrechen", FontAwesome.Solid.TIMES.create());
        abbrechenButton.setClassName("abbrechen-button");
        abbrechenButton.addClickListener(e -> abbrechenDialog.open());

        Button fertigButton = new Button("Fertig", FontAwesome.Solid.CHECK.create());
        fertigButton.setClassName("fertig-button");
        fertigButton.addClickListener(e -> saveEntity(true));

        actionButtons.add(abbrechenButton, fertigButton);
        frame.add(actionButtons);

        add(frame);
    }

    /**
     * Erstellt oder aktualisiert eine Stellenanzeige mit den Daten aus den Feldern.
     *
     * @param aktiv true, wenn die Stellenanzeige aktiv sein soll
     */
    private void saveEntity(boolean aktiv) {
        job.setEintritt(Date.from(eintrittsdatum.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        job.setJobKategorie(berufsbezeichnung.getValue());
        job.setOrt(standort.getValue());
        job.setStudienfacher(studienfach.getValue());
        job.setTitel(titel.getValue());
        job.setFreitext(stellenbeschreibung.getValue());
        job.setHomeOffice(homeOffice.getValue());
        job.setAktiv(aktiv);

        Job newJob;
        if (jobId == 0) {
            newJob = stellenanzeigeController.stellenanzeigeErstellen(job, unternehmensperson);
        } else {
            newJob = stellenanzeigeController.stellenanzeigeAktualisieren(job, unternehmensperson);
        }

        if (newJob == null) {
            Notification.show("Bitte füllen Sie alle Felder aus.");
        } else {
            if (aktiv) {
                UI.getCurrent().navigate(NEXT_PAGE + "/job/" + newJob.getId_Job());
            } else {
                UI.getCurrent().navigate(NEXT_PAGE);
            }
        }
    }

    /**
     * Lädt die Daten des Jobs in die Felder, wenn ein bestehender Job bearbeitet wird.
     * Erstellt einen neuen Job, wenn keine Jobid übergeben wurde.
     * Leitet auf die Profilseite des Unternehmens weiter, wenn die Jobid ungültig ist.
     */
    private void loadJobData() {
        if (jobId == 0) {
            // Neuer Job wird erstellt
            job = new Job();
            job.setUnternehmen(unternehmensperson.getUnternehmen());
            job.setPerson(unternehmensperson);
        } else if (jobId == -1) {
            // Jobid ist ungültig
            UI.getCurrent().navigate(NEXT_PAGE);
        } else {
            // Bestehender Job wird bearbeitet
            jobRepository.findById(jobId).ifPresentOrElse(
                    presentJob -> this.job = presentJob,
                    () -> UI.getCurrent().navigate(NEXT_PAGE)
            );
            if (stellenanzeigeController.canEdit(job, unternehmensperson)) {
                // Vorhandene Daten werden in die Felder eingetragen
                eintrittsdatum.setValue(job.getEintritt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                berufsbezeichnung.setValue(job.getJobKategorie());
                standort.setValue(job.getOrt());
                studienfach.setValue(job.getStudienfacher());
                titel.setValue(job.getTitel());
                stellenbeschreibung.setValue(job.getFreitext());
                homeOffice.setValue(job.isHomeOffice());
            } else {
                // Job darf nicht bearbeitet werden
                UI.getCurrent().navigate(NEXT_PAGE);
            }
        }
    }
}
