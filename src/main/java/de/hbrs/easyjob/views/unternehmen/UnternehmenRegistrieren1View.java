package de.hbrs.easyjob.views.unternehmen;


import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.RegistrierungsController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;

@Route("Unternehmen/Registrieren-1")
@PageTitle("Unternehmen Registrieren 1")
@StyleSheet("UnternehmenRegistrieren.css")

public class UnternehmenRegistrieren1View extends RegistrierenView {

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private TextField telefon = new TextField("Telefon");
    private final RegistrierungsController registrierungsController;

    public UnternehmenRegistrieren1View(RegistrierungsController registrierungsController){
        super();
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("Unternehmen/Registrieren");
        super.setHeader("Ich bin...");
        insertContent();
        super.addButtons();
        super.setAbbrechenDialog("Unternehmen");
        super.next.addClickListener(e -> checkRequirementsandSave(vorname, nachname, telefon));
        this.registrierungsController = registrierungsController;
    }
    @Override
    public void insertContent() {
        vorname.setRequired(true);
        nachname.setRequired(true);
        telefon.setRequired(true);

        super.frame.add(vorname, nachname, telefon);
    }

    private void checkRequirementsandSave(TextField vorname, TextField nachname, TextField telefon){
        super.setNextView("Unternehmen/Registrieren-2");
        String meinVorname = vorname.getValue();
        String meinNachname = nachname.getValue();
        String meineNummer = telefon.getValue();

        if(!vorname.isEmpty() && !nachname.isEmpty() && !telefon.isEmpty()){
            if(registrierungsController.isValidNachname(meinNachname) && registrierungsController.isValidVorname(meinVorname)
            && registrierungsController.isValidTelefonnummer(meineNummer)){
                super.person.setVorname(meinVorname);
                super.person.setNachname(meinNachname);
                super.person.setTelefon(meineNummer);
                getUI().ifPresent(ui -> ui.navigate(super.getNextView()));
            }

        }
    }
}
