package de.hbrs.easyjob.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.FaehigkeitRepository;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.allgemein.LoginView;


import java.util.Set;
import java.util.stream.Collectors;

@StyleSheet("AdminPersonenVerwaltenView.css")
public class AdminStudentProfileComponent extends VerticalLayout {
    private final Student student;

    private Tab allgemein;
    private Tab kenntnisse;
    private Tab ueberMich;
    private VerticalLayout content;
    private final String style;

    private final FaehigkeitService faehigkeitService;
    private final StudentService studentService;


    public AdminStudentProfileComponent(Student student,
                                        String styleClass,
                                        StudentService studentService,
                                        FaehigkeitRepository faehigkeitRepository) {
        this.student = student;
        this.studentService = studentService;
        faehigkeitService = new FaehigkeitService(faehigkeitRepository);
        style = styleClass;
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
                event -> setContent(event.getSelectedTab()));

        content = new VerticalLayout();
        content.setWidth("100%");
        content.setMaxWidth("100em");
        content.setAlignItems(Alignment.STRETCH);
        //content.addClassName("content");

        setContent(tabs.getSelectedTab());


        studentInfo.add(tabs, content);


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
        allgemeinDiv.add(completeZeile("Studienfach:", (student.getStudienfach().getFach() + "(" + (student.getStudienfach().getAbschluss()
                        .equals("Bachelor") ? "B.Sc." : "M.Sc.") + ")")),
                completeZeile("Stellen, die mich interessieren:", studentService.getAllJobKategorien(student.getId_Person()).stream()
                        .map(JobKategorie::getKategorie).collect(Collectors.joining(", "))),
                completeZeile("Bevorzugt in der Nähe von:", studentService.getAllOrte(student.getId_Person()).stream()
                        .map(ort -> ort.getOrt() + " (" + ort.getPLZ() + ")")
                        .collect(Collectors.joining(", "))),
                completeZeile("Bevorzugte Branche(n):", branche),
                completeZeile("Bevorzugte Berufsfelder:", berufsfelder)
        );


        //Kenntnisse
        Div kenntnisseDiv = new Div();
        kenntnisseDiv.addClassName("myTab");

        Faehigkeit ausbildung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Ausbildung");
        if (ausbildung != null) kenntnisseDiv.add(completeZeile("Ausbildung:", ausbildung.getBezeichnung()));

        Faehigkeit erfahrung = faehigkeitService.findSingleFaehigkeitByKategorieForStudent(student, "Praxiserfahrung");
        if (erfahrung != null) kenntnisseDiv.add(completeZeile("Praxiserfahrung:", erfahrung.getBezeichnung()));

        Set<Faehigkeit> sprachen = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "Sprache");
        if (!sprachen.isEmpty()) {
            String[] beschreibungen = sprachen.stream()
                    .map(Faehigkeit::getBezeichnung)
                    .toArray(String[]::new);
            kenntnisseDiv.add(zeileKenn("Sprachen:", beschreibungen));
        }

        Set<Faehigkeit> edv = faehigkeitService.findFaehigkeitByKategorieForStudent(student, "EDV");
        if (!edv.isEmpty()) {
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
        } else if (tab.equals(ueberMich)) {
            content.add(ueberDiv);
        }
    }

    public Div zeileKenn(String beschreibung, String[] wert) {
        Div divReturn = new Div();
        divReturn.addClassName("divReturn");

        Div beschreibungDiv = new Div();
        beschreibungDiv.addClassName("zeileDiv");
        beschreibungDiv.add(beschreibung);

        Div wertDiv = new Div();
        wertDiv.addClassName("zeileDiv");

        for (String s : wert
        ) {
            Span pending = new Span(s);
            pending.getElement().getThemeList().add("badge primary");
            pending.addClassName("badge");
            wertDiv.add(pending);
        }

        divReturn.addClassName("divReturn");
        divReturn.add(beschreibungDiv, wertDiv);

        return divReturn;
    }

    private HorizontalLayout completeZeile(String title, String wert) {

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