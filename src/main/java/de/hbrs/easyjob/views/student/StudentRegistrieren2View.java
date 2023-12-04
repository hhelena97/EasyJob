package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.BerufsFelderRepository;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.views.templates.RegistrierenView;

import java.util.Set;

@Route("Student/Registrieren-2")
@PageTitle("Student Registrieren 2")
@StyleSheet("StudentRegistrieren.css")
public class StudentRegistrieren2View extends RegistrierenView {

    private JobKategorieRepository jobKategorieRepository;
    private OrtController ortController;
    private BrancheRepository brancheRepository;
    private BerufsFelderRepository berufsFelderRepository;
    private MultiSelectComboBox<Ort> standort = new MultiSelectComboBox<>("Standorte");
    private MultiSelectComboBox<JobKategorie> berufsbezeichnung = new MultiSelectComboBox<>("Berufsbezeichnung");
    private MultiSelectComboBox<Branche> branche = new MultiSelectComboBox<>("Branche");
    private MultiSelectComboBox<BerufsFelder> berufsfeld = new MultiSelectComboBox<>("Berufsfeld");
    private Checkbox homeOffice = new Checkbox();


    public StudentRegistrieren2View(JobKategorieRepository jobKategorieRepository, BrancheRepository brancheRepository
            , BerufsFelderRepository berufsFelderRepository, OrtController ortController){
        super();
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("Student/Registrieren-1");
        super.setNextView("Student/Registrieren-3");
        super.setHeader("Ich suche...");
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortController = ortController;
        this.brancheRepository = brancheRepository;
        this.berufsFelderRepository = berufsFelderRepository;
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Student");
        super.next.addClickListener(e -> checkRequirementsandSave(berufsbezeichnung, branche, berufsfeld, standort, homeOffice));
    }
    @Override
    public void insertContent() {

        berufsbezeichnung.setItems(jobKategorieRepository.findAll());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);

        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);

        berufsfeld.setItems(berufsFelderRepository.findAll());
        berufsfeld.setItemLabelGenerator(BerufsFelder::getName);

        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setRequired(true);

        homeOffice.setLabel("Ich bin offen für Home-Office");
        setAlignSelf(Alignment.START, homeOffice);

        frame.add(berufsbezeichnung, branche, berufsfeld, standort, homeOffice);
    }

    private void checkRequirementsandSave(MultiSelectComboBox<JobKategorie> berufsbezeichnung, MultiSelectComboBox<Branche> branche,
                                          MultiSelectComboBox<BerufsFelder> berufsfeld, MultiSelectComboBox<Ort> standort,
                                          Checkbox homeOffice){
        super.setNextView("Student/Registrieren-3");
        Set<Ort> selected = standort.getSelectedItems();

        ((Student)super.person).setHomeOffice(homeOffice.getValue());
        if(!berufsbezeichnung.isEmpty()){
            ((Student)super.person).setJobKategorien(berufsbezeichnung.getSelectedItems());
        }
        if(!branche.isEmpty()){
            ((Student)super.person).setBranchen(branche.getSelectedItems());
        }
        if(!berufsfeld.isEmpty()){
            ((Student)super.person).setBerufsFelder(berufsfeld.getSelectedItems());
        }
        if(!standort.isEmpty()){
            ((Student)super.person).setOrte(selected);
            getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
        }
    }
}
