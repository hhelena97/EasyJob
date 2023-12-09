package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import static de.hbrs.easyjob.controllers.ValidationController.isValidName;
import static de.hbrs.easyjob.controllers.ValidationController.isValidTelefonnummer;

@PageTitle("Jeder Anfang ist schwer...")
public class Schritt1View extends RegistrierenSchritt {
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final TextField telefon = new TextField("Telefon");
    private final Select<String> abschluss = new Select<>();
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
        if (student.getVorname() != null) {
            vorname.setValue(student.getVorname());
        }

        nachname.setRequired(true);
        if (student.getNachname() != null) {
            nachname.setValue(student.getNachname());
        }

        //Combo-Boxen
        abschluss.setLabel("(Angestrebter) Abschluss");
        abschluss.setItems("Bachelor", "Master");
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

        if (vorname.isEmpty() || nachname.isEmpty() || studiengang.isEmpty() || telefon.isEmpty()) {
            return false;
        }

        if (!isValidTelefonnummer(meineNummer)) {
            return false;
        }
        student.setTelefon(meineNummer);

        if (!isValidName(meinNachname)) {
            return false;
        }
        student.setVorname(meinVorname);
        student.setNachname(meinNachname);
        student.setStudienfach(meinStudiengang);

        return true;
    }
}
