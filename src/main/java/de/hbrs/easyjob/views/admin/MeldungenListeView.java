package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.PasswortService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.repositories.MeldungRepository;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.AdminStudentProfileComponent;
import de.hbrs.easyjob.views.components.AdminUnternehmenspersonProfileComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "admin/meldungenListe", layout = AdminLayout.class)
@PageTitle("Meldungen")
@StyleSheet("Variables.css")
@StyleSheet("MeldungenListe.css")
@RolesAllowed("ROLE_ADMIN")
public class MeldungenListeView extends VerticalLayout implements BeforeEnterObserver {

    private final MeldungRepository meldungRepository;
    private final SessionController sessionController;

    private final ProfilSperrenController profilSperrenController;

    private final MeldungController meldungController;

    private final StudentService studentService;
    private final UnternehmenService unternehmenService;

    private Tab personen;
    private Tab unternehmen;
    private Tab jobs;
    private Tab chats;

    String style = "AdminProfilVerwalten.css";

    private VerticalLayout content;


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            event.rerouteTo(LoginView.class);
        }
    }

    @Autowired
    public MeldungenListeView(SessionController sessionController, MeldungRepository meldungRepository,
            MeldungController meldungController, ProfilSperrenController profilSperrenController,
                              StudentService studentenService, UnternehmenService unternehmenService) {
        this.meldungRepository = meldungRepository;
        this.sessionController = sessionController;
        this.meldungController = meldungController;
        this.profilSperrenController = profilSperrenController;
        this.studentService = studentenService;
        this.unternehmenService = unternehmenService;

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

//Liste der Meldungen
    private void setContent(Tab tab){
        content.removeAll();

        Div personenDiv = new Div();
        personenDiv.addClassName("meldungTab");
        List<Meldung> mp1 = meldungController.getAllGemeldetePersonen();
        if (!mp1.isEmpty()){
            for (Meldung m: mp1) {
                addPersonComponentToLayout(m);
            }
        }

        Div unternehmenDiv = new Div();
        unternehmenDiv.addClassName("meldungTab");
        List<Meldung> mp2 = meldungController.getAllGemeldeteUnternehmen();
        if (!mp2.isEmpty()) {
            for (Meldung m: mp2) {
                addUnternehmenComponentToLayout(m);
            }
        }

        Div jobsDiv = new Div();
        jobsDiv.addClassName("meldungTab");
        List<Meldung> mp3 = meldungController.getAllGemeldeteJobs();
        if (!mp3.isEmpty()) {
            for (Meldung m: mp3) {
                addJobComponentToLayout(m);
            }
        }

        Div chatsDiv = new Div();
        chatsDiv.addClassName("meldungTab");
        List<Meldung> mp4 = meldungController.getAllGemeldeteChats();
        if (!mp4.isEmpty()) {
            for (Meldung m: mp4) {
                addChatComponentToLayout(m);
            }
        }

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

        Person p = meldung.getPerson();

        String name = p.getVorname() + " " + p.getNachname();

        Dialog d1 = new Dialog();
        d1.setHeaderTitle("Meldung bearbeiten");

            Div infos = new Div();

                //Profil Bild
                VerticalLayout profilBild = new VerticalLayout();
                profilBild.setWidth("200px");
                profilBild.addClassName("profilBild");
                profilBild.add(new Image(p.getFoto() != null ? p.getFoto() : "images/blank-profile-picture.png", "EasyJob"));

                //Name
                H2 nametitel = new H2();
                nametitel.addClassName("name");
                nametitel.add(p.getVorname() + " " + p.getNachname());

            infos.add(profilBild, nametitel);


            Div buttons = new Div();
                //Person sperren
                //Dialog zum Nachfragen
                Dialog wirklichSperren = new Dialog();
                personSperrenDialog(p,wirklichSperren);

                Button btnSperren = new Button("Sperren");
                btnSperren.addClassName("btnSperren");
                btnSperren.addClickListener(e -> wirklichSperren.open());
            buttons.add(btnSperren);

            VerticalLayout personLayout = new VerticalLayout();

            if (p instanceof Student) {
                Student student = (Student) p;
                personLayout.add(new AdminStudentProfileComponent(student, "AdminProfilVerwalten.css", studentService));
            } else if (p instanceof Unternehmensperson) {
                Unternehmensperson uperson = (Unternehmensperson) p;
                personLayout.add(new AdminUnternehmenspersonProfileComponent(uperson, "AdminProfilVerwalten.css", unternehmenService));
            } else if (p instanceof Admin) {
                Paragraph admininfo = new Paragraph("Das ist ein Admin.");
                Paragraph adminlink = new Paragraph("Zur Administration");
                RouterLink linkAdmin = new RouterLink(EinstellungenStartView.class);
                linkAdmin.add(adminlink);
                personLayout.add(admininfo, linkAdmin);
            } else {
                Paragraph paragraph = new Paragraph("Die Person wurde nicht gefunden");
                paragraph.addClassName("nicht-gefunden");
                personLayout.add(paragraph);
            }

        d1.add(infos,buttons,personLayout);

        Button btnPerson = new Button(name);
        btnPerson.addClassName("PersonAufListeButton");
        btnPerson.addClickListener(e -> d1.open());

    }


    private void addUnternehmenComponentToLayout(Meldung meldung){

        String uname = meldung.getUnternehmen().getName();

        //Dialog-Fenster mit den Details zur Person
        // Header: Meldung bearbeiten
        //Inhalt: PersonComponente
        //footer: abbrechen oder Meldung bearbeitet
    }

    private void addJobComponentToLayout(Meldung meldung){

        String jobtitel = meldung.getJob().getTitel();

        //Dialog-Fenster mit den Details zur Person
        // Header: Meldung bearbeiten
        //Inhalt: PersonComponente
        //footer: abbrechen oder Meldung bearbeitet
    }

    private void addChatComponentToLayout(Meldung meldung){
        //kleines Bild:

        //Name:
        String name = meldung.getPerson().getVorname() + " " + meldung.getPerson().getNachname();

        //Dialog-Fenster mit den Details zur Person
        // Header: Chat-Meldung bearbeiten
        //Inhalt: PersonComponente
        //footer: abbrechen oder Meldung bearbeitet
    }


    private void personSperrenDialog(Person person, Dialog dialog){

        dialog.add(new Paragraph("Wollen Sie " + person.getVorname() + " " + person.getNachname() + " sperren?"));

        Button btnAbbruch2 = new Button("abbrechen");
        btnAbbruch2.addClassName("buttonAbbruch");
        btnAbbruch2.addClickListener(e -> {
            dialog.close();
        });

        Button btnBestaetigen = new Button("Person sperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            profilSperrenController.personSperren(person);
            dialog.close();
        });
        dialog.getFooter().add(btnAbbruch2, btnBestaetigen);
    }


}
