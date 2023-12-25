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
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import java.io.InputStream;

public class Schritt3View extends RegistrierenSchritt {
    Image logo;
    private final Unternehmen unternehmen;
    public Schritt3View(Unternehmen unternehmen) {
        this.unternehmen = unternehmen;
        insertContent();
    }

    @Override
    public void insertContent() {
        setAlignItems(Alignment.CENTER);

        Div rahmen = new Div();
        rahmen.addClassName("logo-frame");

        Image platzhalterLogo = new Image("images/blank-logo.jpeg", "Logo Platzhalter");
        logo = platzhalterLogo;
        Div logoDiv = new Div(platzhalterLogo);
        platzhalterLogo.addClassName("picture-square");
        rahmen.add(logoDiv);

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
            Image neuesLogo = FileUpload.squareImageUpload(fileData, fileName, 242);
            upload.addAllFinishedListener(e ->{
                logo = neuesLogo;
                uploadListe.add(dateiName);
                neuesLogo.addClassName("picture-square");
                logoDiv.replace(platzhalterLogo, neuesLogo);});
            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                logo = platzhalterLogo;
                upload.clearFileList();
                logoDiv.replace(neuesLogo, platzhalterLogo);
                dateiName.removeAll();
                bildBearbeiten.addClassName("cancel");
            });
        });

        add(rahmen, upload, uploadListe);
    }

    @Override
    public boolean checkRequirementsAndSave() {
        unternehmen.setLogo(logo.getSrc());
        return true;
    }
}
