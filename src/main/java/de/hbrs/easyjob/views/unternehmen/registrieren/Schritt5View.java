package de.hbrs.easyjob.views.unternehmen.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import java.io.InputStream;

public class Schritt5View extends RegistrierenSchritt {
    private Image profilBild;
    private final Unternehmensperson unternehmensperson;

    public Schritt5View(Unternehmensperson unternehmensperson) {
        this.unternehmensperson = unternehmensperson;
        insertContent();
    }

    @Override
    public void insertContent() {
        setAlignItems(Alignment.CENTER);

        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        Image platzhalter = new Image("images/blank-profile-picture.png", "Profilbild Platzhalter");
        profilBild = platzhalter;
        Div bildDiv = new Div(platzhalter);
        bildDiv.addClassName("picture-round");
        rahmen.add(bildDiv);

        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        //Setup Upload
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("image/jpeg", "image/jpg, image/png");
        upload.setUploadButton(bildBearbeiten);
        Div uploadListe = new Div();

        //Profilbild Upload
        upload.addSucceededListener(event -> {
            InputStream fileData = buffer.getInputStream();
            String fileName = event.getFileName();

            //Custom Upload Liste
            Div fileInfo = new Div();
            fileInfo.setText(fileName);
            fileInfo.addClassName("file-info");
            Button remove = new Button(new Icon(VaadinIcon.CLOSE));
            remove.addClassName("remove-file");
            remove.addThemeVariants(ButtonVariant.LUMO_SMALL);
            HorizontalLayout dateiName = new HorizontalLayout(fileInfo, remove);
            dateiName.setAlignItems(Alignment.CENTER);

            //Profilbild entsprechend wechseln
            Image neuesBild = FileUpload.squareImageUpload(fileData, fileName, 242);
            upload.addAllFinishedListener(e ->{
                profilBild = neuesBild;
                uploadListe.add(dateiName);
                neuesBild.addClassName("picture-round");
                bildDiv.replace(platzhalter, neuesBild);});
            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                profilBild = platzhalter;
                upload.clearFileList();
                bildDiv.replace(neuesBild, platzhalter);
                dateiName.removeAll();
                bildBearbeiten.addClassName("cancel");
            });
        });

        add(rahmen, upload, uploadListe);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        unternehmensperson.setFoto(profilBild.getSrc());
        return true;
    }
}
