package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.PersonService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.FileUpload;
import de.hbrs.easyjob.views.student.EinstellungenUebersichtStudentView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.stream.Collectors;

public class StudentProfileComponent2 extends VerticalLayout {
    private Student person;




    private  Tab allgemein;
    private  Tab kenntnisse;
    private  Tab ueberMich;
    private  VerticalLayout content;
    private final String style;

    private final StudentService personService;

    Image profilBild2;


    public StudentProfileComponent2(Student student, String styleClass, StudentService studentService) {
        this.person = student;
        this.personService = studentService;
        style=styleClass;
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
        Image platzhalterBild = new Image(person.getFoto(), "EasyJob");
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




/*
        //die Icons zu einstellungen und Bearbeitung
        HorizontalLayout iconsProf = new HorizontalLayout();
        iconsProf.setPadding(false);
        iconsProf.setMargin(false);
        iconsProf.setJustifyContentMode(JustifyContentMode.END);



        //Einstellungen Icon
        Icon cog = new Icon(VaadinIcon.COG);
        cog.addClassName("iconsProf");

        RouterLink link = new RouterLink(EinstellungenUebersichtStudentView.class);
        link.add(cog);



        Icon pen =new Icon(VaadinIcon.PENCIL);
        pen.addClassName("iconsProf");

        iconsProf.add(link,pen);





        //Profil Bild
        Div profilBild = new Div();
        profilBild.addClassName("profilBild");

        profilBild.add(new Image(person.getFoto() != null ? person.getFoto() : "images/blank-profile-picture.png", "EasyJob"));



        //Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(person.getVorname() +" "+ person.getNachname());

        //Ort
        /*
        HorizontalLayout location = new HorizontalLayout();
        location.addClassName("location");
        location.setSpacing(false);
        location.setPadding(false);
        location.setMargin(false);

        IconFactory lo = FontAwesome.Solid.MAP_MARKED_ALT;
        Icon loc =  lo.create();
        loc.addClassName("iconsInUnternehmen");

        H1 stadt = new H1();
        stadt.addClassName("stadt");
        stadt.add("Bonn");

        location.add(loc, stadt);

         */





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


        studentInfo.add(rahmen,/*location,*/tabs, content);


        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();


        Div allgemeinDiv = new Div();
        allgemeinDiv.addClassName("myTab");
        allgemeinDiv.add(completeZeile("Studienfach:", (person.getStudienfach().getFach()+"("+(person.getStudienfach().getAbschluss()
                        .equals("Bachelor") ? "B.Sc." : "M.Sc.") +")")),
                //completeZeile("Hochschulsemester:", "5"),

                completeZeile("Stellen, die mich interessieren:", personService.getAllJobKategorien(person.getId_Person()).stream()
                        .map(JobKategorie::getKategorie).collect(Collectors.joining(",")) ),
                completeZeile("Bevorzugt in der Nähe von:", personService.getAllOrte(person.getId_Person()).stream().map(Ort::getOrt)
                        .collect(Collectors.joining(", "))),
                completeZeile("Bevorzugte Branche(n):", " "),
                completeZeile("Bevorzugte Berufsfelder:", " ")

        );



        /*

        allgemeinDiv.add(zeileDiv("Studienfach:", "B. Sc. Informatik"),
                         zeileDiv("Hochschulsemester:", "5"),
                         zeileDiv("Ich suche nach:", "Abschlussarbeit"),
                zeileDiv("Bevorzugt in der Nähe von:", "Sankt Augustin, Bonn"),
                zeileDiv("Bevorzugte Branche(n):", "Wissenschaft/ Forschung"),
                zeileDiv("Bevorzugte Berufsfelder:", "Cyber Security, Forschung")
        );
*/

        Div kenntnisseDiv = new Div();
        kenntnisseDiv.addClassName("myTab");


        kenntnisseDiv.add(zeileKenn("Programmiersprachen:" , new String[]{"Java", "C#", "Python"}),
                zeileKenn("Betriebsysteme:" , new String[]{"Windows(desktop)", "macOS"} ),
                zeileKenn("Datenbanken:" , new String[]{"PostgreSQL"} ),
                zeileKenn("Frameworks, Bibliotheken und Umgebungen:" , new String[]{"Eclipse", "JUnit","Pandas","NumPy"} ),
                zeileKenn("Methoden:" , new String[]{"CI/CD", "TDD","Scrum","UML"} ),
                zeileKenn("Rollen und Tätigkeiten:" , new String[]{"Backend Entwicklung", " Frontend Entwicklung"} )
        );

        Div ueberDiv = new Div();
        ueberDiv.addClassName("myTab");
        ueberDiv.add("Hallo! Mein Name ist Max Mustermann, und ich befinde mich derzeit im 5. Semester meines Informatikstudiums an der Hochschule Bonn Rhein-Sieg. Als begeisterter und zielstrebiger Student habe ich eine Leidenschaft für die Welt der Informationstechnologie und insbesondere für das aufregende Feld der Cybersecurity.\n" +
                "\n" +
                "Mein Studium hat mir nicht nur ein solides Fundament in Programmierung, Datenanalyse und Informationssystemen vermittelt, sondern auch meine Neugier und meinen Wunsch geweckt, zur Sicherheit und Integrität digitaler Systeme beizutragen. Mein Ziel ist es, meine Leidenschaft für Cybersecurity in eine erfüllende und herausfordernde berufliche Laufbahn Hallo! Mein Name ist Max Mustermann, und ich befinde mich derzeit im 5. Semester meines Informatikstudiums an der Hochschule Bonn Rhein-Sieg. Als begeisterter und zielstrebiger Student habe ich eine Leidenschaft für die Welt der Informationstechnologie und insbesondere für das aufregende Feld der Cybersecurity.\n" +
                "\n" +
                "Mein Studium hat mir nicht nur ein solides Fundament in Programmierung, Datenanalyse und Informationssystemen vermittelt, sondern auch meine Neugier und meinen Wunsch geweckt, zur Sicherheit und Integrität digitaler Systeme beizutragen. Mein Ziel ist es, meine Leidenschaft für Cybersecurity in eine erfüllende und herausfordernde berufliche Laufbahn ");



        if (tab.equals(allgemein)) {
            content.add(allgemeinDiv);
        } else if (tab.equals(kenntnisse)) {
            content.add(kenntnisseDiv);
        } else if (tab.equals(ueberMich)){
            content.add(ueberDiv);
        }
    }




