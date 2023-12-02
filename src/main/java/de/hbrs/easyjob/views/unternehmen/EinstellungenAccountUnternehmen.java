package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.repository.PersonRepository;
import de.hbrs.easyjob.repository.UnternehmenRepository;
import de.hbrs.easyjob.views.allgemein.DeaktivierenConfirmDialogView;
import de.hbrs.easyjob.control.ProfilDeaktivierenControl;
import org.springframework.beans.factory.annotation.Autowired;

@Route("EinstellungenAccountUnternehmen")
@PageTitle("Accounteinstellungen")
@StyleSheet("Registrieren.css")
@StyleSheet("DialogLayout.css")
public class EinstellungenAccountUnternehmen extends VerticalLayout implements BeforeLeaveObserver {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UnternehmenRepository unternehmenRepository;

    private ProfilDeaktivierenControl profilDeaktivieren = new ProfilDeaktivierenControl(personRepository, unternehmenRepository);

    public EinstellungenAccountUnternehmen() {
        VerticalLayout frame = new VerticalLayout();
        DeaktivierenConfirmDialogView deaktivierenDialog = new DeaktivierenConfirmDialogView(true, "Unternehmen", "Ihr Profil wird unsichtbar und Sie können keine Nachrichten mehr erhalten. Das Unternehmensprofil bleibt sichbar, solange mindestens ein verbundenes Profil aktiv ist. Sie können Ihren Account jederzeit reaktivieren.");

        frame.setClassName("Container");
        Button back = new Button("", new Icon(VaadinIcon.ARROW_LEFT));

        Label ueber = new Label("Accounteinstellungen");

        Button deaktivieren = new Button("Account deaktivieren", e -> deaktivierenDialog.openDialogOverlay());


        frame.add(back, ueber, deaktivieren);
        add(frame);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // Deaktiviere Unternehmen-Account
        Unternehmensperson person = (Unternehmensperson) UI.getCurrent().getSession().getAttribute("current_User");
        if (profilDeaktivieren.profilDeaktivierenUnternehmen(person)) {
            System.out.printf("Unternehmen '%s' deaktiviert.\n", person.getUnternehmen().getName());
        }
    }
}
