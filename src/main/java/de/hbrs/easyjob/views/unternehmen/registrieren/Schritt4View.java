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

        if (vorname.isEmpty()) {
            vorname.setErrorMessage(pflichtFeld);
            vorname.setInvalid(true);
            hasError = true;
        } else if (!isValidName(meinVorname)) {
            vorname.setErrorMessage(falscheEingabe);
            vorname.setInvalid(true);
            hasError = true;
        }

        if (telefon.isEmpty()) {
            telefon.setErrorMessage(pflichtFeld);
            telefon.setInvalid(true);
            hasError = true;
        } else if (!isValidTelefonnummer(meineNummer)) {
            telefon.setErrorMessage(falscheEingabe);
            telefon.setInvalid(true);
            hasError = true;
        }

        if(!hasError) {
            unternehmensperson.setVorname(meinVorname);
            unternehmensperson.setNachname(meinNachname);
            unternehmensperson.setTelefon(meineNummer);
        }
        return !hasError;
    }
}
