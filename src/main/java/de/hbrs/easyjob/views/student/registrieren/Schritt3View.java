package de.hbrs.easyjob.views.student.registrieren;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.views.templates.RegistrierenSchritt;

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

        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");

        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        profilBild = new Image("images/blank-profile-picture.png", "Profilbild Platzhalter");
        Div bildDiv = new Div(profilBild);
        profilBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        Button bildBearbeiten = new Button("Bild bearbeiten(optional)", new Icon(VaadinIcon.PENCIL));
        bildBearbeiten.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bildBearbeiten.addClassName("cancel");

        /*
        Upload upload = new Upload(new FileBuffer());

        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/*");
        Image uploadedImage = new Image();


        bildBearbeiten.addClickListener(e -> triggerUpload(upload));

        upload.addSucceededListener(event -> {
            InputStream inputStream = ((FileBuffer) upload.getReceiver()).getInputStream();
            try {
                uploadedImage.getElement().executeJs("this.src = URL.createObjectURL($0);", new Object[]{inputStream});
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

         */

        add(rahmen, bildBearbeiten);
    }

    public boolean checkRequirementsAndSave() {
        student.setFoto(profilBild.getSrc());
        return true;
    }
}
