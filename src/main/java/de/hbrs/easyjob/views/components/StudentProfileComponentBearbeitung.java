package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.OrtController;
import de.hbrs.easyjob.controllers.PersonController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.unternehmen.StellenanzeigeErstellenView;

import java.awt.*;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@StyleSheet("StudentProfilView.css")
public class StudentProfileComponentBearbeitung extends VerticalLayout {

    private final Student student;

    private final VerticalLayout studentInfo = new VerticalLayout();
    private final VerticalLayout content = new VerticalLayout();

    //Tabs
    private Tab allgemein;
    private Tab kenntnisse;
    private Tab ueberMich;
    Div allgemeinDiv;
    Div kenntnisseDiv;

    //Allgemein
    TextField vorname;
    TextField nachname;
    ComboBox<String> abschluss = new ComboBox<>();
    ComboBox<Studienfach> studiengang  = new ComboBox<>();
    MultiSelectComboBox<Ort> standort = new MultiSelectComboBox<>();
    MultiSelectComboBox<JobKategorie> berufsbezeichnung= new MultiSelectComboBox<>();
    MultiSelectComboBox<Branche> branche= new MultiSelectComboBox<>();
    MultiSelectComboBox<BerufsFelder> berufsfeld= new MultiSelectComboBox<>();

    //Kenntnisse
    TextArea ausbildung = new TextArea();
    TextArea praxisErfahrung = new TextArea();
    MultiSelectComboBox<Faehigkeit> sprachen = new MultiSelectComboBox<>();
    MultiSelectComboBox<Faehigkeit> edvKenntnisse = new MultiSelectComboBox<>();

    //Über mich
    TextArea freitext = new TextArea();

    //Services, Controller, Repositories
    private final StudentService studentService;
    private final FaehigkeitService faehigkeitService;
    StudienfachRepository studienfachRepository;
    BerufsFeldRepository berufsFeldRepository;
    BrancheRepository brancheRepository;
    JobKategorieRepository jobKategorieRepository;
    FaehigkeitRepository faehigkeitRepository;
    OrtController ortController;

    private PasswortService passwortService;


    Image profilBild2;
    private final String style;


    public StudentProfileComponentBearbeitung(Student student,
                                              String styleClass,
                                              StudentService studentService,
                                              FaehigkeitService faehigkeitService,
                                              StudienfachRepository studienfachRepository,
                                              BerufsFeldRepository berufsFelderRepository,
                                              BrancheRepository brancheRepository,
                                              JobKategorieRepository jobKategorieRepository,
                                              FaehigkeitRepository faehigkeitRepository,
                                              OrtController ortController,
                                              PasswortService passwortService
                                    ) {
        this.student = student;
        this.studentService = studentService;
        this.faehigkeitService = faehigkeitService;
        this.studienfachRepository = studienfachRepository;
        this.berufsFeldRepository = berufsFelderRepository;
        this.brancheRepository = brancheRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.faehigkeitRepository = faehigkeitRepository;
        this.ortController = ortController;
        this.passwortService = passwortService;
        style=styleClass;

        UI.getCurrent().getPage().addStyleSheet("StudentProfilView.css");

        initializeComponent();
    }

