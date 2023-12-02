package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.OrtControl;
import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repository.BrancheRepository;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;

import java.util.Set;

@Route("Unternehmen/Neu-1")
@PageTitle("Unternehmen anlegen 1")
@StyleSheet("UnternehmenRegistrieren.css")
public class UnternehmenAnlegen1View extends RegistrierenView {

    private BrancheRepository brancheRepository;
    private TextField name = new TextField("Offizieller Name");
    private ComboBox<Branche> branche = new ComboBox<>("Branche");
    private MultiSelectComboBox<Ort> standorte = new MultiSelectComboBox<>("Standorte");
    private TextArea beschreibung = new TextArea("Beschreibung");
    private Unternehmen unternehmen = new Unternehmen();
    private OrtControl ortControl;

    public UnternehmenAnlegen1View(BrancheRepository brancheRepository, OrtControl ortControl) {
        super();
        this.ortControl = ortControl;
        this. brancheRepository = brancheRepository;
        super.setLastView("Unternehmen/Registrieren");
        super.setHeader("Über das Unternehmen...");
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Unternehmen");
        super.next.addClickListener(e -> checkRequirementsandSave(name, branche, standorte, beschreibung));
    }

    @Override
    public void insertContent() {
        name.setRequired(true);

        branche.setRequired(true);
        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);

        standorte.setItems(ortControl.getOrtItemFilter(), ortControl.getAlleOrte());
        standorte.setItemLabelGenerator(ortControl.getOrtItemLabelGenerator());
        standorte.setRequired(true);

        beschreibung.setMaxLength(400);
        beschreibung.setHelperText("Maximal 400 Zeichen");

        super.frame.add(name, branche, standorte, beschreibung);
    }

    private void checkRequirementsandSave(TextField name, ComboBox<Branche> branche, MultiSelectComboBox<Ort> standorte
            , TextArea beschreibung){
        super.setNextView("Unternehmen/Neu-2");
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
            ComponentUtil.setData(UI.getCurrent(), Unternehmen.class, unternehmen);
            getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
        }
    }
}
