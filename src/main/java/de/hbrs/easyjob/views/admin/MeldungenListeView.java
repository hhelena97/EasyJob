package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Meldung;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.MeldungRepository;
import de.hbrs.easyjob.views.components.AdminLayout;

import java.util.List;

@Route(value = "admin/meldungenListe", layout = AdminLayout.class)
@PageTitle("Meldungen")
@StyleSheet("Variables.css")
@StyleSheet("MeldungenListe.css")
//@RolesAllowed("ROLE_ADMIN")
public class MeldungenListeView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionController sessionController;

    private final MeldungController meldungController;

    private Tab personen;
    private Tab unternehmen;
    private Tab jobs;
    private Tab chats;

    private VerticalLayout content;


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            //event.rerouteTo(LoginView.class);
        }
    }

    public MeldungenListeView(SessionController sessionController, MeldungController meldungController) {
        this.sessionController = sessionController;
        this.meldungController = meldungController;

        Div titeltext = new Div(
                new H3 ("Meldungen"));
        titeltext.addClassName("titeltext");

        Div titelbox = new Div(titeltext);
        titelbox.addClassName("titelbox");

        personen = new Tab("Personen");
        unternehmen = new Tab("Unternehmen");
        jobs = new Tab("Jobs");
        chats = new Tab ("Chats");

        Tabs tabs = new Tabs(personen, unternehmen, jobs, chats);
        tabs.addSelectedChangeListener(
                event -> { setContent(event.getSelectedTab());
                });
        tabs.addClassName("tableiste");

        content = new VerticalLayout();
        setContent(tabs.getSelectedTab());

        add(titelbox, tabs, content);
    }

//Inhalt anzeigen funktioniert noch überhautp nicht
    private void setContent(Tab tab){
        content.removeAll();

        Div personenDiv = new Div();
        personenDiv.addClassName("meldungTab");
        List<Meldung> mp = meldungController.getAllGemeldetePersonen();
        if (!mp.isEmpty()){
            mp.forEach(this::addPersonComponentToLayout);
        }

        Div unternehmenDiv = new Div();
        unternehmenDiv.addClassName("meldungTab");


        Div jobsDiv = new Div();
        jobsDiv.addClassName("meldungTab");

        Div chatsDiv = new Div();
        chatsDiv.addClassName("meldungTab");
        chatsDiv.add("Test Test Test\n");



        if (tab.equals(personen)) {
            content.add(personenDiv);
        } else if (tab.equals(unternehmen)) {
            content.add(unternehmenDiv);
        } else if (tab.equals(jobs)){
            content.add(jobsDiv);
        } else if (tab.equals(chats)){
            content.add(chatsDiv);
        }
    }

    private void addPersonComponentToLayout(Meldung meldung){
        //kleines Bild:

        //Name:
        String name = meldung.getPerson().getVorname() + " " + meldung.getPerson().getNachname();

        RouterLink linkPerson;
        //linkPerson = new RouterLink(name, MeldungDetailPersonView.class, meldung.getPerson());
    }
}
