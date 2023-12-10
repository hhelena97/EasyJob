package de.hbrs.easyjob.views.allgemein;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hbrs.easyjob.controllers.LoginController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Unternehmensperson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

@Route("login")
@RouteAlias("")
@PageTitle("Login | EasyJob")
@AnonymousAllowed

public class LoginView extends VerticalLayout {
    @Autowired
    private LoginController loginController;


    public LoginView(){
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
        H2 log =  new H2("Log in");


        //Email Feld
        EmailField validEmailField = new EmailField();
        validEmailField.addClassName("btn");
        validEmailField.setLabel("Email");
        validEmailField.getElement().setAttribute("name", "email");
        validEmailField.setValue("");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setClearButtonVisible(true);

        //Pass
        PasswordField passwordField = new PasswordField();
        passwordField.addClassName("btn");
        passwordField.setLabel("Password");
        passwordField.setValue("");



        //Buttons


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
            boolean authen = loginController.authenticate( validEmailField.getValue(), passwordField.getValue() );
            if(authen){
                //wenn die authentifizierung erfolgreich war, hole die Person aus dem Controller
                Person person = loginController.getPerson();
                // und speichere sie in der Session
                grabAndSetPersonIntoSession(person);

                userDetailsService();


                UI ui = UI.getCurrent();

                if (person instanceof Student){
                    System.out.println("Es ist ein Student.");
                    //weiter zur Studenten-Startseite
                    ui.navigate("student");
                }

                if (person instanceof Unternehmensperson){
                    System.out.println("Es ist eine Unternehmensperson.");
                    //weiter zur Unternehmer-Startseite
                    ui.navigate("unternehmen/unternehmenperson");
                }
                //es ist eine Person, aber kein Student oder Unternehmensperson
                //hier kommt später die Weiterleitung zur Admin-Seite (Sprint 2)

                //alte Methode von Rafi:
                //logButton.getUI().ifPresent(ui ->
                //        ui.navigate("StudentProfilView"));
            }
            //für false kommt hier später noch ein Fehlerhinweis an den Benutzer
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
            // Leitet zur Registrieren-Seite
            ui.navigate("/registrieren");
        });



    }

    //Hilfs-Methode um die Person in der Session zu speichern
    private void grabAndSetPersonIntoSession(Person eingeloggtePerson) {
        UI.getCurrent().getSession().setAttribute("current_User", eingeloggtePerson);
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(loginController.getPerson().getEmail())
                .password(loginController.getPerson().getPasswort())
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);

    }


}