package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.registrieren.UnternehmenRegistrierenController;
import de.hbrs.easyjob.controllers.registrieren.UnternehmenspersonRegistrierenController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import static de.hbrs.easyjob.controllers.ValidationController.isValidEmail;
import static de.hbrs.easyjob.controllers.ValidationController.isValidPassword;

@Route("registrieren")
@PageTitle("Registrieren")
@StyleSheet("Variables.css")
@StyleSheet("Registrieren.css")
public class AlleRegistrierenView extends RegistrierenSchritt {
    // Controller
    private final OrtController ortController;
    private final UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController;
    private final UnternehmenRegistrierenController unternehmenRegistrierenController;

    // Entities
    private Person person;

    // Repositories
    private final BerufsFeldRepository berufsFeldRepository;
    private final BrancheRepository brancheRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final OrtRepository ortRepository;
    private final PersonRepository personRepository;
    private final StudienfachRepository studienfachRepository;
    private final UnternehmenRepository unternehmenRepository;

    // Components
    private final Button register = new Button("Jetzt Registrieren", new Icon(VaadinIcon.ARROW_RIGHT));
    private final Button cancel = new Button("Abbrechen");
    private final RadioButtonGroup<String> wahl = new RadioButtonGroup<>();
    private final TextField mail = new TextField("E-Mail");
    private final PasswordField pw1 = new PasswordField("Passwort erstellen");
    private final PasswordField pw2 = new PasswordField("Passwort wiederholen");
    private final Checkbox checkbox = new Checkbox();
    private final VerticalLayout frame = new VerticalLayout();


    public AlleRegistrierenView(
            BerufsFeldRepository berufsFeldRepository,
            BrancheRepository brancheRepository,
            JobKategorieRepository jobKategorieRepository,
            OrtRepository ortRepository,
            PersonRepository personRepository,
            StudienfachRepository studienfachRepository,
            UnternehmenRepository unternehmenRepository,
            OrtController ortController,
            UnternehmenspersonRegistrierenController unternehmenspersonRegistrierenController,
            UnternehmenRegistrierenController unternehmenRegistrierenController
    ) {
        this.berufsFeldRepository = berufsFeldRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortRepository = ortRepository;
        this.personRepository = personRepository;
        this.studienfachRepository = studienfachRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.ortController = ortController;
        this.unternehmenspersonRegistrierenController = unternehmenspersonRegistrierenController;
        this.unternehmenRegistrierenController = unternehmenRegistrierenController;

        // Layout
        addClassName("body");
        frame.addClassName("container");
        frame.setAlignItems(Alignment.CENTER);
        frame.setAlignSelf(Alignment.CENTER);

        // Header
        H1 header = new H1("Registrieren");
        header.addClassName("header");
        frame.add(header);

        // Main Content
        insertContent();

        // Register Button
        register.addClickListener(e -> checkRequirementsAndSave());
        register.addClassName("register");

        // Cancel Button
        cancel.addClassName("cancel-register");
        cancel.addClickListener(e -> cancel.getUI().ifPresent(ui -> ui.navigate("login")));

        frame.add(register, cancel);
        add(frame);
    }

    @Override
    public void insertContent() {
        wahl.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        wahl.setItems("Ich studiere.", "Ich vertrete ein Unternehmen.");
        wahl.addClassName("alle-registrieren");

        mail.setRequired(true);
        mail.addValueChangeListener(event -> enableRegisterButton());
        mail.setId("email_registrieren_id");
        pw1.setRequired(true);
        pw1.setHelperText("Mind. 8 Zeichen, mind. 1 Großbuchstabe, mind. 1 Sonderzeichen");
        pw1.setId("passwort1_registrieren_id");
        pw1.addValueChangeListener(event -> enableRegisterButton());
        pw2.setRequired(true);
        pw2.setId("passwort2_registrieren_id");
        pw2.addValueChangeListener(event -> enableRegisterButton());

        checkbox.getStyle().set("--lumo-contrast-20pct","var(--icon-hellgrau)");
        checkbox.getStyle().set("--lumo-primary-color","var(--studierende-dark)");
        checkbox.setId("checkbox_agbs_registrieren_id");
        checkbox.addValueChangeListener(event -> enableRegisterButton());

        Button agbs = new Button("AGB und Datenschutzerklärung akzeptieren");
        agbs.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        agbs.addClassName("cancel");
        agbs.getStyle().set("color", "var(--studierende-dark)");
        agbs.addClickListener((e -> agbs.getUI().ifPresent(ui -> ui.navigate("agb"))));

        HorizontalLayout agbHorizontalLayout = new HorizontalLayout(checkbox, agbs);
        agbHorizontalLayout.setSpacing(false);
        setAlignSelf(Alignment.CENTER, checkbox);
        setAlignSelf(Alignment.CENTER, agbs);
        setAlignSelf(Alignment.START, agbHorizontalLayout);

        frame.add(wahl, mail, pw1, pw2, agbHorizontalLayout);
    }

