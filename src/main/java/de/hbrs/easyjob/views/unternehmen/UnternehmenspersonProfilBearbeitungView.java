package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.components.UnternehmenLayout;

import java.io.InputStream;

import static de.hbrs.easyjob.controllers.ValidationController.isValidEmail;


@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
@StyleSheet("UnternehmenRegistrieren.css")
@Route(value = "unternehmen/unternehmenpersonbearbeitung", layout = UnternehmenLayout.class)
public class UnternehmenspersonProfilBearbeitungView extends VerticalLayout {
    // Services
    private final transient PersonService personService;

    // Entities
    private final transient Unternehmensperson person;
    private final transient ComboBox<Ort> ort = new ComboBox<>("Ort");
    private TextField vorname;
    private TextField nachname;
    private TextField email;
    private TextField telefon;
    private Image profilBild2;
    private final VerticalLayout personKontakt = new VerticalLayout();

    // Konstanten
    private static final String CANCEL = "cancel";

    UnternehmenspersonProfilBearbeitungView(SessionController sessionController, PersonService personService) {
        this.personService = personService;
        person = (Unternehmensperson) sessionController.getPerson();
        initialView();
    }

    private void initialView() {
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilBearbeitungView.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");
        UI.getCurrent().getPage().addStyleSheet("Registrieren.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        // Platzhalter Bild
        boolean hasBild = person.getFoto() != null;
        Image platzhalterBild = new Image(hasBild ? person.getFoto() : "images/blank-profile-picture.png", "EasyJob");
        profilBild2 = platzhalterBild;
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        // Bild bearbeiten Button
        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName(CANCEL);

        // Setup Upload
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("image/jpeg", "image/jpg, image/png");
        upload.setUploadButton(bildBearbeiten);
        Div uploadListe = new Div();

        // Profilbild Upload
        upload.addSucceededListener(event -> {
            InputStream fileData = buffer.getInputStream();
            String fileName = event.getFileName();

            // Custom Upload Liste
            Div fileInfo = new Div();
            fileInfo.setText(fileName);
            fileInfo.addClassName("file-info");
            Button remove = new Button(new Icon(VaadinIcon.CLOSE));
            remove.addClassName("remove-file");
            remove.addThemeVariants(ButtonVariant.LUMO_SMALL);
            HorizontalLayout dateiName = new HorizontalLayout(fileInfo, remove);
            dateiName.setAlignItems(Alignment.CENTER);

            // Profilbild entsprechend wechseln
            Image neuesBild = FileUpload.squareImageUpload(fileData, fileName, 242);
            upload.addAllFinishedListener(e -> {
                profilBild2 = neuesBild;
                uploadListe.add(dateiName);
                neuesBild.addClassName("picture-round");
                bildDiv.replace(platzhalterBild, neuesBild);
            });

            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                profilBild2 = platzhalterBild;
                upload.clearFileList();
                bildDiv.replace(neuesBild, platzhalterBild);
                dateiName.removeAll();
                bildBearbeiten.addClassName(CANCEL);
            });
        });

        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);

        //personKontakt
        H2 kon = new H2("Kontakt:");
        kon.addClassName("kon");

        personKontakt.setAlignItems(Alignment.STRETCH);

        vorname = completeZeile("Vorname:", person.getVorname());
        nachname = completeZeile("Nachname:", person.getNachname());
        email = completeZeile("Email:", person.getEmail());
        telefon = completeZeile("Telefon:", person.getTelefon());

        // Buttons
        Button next = new Button("fertig", new Icon(VaadinIcon.ARROW_RIGHT));
        next.addClassName("next");
        next.addClickListener(e -> updateInfos());

        Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
        back.addClassName("back");
        back.addClickListener(e -> UI.getCurrent().getPage().setLocation("/unternehmen/unternehmenperson"));

        Button cancel = new Button("Abbrechen");

        cancel.addClassName(CANCEL);

        // Buttons Container
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);

        personKontakt.add(actions);
        personKontakt.setAlignSelf(Alignment.CENTER, actions);

        personInfo.add(rahmen, upload, uploadListe);

        add(personInfo, kon, personKontakt, ort);
    }

    private TextField completeZeile(String title, String wert) {

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");

        TextField textField = new TextField();
        textField.addClassName("feld");
        textField.setValue(wert);

        wertH.add(textField);
        HorizontalLayout completeZeile = new HorizontalLayout(titleH, wertH);
        completeZeile.setAlignItems(Alignment.STRETCH);
        completeZeile.addClassName("completeZeile");

        personKontakt.add(completeZeile);

        return textField;
    }

    private void updateInfos() {
        person.setVorname(vorname.getValue());
        person.setNachname(nachname.getValue());

        if (isValidEmail(email.getValue())) {
            person.setEmail(email.getValue());
            UI.getCurrent().getPage().setLocation("/unternehmen/unternehmenperson");
        } else {
            Notification.show("falscheEingabe");
        }

        person.setTelefon(telefon.getValue());
        person.setFoto(profilBild2.getSrc());

        personService.savePerson(person);
    }
}