    private void initializeComponent() {

        if (student == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }

        UI.getCurrent().getPage().addStyleSheet(style);

        addClassName("all");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //Bildupload in Methode ausgelagert
        bildUpload();



        //Password ändern
        Icon edit = new Icon(VaadinIcon.EDIT);
        PasswortAendernDialog passwort = new PasswortAendernDialog(student,"StudentProfilView.css", passwortService);
        Button editAdmin = new Button("Password Ändern", edit,e -> passwort.open());



        studentInfo.addClassName("studentInfo");
        studentInfo.setAlignItems(Alignment.CENTER);

        //Tabs
        allgemein = new Tab("Allgemein");
        kenntnisse = new Tab("Kenntnisse");
        ueberMich = new Tab("Über mich");

        Tabs tabs = new Tabs(allgemein, kenntnisse, ueberMich);

        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));

        content.setWidth("100%");
        content.setMaxWidth("100em");
        content.setAlignItems(Alignment.STRETCH);
        setContent(tabs.getSelectedTab());

        // Buttons
        Button next = new Button("Fertig", new Icon(VaadinIcon.CHECK));
        next.addClassName("next");
        next.addClickListener(e -> {
            if(tabs.getSelectedTab().equals(allgemein)) updateAllgemein();
            else if(tabs.getSelectedTab().equals(kenntnisse)) updateKenntnisse();
            else if (tabs.getSelectedTab().equals(ueberMich)){
                student.setFreitext(freitext.getValue());
                studentService.saveStudent(student);
                UI.getCurrent().getPage().setLocation("/student");
            }
        });

        Button back = new Button("Abbrechen", new Icon(VaadinIcon.CLOSE));
        back.addClassName("back");
        back.addClickListener(e -> UI.getCurrent().getPage().setLocation("/student"));


        // Buttons Container
        HorizontalLayout actions = new HorizontalLayout(back, next);
        actions.setSpacing(true);
        actions.setJustifyContentMode(JustifyContentMode.CENTER);
        actions.setAlignSelf(Alignment.CENTER,actions);

        studentInfo.add(editAdmin,tabs, content, actions);
        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();

        Notification notification = Notification
                .show("Achtung: nicht gespeicherte Änderungen werden verworfen");
        notification.setPosition(Notification.Position.BOTTOM_START);
      //  notification.setDuration(1);

        //Allgemein-Tab
        if(tab.equals(allgemein)) {
            allgemeinDiv = new Div();
            allgemeinDiv.addClassName("myTab");

            vorname = completeZeile("allgemein", "Vorname:", student.getVorname());
            nachname = completeZeile("allgemein", "Nachname:", student.getNachname());

            abschluss.setItems("Bachelor", "Master");
            abschluss.setValue("Bachelor");
            if (student.getStudienfach() != null) {
                String meinAbschluss = student.getStudienfach().getAbschluss();
                if (meinAbschluss.equals("Master")) {
                    abschluss.setValue("Master");
                }
                studienfachChanger(meinAbschluss);
                studiengang.setValue(student.getStudienfach());
            }
            abschluss.addValueChangeListener(e -> {
                if(!abschluss.isEmpty()) {
                    studienfachChanger(abschluss.getValue());
                }
            });


            completeZeile("allgemein", "Abschluss:", "abschluss");
            completeZeile("allgemein", "Studienfach:", "studiengang");

            berufsbezeichnung.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            berufsbezeichnung.setItems(jobKategorieRepository.findAll());
            berufsbezeichnung.setItemLabelGenerator(JobKategorie::getKategorie);
            if(student.getJobKategorien() != null) berufsbezeichnung.setValue(student.getJobKategorien());

            branche.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            branche.setItems(brancheRepository.findAll());
            branche.setItemLabelGenerator(Branche::getName);
            if(student.getBranchen() != null) branche.setValue(student.getBranchen());

            berufsfeld.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            berufsfeld.setItems(berufsFeldRepository.findAll());
            berufsfeld.setItemLabelGenerator(BerufsFelder::getName);
            if(student.getBerufsFelder() != null) berufsfeld.setValue(student.getBerufsFelder());

            standort.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            standort.setItems(ortController.getOrtItemFilter(), ortController.getAlleOrte());
            standort.setItemLabelGenerator(ortController.getOrtItemLabelGenerator());
            if(student.getOrte() != null) standort.setValue(student.getOrte());

            completeZeile("allgemein", "Stellen, die mich interessieren:", "berufsbezeichnung");
            completeZeile("allgemein", "Bevorzugt in der Nähe von:", "standort");
            completeZeile("allgemein", "Bevorzugte Branche(n):", "branche");
            completeZeile("allgemein", "Bevorzugte Berufsfelder:", "berufsfeld");

            content.add(allgemeinDiv);
        }

        //Kenntnisse-Tab
        else if (tab.equals(kenntnisse)) {

            kenntnisseDiv = new Div();
            kenntnisseDiv.addClassName("myTab");

            ausbildung.setHeight("150px");
            if(faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Ausbildung") != null) {
                ausbildung.setValue(faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Ausbildung").getBezeichnung());
            }

            praxisErfahrung.setHeight("150px");
            if(faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Praxiserfahrung") != null) {
                praxisErfahrung.setValue(faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Praxiserfahrung").getBezeichnung());
            }

            sprachen.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            sprachen.setItems(faehigkeitService.findAllByKategorie("Sprache"));
            sprachen.setItemLabelGenerator(Faehigkeit::getBezeichnung);
            Set<Faehigkeit> meineSprachen = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "Sprache");
            if(meineSprachen != null) sprachen.setValue(meineSprachen);

            edvKenntnisse.getStyle().set("--lumo-contrast-60pct", "--hintergrund-weiß");
            edvKenntnisse.setItems(faehigkeitService.findAllByKategorie("EDV"));
            edvKenntnisse.setItemLabelGenerator(Faehigkeit::getBezeichnung);
            Set<Faehigkeit> meineEDV = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "EDV");
            if(meineEDV != null) edvKenntnisse.setValue(meineEDV);

            completeZeile("kenntnisse","Ausbildung:", "ausbildung");
            completeZeile("kenntnisse","Praxiserfahrung:", "praxiserfahrung");
            completeZeile("kenntnisse", "Sprachen:", "sprachen");
            completeZeile("kenntnisse","EDV-Kenntnisse:", "edv");

            content.add(kenntnisseDiv);
        }

        //Über mich-Tab
        else if (tab.equals(ueberMich)){
            Div ueberDiv = new Div();
            ueberDiv.addClassName("myTab");

            if(student.getFreitext() != null) freitext.setValue(student.getFreitext());
            freitext.setHelperText("Maximal 250 Zeichen");
            short charLimit = 250;
            freitext.setMaxLength(charLimit);
            freitext.setValueChangeMode(ValueChangeMode.EAGER);
            freitext.addValueChangeListener(e -> e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit));

            ueberDiv.add(freitext);
            content.add(ueberDiv);
        }
    }



    private TextField completeZeile(String tab, String title, String wert){

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");
        TextField textField = new TextField();

        if(Objects.equals(wert, "abschluss")){
            wertH.add(abschluss);
        }else if (Objects.equals(wert, "studiengang")){
            wertH.add(studiengang);
        }else if(Objects.equals(wert, "standort")){
            wertH.add(standort);
        }else if(Objects.equals(wert, "berufsbezeichnung")){
            wertH.add(berufsbezeichnung);
        }else if(Objects.equals(wert, "branche")){
            wertH.add(branche);
        }else if(Objects.equals(wert, "berufsfeld")){
            wertH.add(berufsfeld);
        }else if(Objects.equals(wert, "ausbildung")){
            wertH.add(ausbildung);
        }else if(Objects.equals(wert, "praxiserfahrung")){
            wertH.add(praxisErfahrung);
        }else if(Objects.equals(wert, "sprachen")){
            wertH.add(sprachen);
        }else if(Objects.equals(wert, "edv")) {
            wertH.add(edvKenntnisse);
        }else{
            textField.addClassName("feld");
            textField.setValue(wert);
            wertH.add(textField);
        }

        HorizontalLayout completeZeile = new HorizontalLayout();
        completeZeile.setWidthFull();
        completeZeile.add(titleH);
        completeZeile.setAlignItems(Alignment.START);
        completeZeile.add(wertH);
        completeZeile.setAlignItems(Alignment.END);
        completeZeile.addClassName("completeZeile");

        if(tab.equals("allgemein")) {
            allgemeinDiv.add(completeZeile);
        } else if (tab.equals("kenntnisse")) {
            kenntnisseDiv.add(completeZeile);
        }

        return textField;
    }

    private void studienfachChanger(String selected){
        if (selected.equals("Bachelor")) {
            studiengang.setItems(studienfachRepository.findAllByAbschluss("Bachelor"));
            studiengang.setItemLabelGenerator(Studienfach::getFach);
        } else if (selected.equals("Master")) {
            studiengang.setItems(studienfachRepository.findAllByAbschluss("Master"));
            studiengang.setItemLabelGenerator(Studienfach::getFach);
        }
    }
    private void updateAllgemein(){

        student.setVorname(vorname.getValue());
        student.setNachname(nachname.getValue());

        abschluss.setValue(abschluss.getValue());
        studiengang.setValue(studiengang.getValue());

        if (student.getStudienfach() != null) {
            student.setStudienfach(studiengang.getValue());
        }

        student.setOrte(standort.getSelectedItems());
        student.setBranchen(branche.getSelectedItems());
        student.setBerufsFelder(berufsfeld.getSelectedItems());
        student.setJobKategorien(berufsbezeichnung.getSelectedItems());

        studentService.saveStudent(student);

        UI.getCurrent().getPage().setLocation("/student");
    }

    private void updateKenntnisse(){

        Faehigkeit meineAusbildung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Ausbildung");
        if(meineAusbildung != null){
            meineAusbildung.setBezeichnung(ausbildung.getValue());
        }else {
            meineAusbildung = new Faehigkeit("Ausbildung", ausbildung.getValue());
            faehigkeitRepository.save(meineAusbildung);
        }

        Faehigkeit meineErfahrung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Praxiserfahrung");
        if(meineErfahrung != null){
            meineErfahrung.setBezeichnung(praxisErfahrung.getValue());
        }else  {
            meineErfahrung = new Faehigkeit("Praxiserfahrung", praxisErfahrung.getValue());
            faehigkeitRepository.save(meineErfahrung);
        }

        Set<Faehigkeit> meineEdvKenntnisse = edvKenntnisse.getSelectedItems();
        Set<Faehigkeit> meineSprachen = sprachen.getSelectedItems();

        Set<Faehigkeit> meineFaehigkeiten = new HashSet<>();
        meineFaehigkeiten.add(meineAusbildung);
        meineFaehigkeiten.add(meineErfahrung);
        meineFaehigkeiten.addAll(meineEdvKenntnisse);
        meineFaehigkeiten.addAll(meineSprachen);

        student.setFaehigkeiten(meineFaehigkeiten);
        studentService.saveStudent(student);

        UI.getCurrent().getPage().setLocation("/student");
    }

    private void bildUpload(){

        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        boolean hasBild = student.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? student.getFoto(): "images/blank-profile-picture.png", "EasyJob");
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

        studentInfo.add(rahmen,upload,uploadListe);
    }
}
