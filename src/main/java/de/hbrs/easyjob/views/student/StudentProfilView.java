package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.StudentProfilController;
import de.hbrs.easyjob.views.components.StudentLayout;

@Route(value = "student" , layout = StudentLayout.class)

// TODO: URL Parameter für Studenten ID hinzufügen (siehe unternehmen/ProfilView.java)
public class StudentProfilView extends VerticalLayout {

    //Tabs namen
    private final Tab allgemein;
    private final Tab kenntnisse;
    private final Tab ueberMich;


    //Content wo die Inhalt von einem Tab gezeigt wird
    private final VerticalLayout content;



    StudentProfilController  person;

    StudentProfilView(){
        UI.getCurrent().getPage().addStyleSheet("StudentProfilView.css");
        person = new StudentProfilController();



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

        iconsProf.add(link,pen);



        //Profil Bild
        Div profilBild = new Div();
        profilBild.addClassName("profilBild");
        profilBild.add(new Image(person.getFoto(), "EasyJob"));





        //Name
        H1 name = new H1();
        name.addClassName("name");
        //name.add("Max Mustermann");
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


        studentInfo.add(iconsProf,profilBild,name,/*location,*/tabs, content);


        add(studentInfo);

    }

    private void setContent(Tab tab) {
        content.removeAll();


        Div allgemeinDiv = new Div();
        allgemeinDiv.addClassName("myTab");
        allgemeinDiv.add(completeZeile("Studienfach:", person.getStudienfachUndAbschluss()),
        //completeZeile("Hochschulsemester:", "5"),

        completeZeile("Stellen, die mich interessieren:", person.getJobKategorie() ),
        completeZeile("Bevorzugt in der Nähe von:", " "),
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
