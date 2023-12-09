package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import java.util.Set;

public class Schritt2View extends RegistrierenSchritt {

    private final BrancheRepository brancheRepository;
    private final TextField name = new TextField("Offizieller Name");
    private final ComboBox<Branche> branche = new ComboBox<>("Branche");
    private final MultiSelectComboBox<Ort> standorte = new MultiSelectComboBox<>("Standorte");
    private final TextArea beschreibung = new TextArea("Beschreibung");
    private Unternehmen unternehmen;
    private final OrtController ortController;

    public Schritt2View(BrancheRepository brancheRepository, OrtController ortController, Unternehmen unternehmen) {
        this.ortController = ortController;
        this.brancheRepository = brancheRepository;
        this.unternehmen = unternehmen;
        insertContent();
    }

    @Override
    public void insertContent() {
        name.setRequired(true);

        branche.setRequired(true);
        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);

        standorte.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standorte.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standorte.setRequired(true);

        beschreibung.setMaxLength(400);
        beschreibung.setHelperText("Maximal 400 Zeichen");

        add(name, branche, standorte, beschreibung);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        unternehmen = new Unternehmen();
        String freitext = beschreibung.getValue();
        String nameUnternehmen = name.getValue();
        Set<Branche> branche1 = Set.of(branche.getValue());
        Set<Ort> unsereStandorte = standorte.getSelectedItems();
        if (!beschreibung.isEmpty()){
            unternehmen.setBeschreibung(freitext);
        }
        if(!name.isEmpty() && !branche.isEmpty() && !standorte.isEmpty()){
            unternehmen.setName(nameUnternehmen);
            unternehmen.setBranchen(branche1);
            unternehmen.setStandorte(unsereStandorte);
            return true;
        }
        return false;
    }
}
