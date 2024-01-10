package de.hbrs.easyjob.views.student;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.services.DatabaseMessagePersister;
import de.hbrs.easyjob.services.JobService;
import de.hbrs.easyjob.views.components.StudentLayout;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.security.RolesAllowed;


@Route(value = "chat", layout = StudentLayout.class)
@RolesAllowed("ROLE_STUDENT")
public class ChatView extends VerticalLayout implements HasUrlParameter<String> {

    private final DatabaseMessagePersister databaseMessagePersister;

    private Integer jobId;
    private Integer studentId;
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
                jobId = Integer.parseInt(params[0]);
                studentId = Integer.parseInt(params[1]);
                setupChat(jobId, studentId);
            } catch (NumberFormatException e) {
                // Wenn jobId oder studentId keine Zahl ist, geben Sie eine Fehlermeldung aus
                add(new H1("Fehler"));

            }
        }

    }
    private void setupChat(Integer jobId, Integer studentId) {

        Person person = sessionController.getPerson();
        UserInfo currentUserInfo = new UserInfo(person.getId_Person() + "", person.getVorname() + " " + person.getNachname());

        String topicId = "chat-" + jobId + "-" + studentId;
        String foto = person.getFoto();

        Div header = createChatHeader(jobService.getJobById(jobId));



        // Erstellen der Collaboration Engine-Komponenten
        CollaborationMessageList messageList = new CollaborationMessageList(currentUserInfo, topicId,databaseMessagePersister);
        messageList.setMessageConfigurator((message, user) -> {
            if (foto != null) {
                message.setUserImage(foto);
            }
        });
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

    private Div createChatHeader(Job job) {
        Person person = job.getPerson();
        boolean hasProfileImage = person.getFoto() != null;
        String profileImageSource = hasProfileImage ? person.getFoto() : "images/blank-profile-picture.png";
        Image profileImage = new Image(profileImageSource, "Profile Image");
        profileImage.setHeight("50px");

        Span name = new Span(person.getVorname() + " " + person.getNachname());
        name.getStyle().set("font-weight", "bold");
        Span company = new Span(job.getUnternehmen().getName());
        company.getStyle().set("color", "grey");

        Span description = new Span(job.getTitel());
        description.getStyle().set("color", "grey");
        Icon chevronLeft = new Icon(VaadinIcon.CHEVRON_LEFT);
        chevronLeft.getStyle().set("cursor", "pointer");
        chevronLeft.setSize("1em");
        chevronLeft.addClickListener(event -> {
            // Logik um zur Chatübersicht zu navigieren
            UI.getCurrent().navigate(ChatsView.class); // Den tatsächlichen Pfad zur Chatübersicht einsetzen
        });

        // Drei-Punkte-Icon für das Dropdown-Menü
        Icon dots = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        dots.getStyle().set("cursor", "pointer");
        dots.setSize("1em");

        // Dropdown-Menü erstellen
        ContextMenu contextMenu = new ContextMenu(dots);
        contextMenu.addItem("Melden", e -> {
            // Logik, die ausgeführt wird, wenn auf "Melden" geklickt wird
            Notification.show("Gemeldet", 3000, Notification.Position.MIDDLE);
        });


        HorizontalLayout headerLayout = new HorizontalLayout(chevronLeft, profileImage, name,company, description, dots);
        headerLayout.expand(description);
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setWidthFull();
        headerLayout.getStyle().set("padding", "10px");
        headerLayout.getStyle().set("background-color", "#A3336F");
        headerLayout.getStyle().set("color", "white");


        // ContextMenu an das Icon anhängen (aktiviert das Dropdown-Menü beim Klick auf das Icon)
        contextMenu.setTarget(dots);

        // Div als Wrapper für das Layout zurückgeben
        Div header = new Div(headerLayout);
        header.setWidthFull();
        return header;
    }

}
