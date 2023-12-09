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

        berufsbezeichnung.setItems(jobKategorieRepository.findAll());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);
        berufsbezeichnung.setValue(student.getJobKategorien());

        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);
        branche.setValue(student.getBranchen());

        berufsfeld.setItems(berufsFelderRepository.findAll());
        berufsfeld.setItemLabelGenerator(BerufsFelder::getName);
        berufsfeld.setValue(student.getBerufsFelder());

        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setRequired(true);
        standort.setValue(student.getOrte());

        homeOffice.setLabel("Ich bin offen für Home-Office");
        setAlignSelf(Alignment.START, homeOffice);
        homeOffice.setValue(student.isHomeOffice());

        add(berufsbezeichnung, branche, berufsfeld, standort, homeOffice);
    }

    public boolean checkRequirementsAndSave() {
        if (standort.isEmpty()) {
            return false;
        }

        Set<Ort> selected = standort.getSelectedItems();
        student.setHomeOffice(homeOffice.getValue());
        if (!berufsbezeichnung.isEmpty()) {
            student.setJobKategorien(berufsbezeichnung.getSelectedItems());
        }
        if (!branche.isEmpty()) {
            student.setBranchen(branche.getSelectedItems());
        }
        if (!berufsfeld.isEmpty()) {
            student.setBerufsFelder(berufsfeld.getSelectedItems());
        }
        student.setOrte(selected);
        return true;
    }
}
