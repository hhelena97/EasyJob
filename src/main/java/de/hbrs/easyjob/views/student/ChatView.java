package de.hbrs.easyjob.views.student;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.DatabaseMessagePersister;
import de.hbrs.easyjob.services.JobService;
import de.hbrs.easyjob.views.components.StudentLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "chat/", layout = StudentLayout.class)
@StyleSheet("ChatStudent.css")
@RolesAllowed("ROLE_STUDENT")
public class ChatView extends VerticalLayout implements HasUrlParameter<String> {

    private final DatabaseMessagePersister databaseMessagePersister;

    private final SessionController sessionController;
    private final JobService jobService;

    @Autowired
    public ChatView(DatabaseMessagePersister databaseMessagePersister, SessionController sessionController, JobService jobService) {
        this.databaseMessagePersister = databaseMessagePersister;
        this.sessionController = sessionController;
        this.jobService = jobService;
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        String[] params = parameter.split("/");
        if (params.length == 2) {
            try {
                String topic = params[0];
                Integer id = Integer.parseInt(params[1]);
                Person person = sessionController.getPerson();
                String topicId = topic + "-" + id + "-"+ person.getId_Person();
                if(topic.equals("Job")) {
                    setupChatForJob(id, person, topicId);
                } else if(topic.equals("Student")) {
                    setupChatForstudent(id, person, topicId);
                } else {
                    add(new H1("Fehler"));
                }

            } catch (NumberFormatException e) {
                // Wenn jobId oder studentId keine Zahl ist, geben Sie eine Fehlermeldung aus
                add(new H1("Fehler"));

            }
        }

    }

    private void setupChatForstudent(Integer id, Person person, String topic) {
        String foto = person.getFoto()!=null ? person.getFoto() : "images/blank-profile-picture.png";
        UserInfo currentUserInfo = new UserInfo(person.getId_Person().toString(), person.getVorname() + " " + person.getNachname(), foto);

        Component header = createChatHeader(jobService.getJobById(id));
        // Erstellen der Collaboration Engine-Komponenten
        CollaborationMessageList messageList = new CollaborationMessageList(currentUserInfo, topic,databaseMessagePersister);

        CollaborationMessageInput messageInput = new CollaborationMessageInput(messageList);

        // Layout für den Nachrichtenbereich
        VerticalLayout messageLayout = new VerticalLayout(messageList, messageInput);
        messageLayout.setSizeFull();
        messageLayout.setFlexGrow(0, messageList);
        messageLayout.setFlexGrow(0, messageInput);
        messageLayout.setAlignItems(Alignment.STRETCH);

        // Gesamtes Chat-Layout
        VerticalLayout chatLayout = new VerticalLayout(header, messageLayout);
        chatLayout.setFlexGrow(0, header);
        chatLayout.setFlexGrow(1, messageLayout);
        chatLayout.setSizeFull();
        chatLayout.setAlignItems(Alignment.STRETCH);

        add(chatLayout);
        setSizeFull();
        setPadding(false);
    }

    private void setupChatForJob(Integer id, Person person, String topicId) {
        String foto = person.getFoto()!=null ? person.getFoto() : "images/blank-profile-picture.png";
        UserInfo currentUserInfo = new UserInfo(person.getId_Person().toString(), person.getVorname() + " " + person.getNachname(), foto);
        Component header = createChatHeader(jobService.getJobById(id));
        // Erstellen der Collaboration Engine-Komponenten
        CollaborationMessageList messageList = new CollaborationMessageList(currentUserInfo, topicId,databaseMessagePersister);

        CollaborationMessageInput messageInput = new CollaborationMessageInput(messageList);
        // Layout für den Nachrichtenbereich
        VerticalLayout messageLayout = new VerticalLayout(messageList, messageInput);
        messageLayout.setSizeFull();
        messageLayout.setFlexGrow(0, messageList);
        messageLayout.setFlexGrow(0, messageInput);
        messageLayout.setAlignItems(Alignment.STRETCH);

        // Gesamtes Chat-Layout
        VerticalLayout chatLayout = new VerticalLayout(header, messageLayout);
        chatLayout.setFlexGrow(0, header);
        chatLayout.setFlexGrow(1, messageLayout);
        chatLayout.setPadding(false);
        chatLayout.setSizeFull();
        chatLayout.setAlignItems(Alignment.STRETCH);
        chatLayout.setJustifyContentMode(JustifyContentMode.END);

        add(chatLayout);
        setSizeFull();
        setPadding(false);
    }

