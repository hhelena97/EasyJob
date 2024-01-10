package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.BerufsFeldRepository;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.FileUpload;

import java.io.InputStream;

@StyleSheet("StudentProfilView.css")
public class StudentProfileComponentBearbeitung extends VerticalLayout {
    private Student person;

    private  Tab allgemein;
    private  Tab kenntnisse;
    private  Tab ueberMich;

    TextField vorname;
    TextField nachname;
    ComboBox<String> abschluss = new ComboBox<>();
    ComboBox<Studienfach> studiengang  = new ComboBox<>();

    MultiSelectComboBox<Ort> standort = new MultiSelectComboBox<>();
    MultiSelectComboBox<JobKategorie> berufsbezeichnung= new MultiSelectComboBox<>();
    MultiSelectComboBox<Branche> branche= new MultiSelectComboBox<>();
    MultiSelectComboBox<BerufsFelder> berufsfeld= new MultiSelectComboBox<>();
    private  VerticalLayout content;

    Div allgemeinDiv;
    private final String style;

    private final StudentService personService;
    StudienfachRepository studienfachRepository;
    BerufsFeldRepository berufsFeldRepository;
    BrancheRepository brancheRepository;
    JobKategorieRepository jobKategorieRepository;
    OrtController ortController;

    Image profilBild2;
    VerticalLayout personKontakt = new VerticalLayout();


    public StudentProfileComponentBearbeitung(Student student,
                                              String styleClass,
                                              StudentService studentService,
                                              StudienfachRepository studienfachRepository,
                                              BerufsFeldRepository berufsFelderRepository,
                                              BrancheRepository brancheRepository,
                                              JobKategorieRepository jobKategorieRepository,
                                              OrtController ortController

                                    ) {
        this.person = student;
        this.personService = studentService;
        this.studienfachRepository = studienfachRepository;
        this.berufsFeldRepository = berufsFelderRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortController = ortController;
        style=styleClass;

        UI.getCurrent().getPage().addStyleSheet("StudentProfilView.css");

        initializeComponent();
    }

