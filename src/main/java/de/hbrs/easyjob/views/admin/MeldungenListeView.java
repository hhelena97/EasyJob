package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.ProfilDeaktivierenController;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.AdminLayout;
import de.hbrs.easyjob.views.components.AdminStudentProfileComponent;
import de.hbrs.easyjob.views.components.AdminUnternehmenComponent;
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
    private final PersonRepository personRepository;
    private final UnternehmenRepository unternehmenRepository;

    private final UnternehmenspersonRepository unternehmenspersonRepository;
    private final JobRepository jobRepository;

    private final SessionController sessionController;

    //private final ProfilSperrenController profilSperrenController;

    private final MeldungController meldungController;

    private final StudentService studentService;
    private final UnternehmenService unternehmenService;

    private Tab personen;
    private Tab unternehmen;
    private Tab jobs;
    private Tab chats;

    //Noch ändern auf das CSS indem Katharina das Profil anzeigen hübsch gemacht hat.
    String style = "AdminProfilVerwalten.css";

    private VerticalLayout content = new VerticalLayout();


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            event.rerouteTo(LoginView.class);
        }
    }

    //@Autowired
    public MeldungenListeView(SessionController sessionController, MeldungRepository meldungRepository,
            PersonRepository personRepository, UnternehmenRepository unternehmenRepository, JobRepository jobRepository,
            UnternehmenspersonRepository unternehmenspersonRepository,
            MeldungController meldungController, StudentService studentenService, UnternehmenService unternehmenService) {
        this.meldungRepository = meldungRepository;
        this.personRepository = personRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.jobRepository = jobRepository;
        this.unternehmenspersonRepository = unternehmenspersonRepository;
        this.sessionController = sessionController;
        this.meldungController = meldungController;
        //this.profilSperrenController = new ProfilSperrenController(personRepository, unternehmenRepository, jobRepository, unternehmenspersonRepository);
        this.studentService = studentenService;
        this.unternehmenService = unternehmenService;

        Div titeltext = new Div(new H3 ("Meldungen"));
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

        titelbox.add(tabs);

        add(titelbox, tabs, content);
    }

//Liste der Meldungen
    private void setContent(Tab tab) {
        content.removeAll();
        Paragraph keineMeldung = new Paragraph("Keine Meldungen");

        Div personenDiv = new Div();
        personenDiv.addClassName("meldungTab");
        List<Meldung> mp1 = meldungController.getAllGemeldetePersonen();
        if (!mp1.isEmpty()) {
            for (Meldung m : mp1) {
                personenDiv.add(addPersonComponentToLayout(m));
            }
        } else{
            personenDiv.add(keineMeldung);
        }

        Div unternehmenDiv = new Div();
        unternehmenDiv.addClassName("meldungTab");
        List<Meldung> mp2 = meldungController.getAllGemeldeteUnternehmen();
        if (!mp2.isEmpty()) {
            for (Meldung m : mp2) {
                unternehmenDiv.add(addUnternehmenComponentToLayout(m));
            }
        } else {
            unternehmenDiv.add(keineMeldung);
        }

        Div jobsDiv = new Div();
        jobsDiv.addClassName("meldungTab");
        List<Meldung> mp3 = meldungController.getAllGemeldeteJobs();
        if (!mp3.isEmpty()) {
            for (Meldung m : mp3) {
                //addJobComponentToLayout(m);
            }
        } else {
            jobsDiv.add(keineMeldung);
        }

        Div chatsDiv = new Div();
        chatsDiv.addClassName("meldungTab");
        List<Meldung> mp4 = meldungController.getAllGemeldeteChats();
        if (!mp4.isEmpty()) {
            for (Meldung m : mp4) {
                chatsDiv.add(addPersonComponentToLayout(m));
            }
        } else {
            chatsDiv.add(keineMeldung);
        }

        if (tab.equals(personen)) {
            content.add(personenDiv);
        } else if (tab.equals(unternehmen)) {
            content.add(unternehmenDiv);
        } else if (tab.equals(jobs)) {
            content.add(new Paragraph("JobListe"));
            //content.add(jobsDiv);
        } else if (tab.equals(chats)) {
            content.add(chatsDiv);
        }
    }

    private Div addPersonComponentToLayout(Meldung meldung){

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
                //Unternehmensperson uperson = (Unternehmensperson) p;
                //personLayout.add(new AdminUnternehmenspersonProfileComponent(uperson, "AdminProfilVerwalten.css", unternehmenService));
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

        Button meldungBearbeitet = new Button("Meldung bearbeitet");
        meldungBearbeitet.addClassName("MeldungBearbeitet");
        meldungBearbeitet.addClickListener(e -> {
            meldung.setBearbeitet(true);
            meldungRepository.save(meldung);
            Notification.show("Die Meldung wurde bearbeitet");
            d1.close();
        });

        Button meldungSchliessen = new Button ("Meldung schließen");
        meldungSchliessen.addClassName("MeldungSchliessen");
        meldungSchliessen.addClickListener(e -> d1.close());

        d1.add(infos,buttons,personLayout);
        d1.getFooter().add(meldungSchliessen, meldungBearbeitet);

        Button btnPerson = new Button(name);
        btnPerson.addClassName("PersonAufListeButton");
        btnPerson.addClickListener(e -> d1.open());

        return new Div(btnPerson);
    }


    private Div addUnternehmenComponentToLayout(Meldung meldung){

        String uname = meldung.getUnternehmen().getName();

        Dialog d1 = new Dialog();
        d1.setHeaderTitle("Meldung bearbeiten");

        Div infos = new Div();
            infos.add(new AdminUnternehmenComponent(meldung.getUnternehmen() ,"AdminLayout.css",
                    new ProfilDeaktivierenController(personRepository, unternehmenRepository),unternehmenService));


        d1.add(infos);

        Button meldungBearbeitet = new Button("Meldung bearbeitet");
            meldungBearbeitet.addClassName("MeldungBearbeitet");
            meldungBearbeitet.addClickListener(e -> {
                meldung.setBearbeitet(true);
                meldungRepository.save(meldung);
                Notification.show("Die Meldung wurde bearbeitet");
                d1.close();
            });

        Button meldungSchliessen = new Button ("Meldung schließen");
            meldungSchliessen.addClassName("MeldungSchliessen");
            meldungSchliessen.addClickListener(e -> d1.close());


        d1.getFooter().add(meldungSchliessen, meldungBearbeitet);

        Button btnUnternehmen = new Button(uname);
            btnUnternehmen.addClassName("UnternehmenAufListeButton");
            btnUnternehmen.addClickListener(e -> d1.open());

        return new Div(btnUnternehmen);
    }

    private void addJobComponentToLayout(Meldung meldung){

        String jobtitel = meldung.getJob().getTitel();

        //Dialog-Fenster mit den Details zur Person
        // Header: Meldung bearbeiten
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
            ProfilSperrenController profilSperrenController = new ProfilSperrenController(personRepository,unternehmenRepository,jobRepository,unternehmenspersonRepository);
            profilSperrenController.personSperren(person);
            Notification.show("Die Person wurde gesperrt");
            UI.getCurrent().getPage().setLocation("/admin/meldungenListe");
            dialog.close();
        });
        dialog.getFooter().add(btnAbbruch2, btnBestaetigen);
    }


}
