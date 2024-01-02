package de.hbrs.easyjob.views.allgemein;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;


@Route("login")
@RouteAlias("")
@PageTitle("Login | EasyJob")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    private void resetSessionAttributes() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(SecurityContext.class, null);
        }
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        resetSessionAttributes();
    }


    public LoginView(){
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
        H2 log =  new H2("Log in");


        //Email Feld
        EmailField validEmailField = new EmailField();
        validEmailField.addClassName("btn");
        validEmailField.setLabel("Email");
        validEmailField.getElement().setAttribute("name", "email");
        validEmailField.setValue("");
        validEmailField.setErrorMessage("Enter a valid email address");
        validEmailField.setId("emailloginfeld_id");
        validEmailField.setClearButtonVisible(true);

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

        VerticalLayout fenster = new VerticalLayout(log,validEmailField,passwordField,verButton,logButton,regButton);
        fenster.setAlignItems(Alignment.CENTER);
        fenster.setJustifyContentMode(JustifyContentMode.CENTER);
        fenster.setSpacing(false);

        add(v,fenster);


        logButton.addClickListener(e -> {
            try {
                String username = validEmailField.getValue();
                String password = passwordField.getValue();
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);
                HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
                HttpServletResponse response = VaadinServletResponse.getCurrent().getHttpServletResponse();
                customSecurityContextRepository.saveContext(sc, request, response);

                if (hasRole(authentication, "ROLE_STUDENT")) {
                    UI.getCurrent().navigate("student");
                } else if (hasRole(authentication, "ROLE_UNTERNEHMENSPERSON")) {
                    UI.getCurrent().navigate("unternehmen/unternehmenperson");
                } else {

                    Notification.show("Unbekannte Benutzerrolle.");
                }
            } catch (AuthenticationException ex) {
                Notification.show("Authentifizierung fehlgeschlagen.");
            }
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

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}