    private void initializeComponent() {

        if (person == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }

        UI.getCurrent().getPage().addStyleSheet(style);

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);




        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilBearbeitungView.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");
        UI.getCurrent().getPage().addStyleSheet("Registrieren.css");
        UI.getCurrent().getPage().addStyleSheet("UnternehmenspersonProfilView.css");


        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);


        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        boolean hasBild = person.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? person.getFoto(): "images/blank-profile-picture.png", "EasyJob");
        profilBild2 = platzhalterBild;
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
                profilBild2 = neuesBild;
                uploadListe.add(dateiName);
                neuesBild.addClassName("picture-round");
                bildDiv.replace(platzhalterBild, neuesBild);});
            bildBearbeiten.addClassName("cancel-disabled");
            remove.addClickListener(e -> {
                profilBild2 = platzhalterBild;
                upload.clearFileList();
                bildDiv.replace(neuesBild, platzhalterBild);
                dateiName.removeAll();
                bildBearbeiten.addClassName("cancel");
            });
        });




        //Person Info
        VerticalLayout personInfo = new VerticalLayout();
        personInfo.addClassName("personInfo");
        personInfo.setAlignItems(Alignment.CENTER);












        // Buttons
        Button next = new Button("fertig", new Icon(VaadinIcon.ARROW_RIGHT));
        next.addClassName("next");
        next.addClickListener(e -> updateInfos());

        Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
        back.addClassName("back");
        back.addClickListener(e -> UI.getCurrent().getPage().setLocation("/student"));

        Button cancel = new Button("Abbrechen");
        //cancel.addClickListener(e -> abbrechenDialog.openDialogOverlay());
        cancel.addClassName("cancel");

        // Buttons Container
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);

        personKontakt.add(actions);
        personKontakt.setAlignSelf(Alignment.CENTER,actions);



        VerticalLayout studentInfo = new VerticalLayout();
        studentInfo.addClassName("studentInfo");
        studentInfo.setAlignItems(Alignment.CENTER);




        //Tabs
        allgemein = new Tab("Allgemein");
        kenntnisse = new Tab("Kenntnisse");
        ueberMich = new Tab("Über mich");

        Tabs tabs = new Tabs(allgemein, kenntnisse, ueberMich);
        tabs.addSelectedChangeListener(
                event -> { setContent(event.getSelectedTab());
                });

        content = new VerticalLayout();
        //content.setSpacing(false);
        // content.setPadding(false);
        content.setWidth("100%");
        content.setAlignItems(Alignment.STRETCH);

        setContent(tabs.getSelectedTab());


        studentInfo.add(rahmen,upload,uploadListe,/*location,*/tabs, content, personKontakt);


        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();


        allgemeinDiv = new Div();
        allgemeinDiv.addClassName("myTab");

        vorname = completeZeile("Vorname:" , person.getVorname());
        nachname = completeZeile("Nachname:" , person.getNachname());

        //Combo-Boxen
        abschluss.setItems("Bachelor", "Master");
        abschluss.setValue("Bachelor");
        studiengang.setItems(studienfachRepository.findAllByAbschluss("Bachelor"));
        studiengang.setItemLabelGenerator(Studienfach::getFach);
        abschluss.setRequiredIndicatorVisible(true);
        abschluss.addValueChangeListener(e ->
        {
            if (abschluss.getValue().equals("Bachelor")) {
                studiengang.setItems(studienfachRepository.findAllByAbschluss("Bachelor"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            } else if (abschluss.getValue().equals("Master")) {
                studiengang.setItems(studienfachRepository.findAllByAbschluss("Master"));
                studiengang.setItemLabelGenerator(Studienfach::getFach);
            }
        });

        studiengang.setRequired(true);

        if (person.getStudienfach() != null) {
            abschluss.setValue(person.getStudienfach().getAbschluss());
            studiengang.setValue(person.getStudienfach());
        }

        completeZeile("Abschluss:", "abschluss");
        completeZeile("Studienfach:", "studiengang");



        berufsbezeichnung.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        berufsbezeichnung.setItems(jobKategorieRepository.findAll());
        berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);
        berufsbezeichnung.setValue(person.getJobKategorien());

        branche.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        branche.setItems(brancheRepository.findAll());
        branche.setItemLabelGenerator(Branche::getName);
        branche.setValue(person.getBranchen());

        berufsfeld.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        berufsfeld.setItems(berufsFeldRepository.findAll());
        berufsfeld.setItemLabelGenerator(BerufsFelder::getName);
        berufsfeld.setValue(person.getBerufsFelder());

        standort.getStyle().set("--lumo-contrast-60pct","--hintergrund-weiß");
        standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
        standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
        standort.setRequired(true);
        standort.setValue(person.getOrte());


        completeZeile("Stellen, die mich interessieren:", "berufsbezeichnung");
        completeZeile("Bevorzugt in der Nähe von:", "standort");
        completeZeile("Bevorzugte Branche(n):", "branche");
        completeZeile("Bevorzugte Berufsfelder:","berufsfeld");


        Div kenntnisseDiv = new Div();
        kenntnisseDiv.addClassName("myTab");


        Div ueberDiv = new Div();
        ueberDiv.addClassName("myTab");




        if (tab.equals(allgemein)) {
            content.add(allgemeinDiv);
        } else if (tab.equals(kenntnisse)) {
            content.add(kenntnisseDiv);
        } else if (tab.equals(ueberMich)){
            content.add(ueberDiv);
        }
    }






    private TextField completeZeile(String title, String wert){

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");
        TextField textField = new TextField();

        if(wert == "abschluss"){
            wertH.add(abschluss);
        }else if (wert == "studiengang"){
            wertH.add(studiengang);
        }else if(wert == "standort"){
            wertH.add(standort);
        }else if(wert == "berufsbezeichnung"){
            wertH.add(berufsbezeichnung);
        }else if(wert == "branche"){
            wertH.add(branche);
        }else if(wert == "berufsfeld"){
            wertH.add(berufsfeld);
        }else{
            textField.addClassName("feld");
            textField.setValue(wert);
            wertH.add(textField);
        }



        HorizontalLayout completeZeile = new HorizontalLayout(titleH,wertH);
        completeZeile.setAlignItems(Alignment.STRETCH);
        completeZeile.addClassName("completeZeile");

        allgemeinDiv.add(completeZeile);

        return textField;

    }


    private void updateInfos(){

        person.setVorname(vorname.getValue());
        person.setNachname(nachname.getValue());


        abschluss.setValue(abschluss.getValue());
        studiengang.setValue(studiengang.getValue());

        if (person.getStudienfach() != null) {
            person.setStudienfach(studiengang.getValue());
        }




        person.setOrte(standort.getSelectedItems());
        person.setBranchen(branche.getSelectedItems());
        person.setBerufsFelder(berufsfeld.getSelectedItems());
        person.setJobKategorien(berufsbezeichnung.getSelectedItems());



        personService.saveStudent(person);

        UI.getCurrent().getPage().setLocation("/student");



    }
}
