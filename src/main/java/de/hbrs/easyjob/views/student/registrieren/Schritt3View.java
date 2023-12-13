package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

import java.io.InputStream;

@PageTitle("Fast geschafft...")
public class Schritt3View extends RegistrierenSchritt {
    private Image profilBild;
    private final Student student;

    public Schritt3View(Student student) {
        this.student = student;
        insertContent();
    }

    public void insertContent() {
        setAlignItems(Alignment.CENTER);

        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        Image platzhalterBild = new Image("images/blank-profile-picture.png", "Profilbild Platzhalter");
        profilBild = platzhalterBild;
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        //Bild bearbeiten Button
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
                bildDiv.replace(platzhalterBild, neuesBild);});
            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                profilBild = platzhalterBild;
                upload.clearFileList();
                bildDiv.replace(neuesBild, platzhalterBild);
                dateiName.removeAll();
                bildBearbeiten.addClassName("cancel");
            });
        });

        add(rahmen, upload, uploadListe);
    }

    public boolean checkRequirementsAndSave() {
        student.setFoto(profilBild.getSrc());
        return true;
    }
}