    private void enableRegisterButton() {
        boolean allClear = !mail.isEmpty() && !pw1.isEmpty() && !pw2.isEmpty() && checkbox.getValue();
        if (allClear) {
            register.addClassName("register-checked");
        } else {
            register.removeClassName("register-checked");
        }
    }

    private void setData(String email, String password) {
        this.person.setEmail(email);
        this.person.setPasswort(password);

    }

    @Override
    public boolean checkRequirementsAndSave() {
        String user = wahl.getValue();
        String email = mail.getValue();
        String password = pw1.getValue();
        String passwordConfirmation = pw2.getValue();

        //Eingaben prüfen
        boolean hasError = false;
        String pflichtFeld = "Bitte füllen Sie dieses Feld aus";
        String falscheEingabe = "Eingabe ungültig";
        String ungleichesPasswort = "Eingaben stimmen nicht überein";
        if (!checkbox.getValue()) {
            checkbox.getStyle().set("border", "1px solid red");
            hasError = true;
        } else {
        checkbox.getStyle().remove("border");
        }

        if (wahl.isEmpty()) {
            wahl.setErrorMessage("Bitte wählen Sie eine der Optionen");
            wahl.setInvalid(true);
            hasError = true;
        } else {
            wahl.setInvalid(false);
        }

        if (mail.isEmpty()){
            mail.setErrorMessage(pflichtFeld);
            mail.setInvalid(true);
            hasError = true;
        }else if (!isValidEmail(email)) {
            mail.setErrorMessage(falscheEingabe);
            mail.setInvalid(true);
            hasError = true;
        }

        if (pw1.isEmpty()){
            pw1.setErrorMessage(pflichtFeld);
            pw1.setInvalid(true);
            hasError = true;
        }else if (!isValidPassword(password)) {
            pw1.setErrorMessage(falscheEingabe);
            pw1.setInvalid(true);
            hasError = true;
        }

        if (pw2.isEmpty()){
            pw2.setErrorMessage(pflichtFeld);
            pw2.setInvalid(true);
            hasError = true;
        }else if (!password.equals(passwordConfirmation)) {
            pw1.setErrorMessage(ungleichesPasswort);
            pw1.setInvalid(true);
            pw2.setErrorMessage(ungleichesPasswort);
            pw2.setInvalid(true);
            hasError = true;
        }

        if(hasError){
            return false;
        }

        if (user.equals("Ich studiere.")) {
            this.person = new Student();
            setData(email, password);
            // Load StudentenView
            removeAll();
            add(new de.hbrs.easyjob.views.student.registrieren.RegistrierenView(
                    this.berufsFeldRepository,
                    this.brancheRepository,
                    this.jobKategorieRepository,
                    this.ortRepository,
                    this.personRepository,
                    this.studienfachRepository,
                    this.ortController,
                    (Student) this.person
            ));
            return true;
        } else if (user.equals("Ich vertrete ein Unternehmen.")) {
            this.person = new Unternehmensperson();
            setData(email, password);
            removeAll();
            add(new de.hbrs.easyjob.views.unternehmen.registrieren.RegistrierenView(
                    this.unternehmenspersonRegistrierenController,
                    this.unternehmenRegistrierenController,
                    this.ortController,
                    this.brancheRepository,
                    this.unternehmenRepository,
                    (Unternehmensperson) this.person
            ));
            return true;
        } else {
            return false;
        }
    }
}
