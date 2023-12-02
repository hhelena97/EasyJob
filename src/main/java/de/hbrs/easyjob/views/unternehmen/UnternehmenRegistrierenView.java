package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;

@Route("Unternehmen/Registrieren")
@PageTitle("Unternehmen Registrieren")
@StyleSheet("UnternehmenRegistrieren.css")
public class UnternehmenRegistrierenView extends RegistrierenView {

    private UnternehmenRepository unternehmenRepository;
    private RadioButtonGroup<String> unternehmenAnlegen = new RadioButtonGroup<>();
    private ComboBox<Unternehmen> unternehmen = new ComboBox<>();
    public UnternehmenRegistrierenView(UnternehmenRepository unternehmenRepository){
        super();
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("Registrieren");
        super.setHeader("Ist Ihr Unternehmen bereits bei EasyJob registriert?");
        this.unternehmenRepository = unternehmenRepository;
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Unternehmen");
        super.next.addClickListener(e -> checkRequirementsandSave(unternehmenAnlegen, unternehmen));
    }


    @Override
    public void insertContent() {
        
        unternehmenAnlegen.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        unternehmenAnlegen.setItems("Nein, ich möchte ein neues Unternehmensprofil anlegen.", "Ja, ich gehöre zu:");

        unternehmen.addClassName("unternehmen-auswählen");

        unternehmen.setItems(unternehmenRepository.findAll());
        unternehmen.setItemLabelGenerator(Unternehmen::getName);
        unternehmen.setEnabled(false);

        unternehmenAnlegen.addValueChangeListener(e -> enableComboBox(unternehmenAnlegen, unternehmen));

        VerticalLayout contents = new VerticalLayout(unternehmenAnlegen,  unternehmen);
        contents.setSpacing(false);
        setAlignSelf(Alignment.END, unternehmen);
        setAlignSelf(Alignment.START, contents);

        super.frame.add(contents);
    }

    private void enableComboBox(RadioButtonGroup<String> unternehmenAnlegen, ComboBox<Unternehmen> unternehmen){
        String selection = unternehmenAnlegen.getValue();
        if(selection.equals("Ja, ich gehöre zu:")){
            unternehmen.setEnabled(true);
        }
        else{
            unternehmen.clear();
            unternehmen.setEnabled(false);
        }
    }

    private void checkRequirementsandSave(RadioButtonGroup<String> unternehmenAnlegen, ComboBox<Unternehmen> unternehmen){
        
        String choice = unternehmenAnlegen.getValue();
        Unternehmen selected = unternehmen.getValue();
        if(!unternehmenAnlegen.isEmpty()){
            if(choice.equals("Ja, ich gehöre zu:") && !unternehmen.isEmpty()){
                ComponentUtil.setData(UI.getCurrent(), Unternehmen.class, selected);
                ComponentUtil.setData(UI.getCurrent(), Boolean.class, false);
                super.setNextView("Unternehmen/Registrieren-1");
            }else{
                ComponentUtil.setData(UI.getCurrent(), Boolean.class, true);
                super.setNextView("Unternehmen/Neu-1");
            }
            getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
        }
    }
}
