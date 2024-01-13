package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.student.EinstellungenUebersichtStudentView;

import java.util.stream.Collectors;

@StyleSheet("AdminPersonenVerwaltenView.css")
public class AdminStudentProfileComponent extends VerticalLayout {
    private Student student;

    private Tab allgemein;
    private  Tab kenntnisse;
    private  Tab ueberMich;
    private  VerticalLayout content;
    private final String style;

    private final StudentService studentService;


    public AdminStudentProfileComponent(Student student, String styleClass, StudentService studentService) {
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

        VerticalLayout studentInfo = new VerticalLayout();

        studentInfo.addClassName("studentInfo");
        studentInfo.setAlignItems(Alignment.CENTER);

        studentInfo.setAlignSelf(Alignment.END);


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
        content.setWidth("95%");
        content.setAlignItems(Alignment.STRETCH);
        content.addClassName("content");

        setContent(tabs.getSelectedTab());


        studentInfo.add(tabs, content);


        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();


        Div allgemeinDiv = new Div();
        allgemeinDiv.addClassName("myTab");
        allgemeinDiv.add(completeZeile("Studienfach:", (student.getStudienfach().getFach()+"("+(student.getStudienfach().getAbschluss()
                        .equals("Bachelor") ? "B.Sc." : "M.Sc.") +")")),
                //completeZeile("Hochschulsemester:", "5"),

                completeZeile("Stellen, die mich interessieren:", studentService.getAllJobKategorien(student.getId_Person()).stream()
                        .map(JobKategorie::getKategorie).collect(Collectors.joining(",")) ),
                completeZeile("Bevorzugt in der Nähe von:", studentService.getAllOrte(student.getId_Person()).stream().map(Ort::getOrt)
                        .collect(Collectors.joining(", "))),
                completeZeile("Bevorzugte Branche(n):", " "),
                completeZeile("Bevorzugte Berufsfelder:", " ")

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
