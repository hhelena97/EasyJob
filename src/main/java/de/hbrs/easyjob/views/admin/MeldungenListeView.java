package de.hbrs.easyjob.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.MeldungController;
import de.hbrs.easyjob.controllers.ProfilSperrenController;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.*;
import de.hbrs.easyjob.services.FaehigkeitService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.services.UnternehmenService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "admin/meldungenListe", layout = AdminLayout.class)
@PageTitle("Meldungen")
@StyleSheet("Variables.css")
@StyleSheet("AdminMeldungenListe.css")
@RolesAllowed("ROLE_ADMIN")
public class MeldungenListeView extends VerticalLayout implements BeforeEnterObserver {
    // Repositories
    private final transient MeldungRepository meldungRepository;

    // Services
    private final transient FaehigkeitService faehigkeitService;
    private final transient StudentService studentService;
    private final transient UnternehmenService unternehmenService;

    // Controller
    private final transient MeldungController meldungController;
    private final transient ProfilSperrenController profilSperrenController;
    private final transient SessionController sessionController;
    Paragraph keineMeldung = new Paragraph("Keine Meldungen");


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!sessionController.isLoggedIn() || !sessionController.hasRole("ROLE_ADMIN")) {
            event.rerouteTo(LoginView.class);
        }
    }

    public MeldungenListeView(
            MeldungRepository meldungRepository,
            FaehigkeitService faehigkeitService,
            StudentService studentService,
            UnternehmenService unternehmenService,
            MeldungController meldungController,
            ProfilSperrenController profilSperrenController,
            SessionController sessionController
    ) {
        this.meldungRepository = meldungRepository;
        this.faehigkeitService = faehigkeitService;
        this.studentService = studentService;
        this.unternehmenService = unternehmenService;
        this.meldungController = meldungController;
        this.profilSperrenController = profilSperrenController;
        this.sessionController = sessionController;


        Div btnAusloggen = new AdminAusloggen(sessionController);
        HorizontalLayout ausloggen = new HorizontalLayout(btnAusloggen);
        ausloggen.addClassName("ausloggenFenster");


        Div titeltext = new Div(new H3("Meldungen"));
        titeltext.addClassName("titeltext");


        TabSheet tabs = new TabSheet();
        tabs.add("Personen", personenDiv());
        tabs.add("Unternehmen", unternehmenDiv());
        tabs.add("Jobs", jobsDiv());
        tabs.add("Chats", chatsDiv());

        tabs.addClassName("tableiste");

        VerticalLayout titelbox = new VerticalLayout(ausloggen, titeltext);
        titelbox.addClassName("gruene-box");

        add(titelbox, tabs);
    }

    private Div personenDiv() {
        Div inhalt = new Div();

        List<Meldung> mp1 = meldungController.getAllGemeldetePersonen();
        if (mp1 == null || mp1.isEmpty()) {
            inhalt.add(keineMeldung);
        } else {
            for (Meldung m : mp1) {
                inhalt.add(addPersonComponentToLayout(m));
            }
        }
        return inhalt;
    }

    private Div unternehmenDiv() {
        Div inhalt = new Div();

        List<Meldung> mp = meldungController.getAllGemeldeteUnternehmen();
        if (mp == null || mp.isEmpty()) {
            inhalt.add(keineMeldung);
        } else {
            for (Meldung m : mp) {
                inhalt.add(addUnternehmenComponentToLayout(m));
            }
        }
        return inhalt;
    }

    private Div jobsDiv() {
        Div inhalt = new Div();

        List<Meldung> mp = meldungController.getAllGemeldeteJobs();
        if (mp.isEmpty()) {
            inhalt.add(keineMeldung);
        } else {
            for (Meldung m : mp) {
                inhalt.add(addJobComponentToLayout(m));
            }
        }
        return inhalt;
    }

    private Div chatsDiv() {
        Div inhalt = new Div();

        List<Meldung> mp = meldungController.getAllGemeldeteChats();
        if (mp == null || mp.isEmpty()) {
            inhalt.add(keineMeldung);
        } else {
            for (Meldung m : mp) {
                inhalt.add(addPersonComponentToLayout(m));
            }
        }
        return inhalt;
    }

    private Div addPersonComponentToLayout(Meldung meldung) {

        Person p = meldung.getPerson();

        String name = p.getVorname() + " " + p.getNachname();

        //Meldung overlay
        Dialog d1 = new Dialog();
        d1.setHeaderTitle("Meldung bearbeiten");

        Div infos = new Div();
        infos.addClassName("infos");

        String foto = p.getFoto() != null ? p.getFoto() : "images/blank-profile-picture.png";
        //Profil Bild
        VerticalLayout profilBild = new VerticalLayout(new Image(foto, "EasyJob"));
        profilBild.addClassName("profilBild");

        //Name
        H2 nametitel = new H2();
        nametitel.addClassName("name");
        nametitel.add(p.getVorname() + " " + p.getNachname());

        infos.add(profilBild, nametitel);


        Div buttons = new Div();
        //Person sperren
        //Dialog zum Nachfragen
        Dialog wirklichSperren = new Dialog();
        personSperrenDialog(p, wirklichSperren);

        Button btnSperren = new Button("Sperren");
        btnSperren.addClassName("btnSperren");
        btnSperren.addClickListener(e -> wirklichSperren.open());
        buttons.add(btnSperren);

        VerticalLayout personLayout = new VerticalLayout();

        if (p instanceof Student student) {
            personLayout.add(new AdminStudentProfileComponent(
                    student,
                    studentService,
                    faehigkeitService
            ));
        } else if (p instanceof Unternehmensperson uperson) {
            personLayout.add(new AdminUnternehmenspersonProfileComponent(
                    uperson,
                    unternehmenService,
                    profilSperrenController
            ));
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
        meldungBearbeitet.addClickListener(e -> {
            meldung.setBearbeitet(true);
            meldungRepository.save(meldung);
            Notification.show("Die Meldung wurde bearbeitet");
            d1.close();
        });

        Button meldungSchliessen = new Button("Abbrechen");
        meldungSchliessen.addClickListener(e -> d1.close());

        d1.add(infos, buttons, personLayout);
        d1.getFooter().add(meldungSchliessen, meldungBearbeitet);

        Button btnPerson = new Button(name);
        btnPerson.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnPerson.addClickListener(e -> d1.open());

        return new Div(btnPerson);
    }


    private Div addUnternehmenComponentToLayout(Meldung meldung) {

        String uname = meldung.getUnternehmen().getName();

        Dialog d1 = new Dialog();
        d1.setHeaderTitle("Meldung bearbeiten");

        Div infos = new Div();
        infos.add(new AdminUnternehmenManageComponent(
                meldung.getUnternehmen(),
                profilSperrenController,
                unternehmenService
        ));


        d1.add(infos);

        Button meldungBearbeitet = new Button("Meldung bearbeitet");
        meldungBearbeitet.addClassName("MeldungBearbeitet");
        meldungBearbeitet.addClickListener(e -> {
            meldung.setBearbeitet(true);
            meldungRepository.save(meldung);
            Notification.show("Die Meldung wurde bearbeitet");
            d1.close();
        });

        Button meldungSchliessen = new Button("Meldung schließen");
        meldungSchliessen.addClassName("MeldungSchliessen");
        meldungSchliessen.addClickListener(e -> d1.close());


        d1.getFooter().add(meldungSchliessen, meldungBearbeitet);

        Button btnUnternehmen = new Button(uname);
        btnUnternehmen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnUnternehmen.addClickListener(e -> d1.open());

        return new Div(btnUnternehmen);
    }

    private Div addJobComponentToLayout(Meldung meldung) {

        String jname = meldung.getJob().getTitel();

        Dialog d1 = new Dialog();
        d1.setHeaderTitle("Meldung bearbeiten");

        Div infos = new Div();
        infos.add(new AdminJobComponent(meldung.getJob(), profilSperrenController));

        d1.add(infos);

        Button meldungBearbeitet = new Button("Meldung bearbeitet");
        //meldungBearbeitet.addClassName("MeldungBearbeitet");
        meldungBearbeitet.addClickListener(e -> {
            meldung.setBearbeitet(true);
            meldungRepository.save(meldung);
            Notification.show("Die Meldung wurde bearbeitet");
            d1.close();
        });

        Button meldungSchliessen = new Button("abbrechen");
        //meldungSchliessen.addClassName("MeldungSchliessen");
        meldungSchliessen.addClickListener(e -> d1.close());


        d1.getFooter().add(meldungSchliessen, meldungBearbeitet);

        Button btnJob = new Button(jname);
        btnJob.addClassName("JobAufListeButton");
        btnJob.addClickListener(e -> d1.open());

        return new Div(btnJob);
    }


    private void personSperrenDialog(Person person, Dialog dialog) {

        dialog.add(new Paragraph("Möchten Sie " + person.getVorname() + " " + person.getNachname() + " sperren?"));

        Button btnAbbruch2 = new Button("abbrechen");
        btnAbbruch2.addClassName("buttonAbbruch");
        btnAbbruch2.addClickListener(e -> dialog.close());

        Button btnBestaetigen = new Button("Person sperren");
        btnBestaetigen.addClassName("buttonBestaetigen");
        btnBestaetigen.addClickListener(e -> {
            profilSperrenController.personSperren(person);
            Notification.show("Die Person wurde gesperrt");
            UI.getCurrent().getPage().setLocation("/admin/meldungenListe");
            dialog.close();
        });
        dialog.getFooter().add(btnAbbruch2, btnBestaetigen);
    }


}
