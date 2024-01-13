package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.controllers.SessionController;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;


@Route("login")
@RouteAlias("")
@PageTitle("Login | EasyJob")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final transient SessionController sessionController;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (sessionController.isLoggedIn()) {
            if (sessionController.hasRole("ROLE_ADMIN")) {
                event.rerouteTo("admin");
            } else if (sessionController.hasRole("ROLE_STUDENT")) {
                event.rerouteTo("student");
            } else if (sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
                event.rerouteTo("unternehmen/unternehmenperson");
            } else {
                Notification.show("Unbekannte Benutzerrolle. Logge Nutzer aus.");
                sessionController.logout();
            }
        }
    }


    public LoginView(SessionController sessionController) {
        this.sessionController = sessionController;

        VaadinService.getCurrentResponse().setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        VaadinService.getCurrentResponse().setHeader("Pragma", "no-cache");
        VaadinService.getCurrentResponse().setHeader("Expires", "-1");
        UI.getCurrent().getPage().addStyleSheet("LoginView.css");


        //Logo Teil----------------------------------------------------------------
        VerticalLayout div = new VerticalLayout(new Image("images/logo_icon.png", "EasyJob"));

        div.setWidth(getMaxWidth());
        div.setAlignItems(CENTER);
        div.addClassName("logo");


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
        v.setAlignItems(CENTER);
        setJustifyContentMode(JustifyContentMode.START);


        //Form----------------------------------------------------------------

        //Title
        H2 log = new H2("Log in");


        //Email Feld
        EmailField validEmailField = new EmailField();
        validEmailField.addClassName("btn");
        validEmailField.setLabel("Email");
        validEmailField.getElement().setAttribute("name", "email");
        validEmailField.setValue("");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setClearButtonVisible(true);
        validEmailField.setId("emailloginfeld_id");

        //Pass
        PasswordField passwordField = new PasswordField();
        passwordField.addClassName("btn");
        passwordField.setLabel("Password");
        passwordField.setValue("");
        passwordField.setId("passwordloginfeld_id");


        //Buttons


        Button logButton = new Button("Einloggen");
        logButton.addClassName("btn");
        logButton.addClassName("logButton");
        logButton.addClassName("log");
        logButton.setId("loginbutton_id_loginpage");

        logButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button verButton = new Button("Passwort vergessen?");
        verButton.addClassName("verButton");
        verButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button regButton = new Button("Jetzt registrieren");
        regButton.addClassName("btn");
        regButton.addClassName("regButton");
        regButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        regButton.setId("registrierenbutton_id");

        VerticalLayout fenster = new VerticalLayout(log, validEmailField, passwordField, verButton, logButton, regButton);
        fenster.setAlignItems(Alignment.CENTER);
        fenster.setJustifyContentMode(JustifyContentMode.CENTER);
        fenster.setSpacing(false);

        add(v, fenster);


        logButton.addClickListener(e -> {
            String username = validEmailField.getValue();
            String password = passwordField.getValue();
            if (!sessionController.login(username, password)) {
                Notification.show("Authentifizierung fehlgeschlagen.");
            } else if (sessionController.hasRole("ROLE_ADMIN")) {
                UI.getCurrent().navigate("admin");
            } else if (sessionController.hasRole("ROLE_STUDENT")) {
                UI.getCurrent().navigate("student");
            } else if (sessionController.hasRole("ROLE_UNTERNEHMENSPERSON")) {
                UI.getCurrent().navigate("unternehmen/unternehmenperson");
            } else {
                Notification.show("Unbekannte Benutzerrolle.");
            }
        });


        verButton.addClickListener(event -> {
            // Ruft die UI-Instanz ab
            UI ui = UI.getCurrent();
            // Leitet zur gewünschten Seite
            ui.navigate("passwortVergessen");
        });

        regButton.addClickListener(event -> {
            // Ruft die UI-Instanz ab
            UI ui = UI.getCurrent();
            // Leitet zur Registrieren-Seite
            ui.navigate("/registrieren");
        });
    }
}