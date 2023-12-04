package de.hbrs.easyjob.views.templates;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.views.components.DialogLayout;

@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
public abstract class RegistrierenView extends VerticalLayout {

    //Container für alle Inhalte
    protected VerticalLayout frame = new VerticalLayout();
    //Header Text
    private H1 header = new H1();
    //Container für Buttons
    private VerticalLayout buttonsContainer = new VerticalLayout();
    //Dialog für Abbrechen
    protected DialogLayout abbrechenDialog = new DialogLayout(true);
    //Nächste View und letzte View
    private String nextView;
    private String lastView;
    //Buttons Zurück, Weiter, Fertig
    protected Button next = new Button("Weiter", new Icon(VaadinIcon.ARROW_RIGHT));
    private Button back = new Button("Zurück", new Icon(VaadinIcon.ARROW_LEFT));
    protected Button fertig = new Button("Fertig", new Icon(VaadinIcon.CHECK));
    //Die Person die sich registriert
    protected Person person;

    public RegistrierenView(){

        addClassName("body");
        header.addClassName("header");

        back.addClassName("back");
        back.addClickListener(e -> {back.getUI().ifPresent(ui -> ui.navigate(lastView));});

        next.addClassName("next");

        Button cancel = new Button("Abbrechen", e -> abbrechenDialog.openDialogOverlay());
        cancel.addClassName("cancel");

        //Container für Buttons füllen
        HorizontalLayout flip = new HorizontalLayout(back, next);
        flip.setSpacing(true);
        flip.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonsContainer.add(flip, cancel);
        buttonsContainer.setSpacing(false);
        buttonsContainer.setAlignItems(Alignment.CENTER);


        frame.setClassName("container");
        frame.add(header);
        frame.setAlignItems(Alignment.CENTER);
        frame.setAlignSelf(Alignment.CENTER);
        add(frame);
    }

    protected void setNextView(String nextView){
        this.nextView = nextView;
    }
    public String getNextView() {
        return nextView;
    }
    protected void setLastView(String lastView){
        this.lastView = lastView;
    }
    protected void setHeader(String titel){
    header.add(titel);
    }
    protected void addButtons(){frame.add(buttonsContainer);
    }
    public void addFertigButton(){

        fertig.addClassName("next");
        HorizontalLayout flip = new HorizontalLayout(back, fertig);
        buttonsContainer.replace(buttonsContainer.getComponentAt(0), flip);
    }
    public void registerButtons(Button register, Button cancel){
        buttonsContainer.removeAll();
        register.addClassName("register");
        cancel.addClassName("cancel-register");
        cancel.addClickListener(e -> cancel.getUI().ifPresent(ui -> ui.navigate("login")));
        buttonsContainer.add(register, cancel);
        frame.add(buttonsContainer);
    }
    public void setAbbrechenDialog(String user){
        //Abbrechen Dialog
        if (user.equals("Student")) {
            abbrechenDialog.insertDialogContent("Möchtest du den Vorgang wirklich abbrechen?"
                    , "Deine Eingaben werden nicht gespeichert!", "Nein, hier bleiben."
                    , "Ja, abbrechen.", "Student", "login");

        } else if (user.equals("Unternehmen")) {
            abbrechenDialog.insertDialogContent("Möchten Sie den Vorgang wirklich abbrechen?"
                    , "Ihre Eingaben werden nicht gespeichert!", "Nein, hier bleiben."
                    , "Ja, abbrechen.", "Unternehmen", "login");
        }
    }

    public abstract void insertContent();
}
