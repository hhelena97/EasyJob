package de.hbrs.easyjob.views.unternehmen;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.StellenanzeigeController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.PrefixUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Route("unternehmen/stellenanzeige/erstellen")
@PageTitle("Stellenanzeige erstellen")
@StyleSheet("Variables.css")
@StyleSheet("StellenanzeigeErstellen.css")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class StellenanzeigeErstellenView extends VerticalLayout implements BeforeEnterObserver {

    private final DatePicker eintrittsdatum;
    private final ComboBox<JobKategorie> berufsbezeichnung;
    private final ComboBox<Ort> standort;
    private final TextField titel;
    private final TextArea stellenbeschreibung;
    private final Checkbox homeOffice;

    private final StellenanzeigeController stellenanzeigeController;

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

    public StellenanzeigeErstellenView(JobKategorieRepository jobKategorieRepository, OrtController ortController, StellenanzeigeController stellenanzeigeController) {
        this.stellenanzeigeController = stellenanzeigeController;

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
        // TODO: Dialog, ob wirklich abbrechen
        abbrechenButton.addClickListener(e -> abbrechenHandler());

        Button fertigButton = new Button("Fertig", FontAwesome.Solid.CHECK.create());
        fertigButton.setClassName("fertig-button");
        // TODO: Stellenanzeige speichern
        fertigButton.addClickListener(e -> fertigHandler());

        actionButtons.add(abbrechenButton, fertigButton);
        frame.add(actionButtons);

        add(frame);
    }

    private void abbrechenHandler() {
        // TODO: Dialog, ob wirklich abbrechen
        UI.getCurrent().getPage().getHistory().back();
    }

    private void fertigHandler() {
        // TODO: Stellenanzeige speichern

        // Convert LocalDate to Date
        Date eintrittsdatumDate = Date.from(eintrittsdatum.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // TODO: Unternehmen und Unternehmensperson aus Session holen
        // TODO: Studienfach input in Mock-Up anlegen
        stellenanzeigeController.stellenanzeigeErstellen(
                titel.getValue(),
                stellenbeschreibung.getValue(),
                eintrittsdatumDate,
                new Unternehmen(),
                new Unternehmensperson(),
                standort.getValue(),
                berufsbezeichnung.getValue(),
                Set.of(new Studienfach()),
                homeOffice.getValue()
        );
        UI.getCurrent().getPage().getHistory().back();
    }

    private void zurueckHandler() {
        // TODO: Speichern
        abbrechenHandler();
    }
}
