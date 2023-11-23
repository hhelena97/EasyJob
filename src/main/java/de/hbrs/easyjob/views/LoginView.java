package de.hbrs.easyjob.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.control.LoginControl;
import de.hbrs.easyjob.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

@Route("login")
@RouteAlias("")
@PageTitle("Login | EasyJob")
@AnonymousAllowed
public class LoginView extends VerticalLayout {
    @Autowired
    private LoginControl loginControl;

    public LoginView(){
        UI.getCurrent().getPage().addStyleSheet("LoginView.css");

        Div div = new Div();
        div.addClassName("divLogin");
        div.setWidth(getMaxWidth());

        addClassName("LoginView-view");
        setSizeFull();
        setAlignItems(CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(false);

        VerticalLayout v = new VerticalLayout(div);
        v.addClassName("v");
        v.setSizeFull();
        v.setHeight("30%");
        v.setPadding(false);
        v.setMargin(false);
        v.setAlignItems(START);
        setJustifyContentMode(JustifyContentMode.START);

        H2 log =  new H2("Log in");

        EmailField validEmailField = new EmailField();
        validEmailField.addClassName("btn");
        validEmailField.setLabel("Email");
        validEmailField.getElement().setAttribute("name", "email");
        validEmailField.setValue("");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setClearButtonVisible(true);

        PasswordField passwordField = new PasswordField();
        passwordField.addClassName("btn");
        passwordField.setLabel("Password");
        passwordField.setValue("");

        Button logButton = new Button("Einloggen");
        logButton.addClassName("btn");
        logButton.addClassName("logButton");
        logButton.addClassName("log");

        logButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button verButton = new Button("Passwort vergessen?");
        verButton.addClassName("verButton");
        verButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button regButton = new Button("Jetzt registrieren");
        regButton.addClassName("btn");
        regButton.addClassName("regButton");
        regButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        VerticalLayout fenster = new VerticalLayout(log,validEmailField,passwordField,verButton,logButton,regButton);
        fenster.setAlignItems(Alignment.CENTER);
        fenster.setJustifyContentMode(JustifyContentMode.CENTER);
        fenster.setSpacing(false);

        add(v,fenster);

//Wenn jemand auf einen Button drückt, wird der entsprechende Listener aktiv und startet das Event
        logButton.addClickListener(e -> {
            loginControl.authenticate( validEmailField.getValue(), passwordField.getValue() );
        });

        verButton.addClickListener(event -> {
            // Ruft die UI-Instanz ab
            UI ui = UI.getCurrent();
            // Leitet zur gewünschten Seite
            //ui.navigate("passwortVergessen");
            System.out.println("Passwort vergessen gedrückt");
        });

        regButton.addClickListener(event -> {
            // Ruft die UI-Instanz ab
            UI ui = UI.getCurrent();
            // Leitet zur gewünschten Seite
            //ui.navigate("registrieren");
            System.out.println("User will zur Registrierung");

        });


    }

}