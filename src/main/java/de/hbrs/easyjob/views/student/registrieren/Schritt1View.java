package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import static de.hbrs.easyjob.controllers.ValidationController.*;

@PageTitle("Jeder Anfang ist schwer...")
public class Schritt1View extends RegistrierenSchritt {
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final TextField telefon = new TextField("Telefon");
    private final ComboBox<String> abschluss = new ComboBox<>("(Angestrebter) Abschluss");
    private final ComboBox<Studienfach> studiengang = new ComboBox<>("Studiengang");
    private final StudienfachRepository studienfachRepository;
    private final Student student;

    public Schritt1View(
            StudienfachRepository studienfachRepository,
            Student student
    ) {
        this.studienfachRepository = studienfachRepository;
        this.student = student;
        insertContent();
    }

    public void insertContent() {
        //Textfelder
        vorname.setRequired(true);
        vorname.setId("vorname_registrieren_1");
        if (student.getVorname() != null) {
            vorname.setValue(student.getVorname());
        }

        nachname.setRequired(true);
        nachname.setId("nachname_registrieren_1");
        if (student.getNachname() != null) {
            nachname.setValue(student.getNachname());
        }

        //Combo-Boxen
        abschluss.setItems("Bachelor", "Master");
        abschluss.setValue("Bachelor");
        abschluss.setId("abschluss_combobox_id");
        studiengang.setItems(studienfachRepository.findAllByAbschluss("Bachelor"));
        studiengang.setItemLabelGenerator(Studienfach::getFach);
        studiengang.setId("studienfach_combobox_id");
        abschluss.setRequiredIndicatorVisible(true);
        abschluss.addValueChangeListener(e ->
        {
            if (abschluss.getValue().equals("Bachelor")) {
                studiengang.setItems(studienfachRepository.findAllByAbschluss("Bachelor"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            } else if (abschluss.getValue().equals("Master")) {
                studiengang.setItems(studienfachRepository.findAllByAbschluss("Master"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            }
        });

        studiengang.setRequired(true);

        if (student.getStudienfach() != null) {
            abschluss.setValue(student.getStudienfach().getAbschluss());
            studiengang.setValue(student.getStudienfach());
        }

        add(vorname, nachname, telefon, abschluss, studiengang);
    }

    public boolean checkRequirementsAndSave() {
        String meinVorname = vorname.getValue();
        String meinNachname = nachname.getValue();
        String meineNummer = telefon.getValue();
        Studienfach meinStudiengang = studiengang.getValue();

        //Eingabefelder prüfen
        boolean hasError = false;
        String pflichtFeld = "Bitte füllen Sie dieses Feld aus";
        String falscheEingabe = "Eingabe ungültig";

        if (nachname.isEmpty()) {
            nachname.setErrorMessage(pflichtFeld);
            nachname.setInvalid(true);
            hasError = true;
        } else if (!isValidName(meinNachname)) {
            nachname.setErrorMessage(falscheEingabe);
            nachname.setInvalid(true);
            hasError = true;
        }

        if (studiengang.isEmpty()) {
            studiengang.setErrorMessage(pflichtFeld);
            studiengang.setInvalid(true);
            hasError = true;
        } else if (!isValidStudienfach(meinStudiengang, studienfachRepository)) {
            studiengang.setErrorMessage(falscheEingabe);
            studiengang.setInvalid(true);
            hasError = true;
        }

        if (vorname.isEmpty()) {
            vorname.setErrorMessage(pflichtFeld);
            vorname.setInvalid(true);
            hasError = true;
        } else if (!isValidName(meinVorname)) {
            vorname.setErrorMessage(falscheEingabe);
            vorname.setInvalid(true);
            hasError = true;
        }

        if(!telefon.isEmpty()) {
            if (!isValidTelefonnummer(meineNummer)) {
                telefon.setErrorMessage(falscheEingabe);
                telefon.setInvalid(true);
                hasError = true;
            }else {
                student.setTelefon(meineNummer);
            }
        }

        if(!hasError) {
            student.setVorname(meinVorname);
            student.setNachname(meinNachname);
            student.setStudienfach(meinStudiengang);
        }
        return !hasError;
    }
}
