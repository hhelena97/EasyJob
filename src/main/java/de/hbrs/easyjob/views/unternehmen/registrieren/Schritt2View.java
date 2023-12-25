package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
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
    private final TextField strasse = new TextField("Straße und Hausnummer");
    private final ComboBox<Ort> ort = new ComboBox<>("Ort");
    private final ComboBox<Branche> branche = new ComboBox<>("Branche");
    private final MultiSelectComboBox<Ort> standorte = new MultiSelectComboBox<>("Standorte");
    private final TextArea beschreibung = new TextArea("Beschreibung");
    private final Unternehmen unternehmen;
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

        ort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        ort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());

        branche.setRequired(true);
        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);

        standorte.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standorte.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standorte.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        standorte.setRequired(true);

        beschreibung.setRequired(true);
        beschreibung.setHelperText("Maximal 400 Zeichen");
        short charLimit = 400;
        beschreibung.setMaxLength(charLimit);
        beschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        beschreibung.addValueChangeListener(e -> e.getSource()
                .setHelperText(e.getValue().length() + "/" + charLimit));

        add(name, strasse, ort, branche, standorte, beschreibung);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        String freitext = beschreibung.getValue();
        String nameUnternehmen = name.getValue();
        Set<Ort> unsereStandorte = standorte.getSelectedItems();
        String adresseStrasse = strasse.getValue();
        Ort adresseOrt = ort.getValue();

        boolean hasError = false;
        String pflichtFeld = "Bitte füllen Sie dieses Feld aus";

        if (!strasse.isEmpty() && !ort.isEmpty()){
            String adresse = adresseStrasse + ", " + adresseOrt.getPLZ() + " " + adresseOrt.getOrt();
            unternehmen.setKontaktdaten(adresse);
        }
        if (name.isEmpty()) {
            name.setErrorMessage(pflichtFeld);
            name.setInvalid(true);
            hasError = true;
        }
        if (branche.isEmpty()) {
            branche.setErrorMessage(pflichtFeld);
            branche.setInvalid(true);
            hasError = true;
        }
        if (standorte.isEmpty()) {
            standorte.setErrorMessage(pflichtFeld);
            standorte.setInvalid(true);
            hasError = true;
        }
        if (beschreibung.isEmpty()){
            beschreibung.setErrorMessage(pflichtFeld);
            beschreibung.setInvalid(true);
            hasError = true;
        }

        if(!hasError){
            unternehmen.setName(nameUnternehmen);
            unternehmen.setBranchen(Set.of(branche.getValue()));
            unternehmen.setStandorte(unsereStandorte);
            unternehmen.setBeschreibung(freitext);
        }
        return !hasError;
    }
}
