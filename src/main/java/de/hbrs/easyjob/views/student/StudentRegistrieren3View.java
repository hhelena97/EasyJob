package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.control.RegistrierungsControllerStudent;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repository.*;
import de.hbrs.easyjob.views.allgemein.DialogLayout;
import de.hbrs.easyjob.views.allgemein.RegistrierenView;

@Route("Student/Registrieren-3")
@PageTitle("Student Registrieren 3")
@StyleSheet("StudentRegistrieren.css")
@StyleSheet("DialogLayout.css")
public class StudentRegistrieren3View extends RegistrierenView {

    private Image profilBild;
    private StudentRepository studentRepository;
    private StudienfachRepository studienfachRepository;
    private JobKategorieRepository jobKategorieRepository;
    private BrancheRepository brancheRepository;
    private BerufsFeldRepository berufsFeldRepository;
    private OrtRepository ortRepository;
    private DialogLayout finishDialog = new DialogLayout(true);

    public StudentRegistrieren3View(StudentRepository studentRepository,  StudienfachRepository studienfachRepository, JobKategorieRepository jobKategorieRepository, BrancheRepository brancheRepository, BerufsFeldRepository berufsFeldRepository, OrtRepository ortRepository){
        super();
        this.studienfachRepository = studienfachRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.brancheRepository = brancheRepository;
        this.berufsFeldRepository = berufsFeldRepository;
        this.studentRepository = studentRepository;
        this.ortRepository = ortRepository;
        super.person = ComponentUtil.getData(UI.getCurrent(), Person.class);
        super.setLastView("Student/Registrieren-2");
        super.setHeader("Fast geschafft...");
        insertContent();
        super.addFertigButton();
        super.addButtons();
        super.setAbbrechenDialog("Student");
        super.fertig.addClickListener(e -> finishRegistration());
    }
    @Override
    public void insertContent() {

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

        super.frame.add(rahmen, bildBearbeiten);
    }

    private void triggerUpload(Upload upload) {
        Element uploadElement = upload.getElement();
        uploadElement.executeJs("this.querySelector('[type=\"file\"]').click();");
    }

    private void finishRegistration() {
        RegistrierungsControllerStudent registrierungsControllerStudent = new RegistrierungsControllerStudent(studentRepository,
                studienfachRepository, jobKategorieRepository, brancheRepository, berufsFeldRepository, ortRepository);
        super.person.setFoto(profilBild.getSrc());
        registrierungsControllerStudent.createStudent(((Student)super.person), true);
        Button weiterZumLogin = new Button("Weiter zum Login");
        weiterZumLogin.addClassName("close-student");
        finishDialog.simpleDialog("Account erfolgreich angelegt!", weiterZumLogin, "login");
        finishDialog.openDialogOverlay();
    }
}