    public Div zeileDiv(String beschreibung, String wert ) {
        Div divReturn = new Div();
        divReturn.addClassName("divReturn");

        Div beschreibungDiv = new Div();
        beschreibungDiv.addClassName("zeileDiv");
        beschreibungDiv.add(beschreibung);

        Div wertDiv = new Div();
        wertDiv.addClassName("zeileDiv");
        wertDiv.add(wert);

        divReturn.addClassName("divReturn");
        divReturn.add(beschreibungDiv,wertDiv);

        return divReturn;

    }

    public Div zeileKenn(String beschreibung, String[] wert ) {
        Div divReturn = new Div();
        divReturn.addClassName("divReturn");

        Div beschreibungDiv = new Div();
        beschreibungDiv.addClassName("zeileDiv");
        beschreibungDiv.add(beschreibung);

        Div wertDiv = new Div();
        wertDiv.addClassName("zeileDiv");

        for (String s: wert
        ) {
            Span pending = new Span(s);
            pending.getElement().getThemeList().add("badge primary");
            pending.addClassName("badge");
            wertDiv.add(pending);
        }




        divReturn.addClassName("divReturn");
        divReturn.add(beschreibungDiv,wertDiv);

        return divReturn;

    }

    private HorizontalLayout completeZeile(String title, String wert){

        HorizontalLayout titleH = new HorizontalLayout();
        titleH.setSizeFull();
        titleH.addClassName("title");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");
        wertH.add(wert);
        HorizontalLayout completeZeile = new HorizontalLayout(titleH,wertH);
        completeZeile.setAlignItems(Alignment.STRETCH);
        completeZeile.addClassName("completeZeile");
        return completeZeile;


    }
}
