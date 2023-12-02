package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.ControllerRegS1;
import de.hbrs.easyjob.control.RegistrierungsController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;

@Route("Student/Registrieren-1")
@PageTitle("Student Registrieren 1")
@StyleSheet("StudentRegistrieren.css")
public class StudentRegistrieren1View extends RegistrierenView {

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private TextField telefon = new TextField("Telefon");
    private Select<String> abschluss = new Select<>("Bachelor", "Master");
    private ComboBox<Studienfach> studiengang = new ComboBox<>("Studiengang");
    private final ControllerRegS1 controllerRegS1;
    private final RegistrierungsController registrierungsController;

    public StudentRegistrieren1View(ControllerRegS1 controllerRegS1, RegistrierungsController registrierungsController){
        super();
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("/Registrieren");
        super.setHeader("Ich bin...");
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Student");
        super.next.addClickListener(e -> checkRequirementsAndSave(vorname, nachname, telefon, studiengang));
        this.controllerRegS1 = controllerRegS1;
        this.registrierungsController = registrierungsController;
    }

    @Override
    public void insertContent() {
        //Textfelder
        vorname.setRequired(true);
        nachname.setRequired(true);

        //Combo-Boxen

        abschluss.setLabel("(Angestrebter) Abschluss");
        abschluss.setRequiredIndicatorVisible(true);
        abschluss.addValueChangeListener(e ->
            {if (abschluss.getValue().equals("Bachelor")){
                studiengang.setItems(controllerRegS1.getStudienfachNachAbschluss("Bachelor"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            } else if (abschluss.getValue().equals("Master")) {
                studiengang.setItems(controllerRegS1.getStudienfachNachAbschluss("Master"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            }
            });

        studiengang.setRequired(true);

        super.frame.add(vorname, nachname, telefon, abschluss, studiengang);
    }

    private void checkRequirementsAndSave(TextField vorname, TextField nachname, TextField telefon,
                                           ComboBox<Studienfach> studiengang){
        super.setNextView("Student/Registrieren-2");
        String meinVorname = vorname.getValue();
        String meinNachname = nachname.getValue();
        String meineNummer = telefon.getValue();
        Studienfach meinStudiengang = studiengang.getValue();

        if(!vorname.isEmpty() && !nachname.isEmpty() && !studiengang.isEmpty()){

            if(!telefon.isEmpty() && registrierungsController.isValidTelefonnummer(meineNummer)){
            super.person.setTelefon(meineNummer);
            }
            if(registrierungsController.isValidNachname(meinNachname)){
                super.person.setVorname(meinVorname);
                super.person.setNachname(meinNachname);
                ((Student) super.person).setStudienfach(meinStudiengang);
                getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
            }

        }
    }
}
