package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.UnternehmenRepository;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;


public class Schritt1View extends RegistrierenSchritt {
    private final RadioButtonGroup<String> unternehmenAnlegen = new RadioButtonGroup<>();
    private final ComboBox<Unternehmen> unternehmenComboBox = new ComboBox<>();
    private final UnternehmenRepository unternehmenRepository;
    private final Unternehmensperson unternehmensperson;
    private Unternehmen unternehmen;

    public Schritt1View(
            UnternehmenRepository unternehmenRepository,
            Unternehmensperson unternehmensperson,
            Unternehmen unternehmen
    ) {
        this.unternehmen = unternehmen;
        this.unternehmensperson = unternehmensperson;
        this.unternehmenRepository = unternehmenRepository;
        insertContent();
    }


    @Override
    public void insertContent() {
        unternehmenAnlegen.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        unternehmenAnlegen.setItems("Nein, ich möchte ein neues Unternehmensprofil anlegen.", "Ja, ich gehöre zu:");

        unternehmenComboBox.addClassName("unternehmen-auswählen");

        unternehmenComboBox.setItems(unternehmenRepository.findAll());
        unternehmenComboBox.setItemLabelGenerator(Unternehmen::getName);
        unternehmenComboBox.setEnabled(false);

        unternehmenAnlegen.addValueChangeListener(e -> enableComboBox(unternehmenAnlegen, unternehmenComboBox));

        VerticalLayout contents = new VerticalLayout(unternehmenAnlegen, unternehmenComboBox);
        contents.setSpacing(false);
        setAlignSelf(Alignment.END, unternehmenComboBox);
        setAlignSelf(Alignment.START, contents);

        add(contents);
    }

    private void enableComboBox(RadioButtonGroup<String> unternehmenAnlegen, ComboBox<Unternehmen> unternehmen) {
        String selection = unternehmenAnlegen.getValue();
        if (selection.equals("Ja, ich gehöre zu:")) {
            unternehmen.setEnabled(true);
        } else {
            unternehmen.clear();
            unternehmen.setEnabled(false);
        }
    }

    @Override
    public boolean checkRequirementsAndSave() {
        String choice = unternehmenAnlegen.getValue();
        Unternehmen selected = unternehmenComboBox.getValue();
        if (!unternehmenAnlegen.isEmpty()) {
            if (choice.equals("Ja, ich gehöre zu:")) {
                if (!unternehmenComboBox.isEmpty()) {
                    unternehmensperson.setUnternehmen(selected);
                    unternehmen = selected;
                    return true;
                }
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
