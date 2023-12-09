package de.hbrs.easyjob.views.unternehmen.registrieren;


import com.vaadin.flow.component.textfield.TextField;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import static de.hbrs.easyjob.controllers.ValidationController.isValidName;
import static de.hbrs.easyjob.controllers.ValidationController.isValidTelefonnummer;


public class Schritt4View extends RegistrierenSchritt {
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final TextField telefon = new TextField("Telefon");
    private final Unternehmensperson unternehmensperson;

    public Schritt4View(Unternehmensperson unternehmensperson) {
        this.unternehmensperson = unternehmensperson;
        insertContent();
    }

    @Override
    public void insertContent() {
        vorname.setRequired(true);
        if (unternehmensperson.getVorname() != null) {
            vorname.setValue(unternehmensperson.getVorname());
        }

        nachname.setRequired(true);
        if (unternehmensperson.getNachname() != null) {
            nachname.setValue(unternehmensperson.getNachname());
        }

        telefon.setRequired(true);
        if (unternehmensperson.getTelefon() != null) {
            telefon.setValue(unternehmensperson.getTelefon());
        }

        add(vorname, nachname, telefon);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        String meinVorname = vorname.getValue();
        String meinNachname = nachname.getValue();
        String meineNummer = telefon.getValue();

        if (vorname.isEmpty() || nachname.isEmpty() || telefon.isEmpty()) {
            return false;
        }

        if (!isValidName(meinNachname) || !isValidName(meinVorname) || !isValidTelefonnummer(meineNummer)) {
            return false;
        }

        unternehmensperson.setVorname(meinVorname);
        unternehmensperson.setNachname(meinNachname);
        unternehmensperson.setTelefon(meineNummer);
        return true;
    }
}
