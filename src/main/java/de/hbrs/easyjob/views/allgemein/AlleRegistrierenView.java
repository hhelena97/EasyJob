package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.RegistrierungsController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;

@Route("/Registrieren")
@PageTitle("Registrieren")
@StyleSheet("Variables.css")
@StyleSheet("Registrieren.css")
public class AlleRegistrierenView extends RegistrierenView{

    private Button register = new Button("Jetzt Registrieren", new Icon(VaadinIcon.ARROW_RIGHT));
    private Button cancel = new Button("Abbrechen");
    private RadioButtonGroup<String> wahl = new RadioButtonGroup<>();
    private TextField mail = new TextField("E-Mail");
    private TextField pw1 = new TextField("Passwort erstellen");
    private TextField pw2 = new TextField("Passwort wiederholen");
    private Checkbox checkbox = new Checkbox();
    private final RegistrierungsController registrierungsController;

    public AlleRegistrierenView(RegistrierungsController registrierungsController){
        super();
        super.setHeader("Registrieren");
        insertContent();
        super.addButtons();
        super.registerButtons(register, cancel);
        register.addClickListener(e -> checkRequirementsAndSave(wahl, mail, pw1, pw2, checkbox));
        this.registrierungsController = registrierungsController;
    }

    @Override
    public void insertContent() {

        wahl.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        wahl.setItems("Ich studiere.", "Ich vertrete ein Unternehmen.");
        wahl.addClassName("alle-registrieren");

        mail.setRequired(true);

        pw1.setRequired(true);

        pw2.setRequired(true);


        checkbox.addClassName("alle-registrieren");
        checkbox.addValueChangeListener(event -> {
            boolean isChecked = event.getValue();
            if (isChecked) {
                register.addClassName("register-checked");
            } else {
                register.removeClassName("register-checked");
            }
        });

        Button agbs = new Button("AGB und Datenschutzerklärung akzeptieren");
        agbs.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        agbs.addClassName("cancel");
        agbs.getStyle().set("color", "var(--studierende-dark)");
        agbs.addClickListener((e -> agbs.getUI().ifPresent(ui -> ui.navigate("Allgemeine-Geschäftsbedingungen"))));

        HorizontalLayout agbHorizontalLayout = new HorizontalLayout(checkbox, agbs);
        agbHorizontalLayout.setSpacing(false);
        //agbHorizontalLayout.setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER, checkbox);
        setAlignSelf(Alignment.CENTER, agbs);
        setAlignSelf(Alignment.START, agbHorizontalLayout);

        super.frame.add(wahl, mail, pw1, pw2, agbHorizontalLayout);
    }


    private void checkRequirementsAndSave(RadioButtonGroup<String> wahl, TextField mail, TextField pw1, TextField pw2, Checkbox checkbox){

        String user = wahl.getValue();
        Boolean checked = checkbox.getValue();

        if(checked && user.equals("Ich studiere.")){
            super.person = new Student();
            super.setNextView("Student/Registrieren-1");
        } else if (checked && user.equals("Ich vertrete ein Unternehmen.")) {
            super.person = new Unternehmensperson();
            super.setNextView("Unternehmen/Registrieren");
        }

        String meineMail = mail.getValue();
        String meinPasswort = pw1.getValue();
        String meinPasswort2 =  pw2.getValue();

        if(!wahl.isEmpty() && !mail.isEmpty() && !pw1.isEmpty() && !pw2.isEmpty()){
            if (registrierungsController.isValidEmail(meineMail) && registrierungsController.isValidPassword(meinPasswort)
            && registrierungsController.isValidPassword(meinPasswort2)){
                if (registrierungsController.isPasswordTheSame(meinPasswort, meinPasswort2)){
                    super.person.setEmail(meineMail);
                    super.person.setPasswort(meinPasswort);
                    ComponentUtil.setData(UI.getCurrent(), Person.class, super.person);
                    getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
                }
            }
        }
    }
}
