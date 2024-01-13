package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.BerufsFeldRepository;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import java.util.Set;

@PageTitle("Kurz vor dem Ziel...")
public class Schritt2View extends RegistrierenSchritt {
    private final MultiSelectComboBox<Ort> standort = new MultiSelectComboBox<>("Standorte");
    private final MultiSelectComboBox<JobKategorie> berufsbezeichnung = new MultiSelectComboBox<>("Berufsbezeichnung");
    private final MultiSelectComboBox<Branche> branche = new MultiSelectComboBox<>("Branche");
    private final MultiSelectComboBox<BerufsFelder> berufsfeld = new MultiSelectComboBox<>("Berufsfeld");
    private final Checkbox homeOffice = new Checkbox();

    private final BerufsFeldRepository berufsFelderRepository;
    private final BrancheRepository brancheRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final OrtController ortController;

    private final Student student;

    public Schritt2View(
            BerufsFeldRepository berufsFelderRepository,
            BrancheRepository brancheRepository,
            JobKategorieRepository jobKategorieRepository,
            OrtController ortController,
            Student student
    ) {
        this.berufsFelderRepository = berufsFelderRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortController = ortController;
        this.student = student;

        insertContent();
    }

    public void insertContent() {

        berufsbezeichnung.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        berufsbezeichnung.setItems(jobKategorieRepository.findAll());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);
        berufsbezeichnung.setValue(student.getJobKategorien());
        berufsbezeichnung.setId("berufsbezeichnung_feld_id");

        branche.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);
        branche.setValue(student.getBranchen());
        branche.setId("branche_feld_id");

        berufsfeld.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        berufsfeld.setItems(berufsFelderRepository.findAll());
        berufsfeld.setItemLabelGenerator(BerufsFelder::getName);
        berufsfeld.setValue(student.getBerufsFelder());
        berufsfeld.setId("berufsfeld_feld_id");

        standort.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setId("standort_feld_id");
        standort.setRequired(true);
        standort.setValue(student.getOrte());

        homeOffice.setLabel("Ich bin offen für Home-Office");
        homeOffice.getStyle().set("--lumo-contrast-20pct","var(--icon-hellgrau)");
        homeOffice.getStyle().set("--lumo-primary-color","var(--studierende-dark)");
        setAlignSelf(Alignment.START, homeOffice);
        homeOffice.setValue(student.isHomeOffice());

        add(berufsbezeichnung, branche, berufsfeld, standort, homeOffice);
    }

    public boolean checkRequirementsAndSave() {
        student.setHomeOffice(homeOffice.getValue());
        Set<Ort> selected = standort.getSelectedItems();

        //Eingabefelder prüfen
        if (standort.isEmpty()) {
            standort.setErrorMessage("Bitte füllen Sie dieses Feld aus");
            standort.setInvalid(true);
            return false;
        } else {
            student.setOrte(selected);
        }

        if (!berufsbezeichnung.isEmpty()) {
            student.setJobKategorien(berufsbezeichnung.getSelectedItems());
        }
        if (!branche.isEmpty()) {
            student.setBranchen(branche.getSelectedItems());
        }
        if (!berufsfeld.isEmpty()) {
            student.setBerufsFelder(berufsfeld.getSelectedItems());
        }

        return true;
    }
}
