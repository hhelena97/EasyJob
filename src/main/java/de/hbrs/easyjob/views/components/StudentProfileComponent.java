package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.student.EinstellungenUebersichtStudentView;
import de.hbrs.easyjob.views.student.StudentProfilBearbeitungView;
import java.util.StringJoiner;

import java.util.stream.Collectors;

public class StudentProfileComponent extends VerticalLayout {
    private Student student;

    private  Tab allgemein;
    private  Tab kenntnisse;
    private  Tab ueberMich;
    private  VerticalLayout content;
    private final String style;

    private final StudentService studentService;


    public StudentProfileComponent(Student student, String styleClass, StudentService studentService) {
        this.student = student;
        this.studentService = studentService;
        style=styleClass;
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

        RouterLink linkPen = new RouterLink(StudentProfilBearbeitungView.class);
        linkPen.add(pen);

        iconsProf.add(link,linkPen);



        //Profil Bild
        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        Image ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Platzhalter Bild
        boolean hasBild = student.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? student.getFoto(): "images/blank-profile-picture.png", "EasyJob");
        Image profilBild2 = platzhalterBild;
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        //Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(student.getVorname() +" "+ student.getNachname());

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

        studentInfo.setAlignSelf(Alignment.END,iconsProf);


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


        studentInfo.add(iconsProf,rahmen,name,/*location,*/tabs, content);


        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();


        String branche = student.getBranchen().stream()
                .map(Branche::getName)
                .collect(Collectors.joining(", "));

        String berufsfelder = student.getBerufsFelder().stream()
                .map(BerufsFelder::getName)
                .collect(Collectors.joining(", "));

        Div allgemeinDiv = new Div();
        allgemeinDiv.addClassName("myTab");
        allgemeinDiv.add(completeZeile("Studienfach:", (student.getStudienfach().getFach()+"("+(student.getStudienfach().getAbschluss()
                        .equals("Bachelor") ? "B.Sc." : "M.Sc.") +")")),
                //completeZeile("Hochschulsemester:", "5"),

                completeZeile("Stellen, die mich interessieren:", studentService.getAllJobKategorien(student.getId_Person()).stream()
                        .map(JobKategorie::getKategorie).collect(Collectors.joining(",")) ),
                completeZeile("Bevorzugt in der Nähe von:", studentService.getAllOrte(student.getId_Person()).stream().map(Ort::getOrt)
                        .collect(Collectors.joining(", "))),




                completeZeile("Bevorzugte Branche(n):", branche),
                completeZeile("Bevorzugte Berufsfelder:", berufsfelder)

        );



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
        ueberDiv.add(student.getFreitext());





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