    private Component createChatHeader(Job job) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.STRETCH);
        card.setWidth("100%");


        HorizontalLayout frame = new HorizontalLayout();
        VerticalLayout foto = new VerticalLayout();
        foto.addClassName("foto");
        Person person = job.getPerson();
        boolean hasProfileImage = person.getFoto() != null;
        String profileImageSource = hasProfileImage ? person.getFoto() : "images/blank-profile-picture.png";
        Image profileImage = new Image(profileImageSource, "Profile Image");
        profileImage.addClassNames("profileImage");
        foto.add(profileImage);

        VerticalLayout chatDetails = new VerticalLayout();
        chatDetails.setSpacing(false);
        chatDetails.setAlignItems(Alignment.START);
        chatDetails.addClassName("student-details2");
        RouterLink name = new RouterLink("", UnternehmensPersonProfilView.class, person.getId_Person());
        name.getStyle().set("font-weight", "bold");
        name.add(person.getVorname() + " " + person.getNachname());
        name.addClassName("name");

        HorizontalLayout companyNameLayout = new HorizontalLayout();
        companyNameLayout.setPadding(false);
        companyNameLayout.setMargin(false);
        Label company = new Label(job.getUnternehmen().getName());
        company.getStyle().set("color", "white");
        company.getStyle().set("font-size", "8px");
        companyNameLayout.addClassName("name2");
        companyNameLayout.add(company);

        HorizontalLayout jobtitelLayout = new HorizontalLayout();
        jobtitelLayout.setPadding(false);
        jobtitelLayout.setMargin(false);
        jobtitelLayout.addClassName("name2");
        Label jobtitel = new Label(job.getTitel());
        jobtitel.getStyle().set("color", "white");
        jobtitel.getStyle().set("font-size", "8px");
        jobtitelLayout.add(jobtitel);

        chatDetails.add(name, company, jobtitelLayout);
        VerticalLayout zurueck = new VerticalLayout();
        Icon chevronLeft = new Icon(VaadinIcon.CHEVRON_LEFT);
        zurueck.addClassName("zurueck");
        chevronLeft.getStyle().set("cursor", "pointer");
        //chevronLeft.setSize("1em");
        chevronLeft.addClickListener(event -> {
            // Logik um zur Chatübersicht zu navigieren
            UI.getCurrent().navigate(ChatsView.class); // Den tatsächlichen Pfad zur Chatübersicht einsetzen
        });
        zurueck.add(chevronLeft);

        VerticalLayout dotsLayout = new VerticalLayout();
        // Drei-Punkte-Icon für das Dropdown-Menü
        Icon dots = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        dots.getStyle().set("cursor", "pointer");
        dots.setSize("1em");
        dotsLayout.addClassName("dotsLayout");


        // Dropdown-Menü erstellen
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(dots);
        contextMenu.setOpenOnClick(true);
        MenuItem melden = contextMenu.addItem("Melden", e -> {

            // Logik, die ausgeführt wird, wenn auf "Melden" geklickt wird
            Notification.show("Gemeldet", 3000, Notification.Position.MIDDLE);
        });
        melden.getElement().getStyle().set("color", "red");
        dotsLayout.add(dots);
        frame.add(zurueck,profileImage, chatDetails, dotsLayout);
        frame.addClassName("frame");
        frame.setSpacing(false);



        card.add(frame);
        card.setWidthFull();
        return card;
    }

}
