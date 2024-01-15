package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.student.EinstellungenUebersichtStudentView;
import de.hbrs.easyjob.views.student.StudentProfilBearbeitungView;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentProfileComponent extends VerticalLayout {
    private final Student student;

    private  Tab allgemein;
    private  Tab kenntnisse;
    private  Tab ueberMich;
    private  VerticalLayout content;
    private final String style;

    private final StudentService studentService;
    private final FaehigkeitService faehigkeitService;

    private final MeldungController meldungController;

    boolean isUnternehmensPerson;


    public StudentProfileComponent(Student student,
                                   String styleClass,
                                   StudentService studentService,
                                   MeldungController meldungController,
                                   FaehigkeitService faehigkeitService) {
        this.student = student;
        this.studentService = studentService;
        this.faehigkeitService = faehigkeitService;
        style=styleClass;
        this.meldungController = meldungController;
        isUnternehmensPerson = Objects.equals(style, "styles/UnternehmenStudentProfilView.css");

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

        HorizontalLayout iconsProf = new HorizontalLayout();
        HorizontalLayout frame = new HorizontalLayout();

        Image ellipse;
        if (!isUnternehmensPerson) {

            ellipse = new Image("images/Ellipse-Lila-Groß.png", "Bildumrandung");
            //die Icons zu einstellungen und Bearbeitung

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

        } else {

            ellipse = new Image("images/Ellipse-Blau-Groß.png", "Bildumrandung");
            //Zurück Button
            ZurueckButtonRundLayout zurueck = new ZurueckButtonRundLayout("Unternehmen");
            zurueck.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());

            frame.setPadding(false);
            frame.setMargin(false);
            frame.setWidthFull();
            frame.setAlignItems(Alignment.CENTER);
            frame.setJustifyContentMode(JustifyContentMode.BETWEEN);
            VerticalLayout dotsLayout = new VerticalLayout();
            dotsLayout.setWidth("fit-content");
            // Drei-Punkte-Icon für das Dropdown-Menü
            Icon dots = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
            dots.getStyle().set("cursor", "pointer");
            dots.setSize("1em");

            // Dropdown-Menü erstellen
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(dots);
            contextMenu.setOpenOnClick(true);
            MenuItem item = contextMenu.addItem("Melden", e -> {
                Meldung meldung = new Meldung();
                meldungController.saveMeldung(meldung, student);
                Notification.show("Gemeldet", 3000, Notification.Position.TOP_STRETCH);
            });

            item.getElement().getStyle().set("color", "red");

            dotsLayout.add(dots);
            frame.add(zurueck, dotsLayout);
        }

        //Bildrahmen
        Div rahmen = new Div();
        rahmen.addClassName("profile-picture-frame");
        ellipse.addClassName("profile-picture-background");
        rahmen.add(ellipse);

        //Profilbild
        boolean hasBild = student.getFoto() != null;
        Image platzhalterBild = new Image(hasBild? student.getFoto(): "images/blank-profile-picture.png", "EasyJob");
        Div bildDiv = new Div(platzhalterBild);
        platzhalterBild.addClassName("picture-round");
        rahmen.add(bildDiv);

        //Name
        H1 name = new H1();
        name.addClassName("name");
        name.add(student.getVorname() +" "+ student.getNachname());


        VerticalLayout studentInfo = new VerticalLayout();
        studentInfo.addClassName("studentInfo");
        studentInfo.setAlignItems(Alignment.CENTER);

        if(!isUnternehmensPerson) {
            studentInfo.setAlignSelf(Alignment.END,iconsProf);
        } else {
            studentInfo.setAlignSelf(Alignment.END,frame);
        }


        //Tabs
        allgemein = new Tab("Allgemein");
        kenntnisse = new Tab("Kenntnisse");
        ueberMich = new Tab("Über mich");

        Tabs tabs = new Tabs(allgemein, kenntnisse, ueberMich);
        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));

        content = new VerticalLayout();
        content.setWidth("100%");
        content.setMaxWidth("100em");
        content.setAlignItems(Alignment.STRETCH);

        setContent(tabs.getSelectedTab());


        if (!isUnternehmensPerson) {
            studentInfo.add(iconsProf,rahmen,name,tabs, content);
        } else {
            studentInfo.add(frame,rahmen,name,tabs, content);
        }

        add(studentInfo);
    }

    private void setContent(Tab tab) {
        content.removeAll();

        //Allgemein
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
                        completeZeile("Stellen, die mich interessieren:", studentService.getAllJobKategorien(student.getId_Person()).stream()
                                .map(JobKategorie::getKategorie).collect(Collectors.joining(",")) ),
                        completeZeile("Bevorzugt in der Nähe von:", studentService.getAllOrte(student.getId_Person()).stream()
                                .map(ort -> ort.getOrt() + " (" + ort.getPLZ()+ ")")
                                .collect(Collectors.joining(", "))),
                        completeZeile("Bevorzugte Branche(n):", branche),
                        completeZeile("Bevorzugte Berufsfelder:", berufsfelder)
        );


        //Kenntnisse
        Div kenntnisseDiv = new Div();
        kenntnisseDiv.addClassName("myTab");

        Faehigkeit ausbildung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Ausbildung");
        if(ausbildung != null) kenntnisseDiv.add(completeZeile("Ausbildung:", ausbildung.getBezeichnung()));

        Faehigkeit erfahrung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Praxiserfahrung");
        if(erfahrung != null) kenntnisseDiv.add(completeZeile("Praxiserfahrung:", erfahrung.getBezeichnung()));

        Set<Faehigkeit> sprachen = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "Sprache");
        if(!sprachen.isEmpty()) {
            String[] beschreibungen = sprachen.stream()
                    .map(Faehigkeit::getBezeichnung)
                    .toArray(String[]::new);
            kenntnisseDiv.add(zeileKenn("Sprachen:", beschreibungen));
        }

        Set<Faehigkeit> edv = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "EDV");
        if(!edv.isEmpty()) {
            String[] beschreibungen = edv.stream()
                    .map(Faehigkeit::getBezeichnung)
                    .toArray(String[]::new);
            kenntnisseDiv.add(zeileKenn("EDV-Kenntnisse:", beschreibungen));
        }


        //Über mich
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
        titleH.addClassName("wert");
        titleH.add(title);
        HorizontalLayout wertH = new HorizontalLayout();
        wertH.setSizeFull();
        wertH.addClassName("wert");
        wertH.add(wert);
        HorizontalLayout completeZeile = new HorizontalLayout();
        completeZeile.setWidthFull();
        completeZeile.add(titleH);
        completeZeile.setAlignItems(Alignment.START);
        completeZeile.add(wertH);
        completeZeile.setAlignItems(Alignment.END);
        completeZeile.addClassName("completeZeile");

        return completeZeile;
    }
}
