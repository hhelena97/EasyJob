package de.hbrs.easyjob.views.unternehmen.registrieren;


import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import static de.hbrs.easyjob.controllers.ValidationController.isValidName;
import static de.hbrs.easyjob.controllers.ValidationController.isValidTelefonnummer;


public class Schritt4View extends RegistrierenSchritt {
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final TextField telefon = new TextField("Telefon");
    private final TextField strasse = new TextField("Straße und Hausnummer");
    private final ComboBox<Ort> ort = new ComboBox<>("Ort");
    private final OrtController ortController;
    private final Unternehmensperson unternehmensperson;

    public Schritt4View(OrtController ortController, Unternehmensperson unternehmensperson) {
        this.ortController = ortController;
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

        H4 buero = new H4("Büroadresse:");
        buero.getStyle().set("font-weight", "400");
        buero.getStyle().set("color", "var(--unternehmen-dark");

        ort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        ort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());

        add(vorname, nachname, telefon, buero, strasse, ort);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        String meinVorname = vorname.getValue();
        String meinNachname = nachname.getValue();
        String meineNummer = telefon.getValue();
        String adresseStrasse = strasse.getValue();
        Ort adresseOrt = ort.getValue();

        //Eingabefelder prüfen
        boolean hasError = false;
        String pflichtFeld = "Bitte füllen Sie dieses Feld aus";
        String falscheEingabe = "Eingabe ungültig";

        if (!strasse.isEmpty() && !ort.isEmpty()){
            String adresse = adresseStrasse + ", " + adresseOrt.getPLZ() + " " + adresseOrt.getOrt();
            unternehmensperson.setAnschrift(adresse);
        }
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
