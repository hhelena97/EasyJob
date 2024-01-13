package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
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
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.services.DatabaseMessagePersister;
import de.hbrs.easyjob.services.JobService;
import de.hbrs.easyjob.services.StudentService;
import de.hbrs.easyjob.views.components.StudentLayout;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import de.hbrs.easyjob.views.student.ChatsView;
import de.hbrs.easyjob.views.student.UnternehmensPersonProfilView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
@Route(value = "chat-unternehmensperson", layout = UnternehmenLayout.class)
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class chatViewUnternehmensperson extends VerticalLayout implements HasUrlParameter<String> {

    private final DatabaseMessagePersister databaseMessagePersister;

    private final StudentService studentService;
    private final SessionController sessionController;
    private final JobService jobService;

    @Autowired
    public chatViewUnternehmensperson(DatabaseMessagePersister databaseMessagePersister, StudentService studentService, SessionController sessionController, JobService jobService) {
        this.databaseMessagePersister = databaseMessagePersister;
        this.studentService = studentService;
        this.sessionController = sessionController;
        this.jobService = jobService;
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String topicId) {
        if (topicId != null) {
            setupChat(topicId);
        } else {
            add(new H1("Job-ID fehlt in der URL"));
        }
    }
    private void setupChat(String topicId) {
        // Logik zur Extraktion von jobId und studentId aus topicId
        String[] parts = topicId.split("-");
        String topic = parts[0];
        Integer Id = Integer.parseInt(parts[1]);




        if (topic.equals("Job")) {
            Job job = jobService.getJobById(Id);
            createOrGetChatForJob(job, topicId);
        } else if (topic.equals("Student")) {
            Student student = studentService.getStudentByID(Id);
            createOrGetChatForStudent(student, topicId);
        } else {
            System.out.println("Topic is null");
            return;
        }


    }

    private void createOrGetChatForStudent(Student student, String topic) {
        Component header = createChatHeader(jobService.getJobById(1));
        Person person = sessionController.getPerson();
        UserInfo currentUserInfo = new UserInfo(person.getId_Person() + "", person.getVorname() + " " + person.getNachname());

        VerticalLayout messageLayout = getVerticalLayout(topic, person, currentUserInfo);

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

    private void createOrGetChatForJob(Job job, String topic) {
        Person person = sessionController.getPerson();
        String foto = person.getFoto()!=null ? person.getFoto() : "images/blank-profile-picture.png";
        Component header = createChatHeader(job);

        UserInfo currentUserInfo = new UserInfo(person.getId_Person() + "", person.getVorname() + " " + person.getNachname(), foto);

        VerticalLayout messageLayout = getVerticalLayout(topic, person, currentUserInfo);

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

    private VerticalLayout getVerticalLayout(String topic, Person person, UserInfo currentUserInfo) {
        String topicId = topic +"-"+ person.getId_Person();
        // Erstellen der Collaboration Engine-Komponenten
        CollaborationMessageList messageList = new CollaborationMessageList(currentUserInfo, topicId,databaseMessagePersister);

        CollaborationMessageInput messageInput = new CollaborationMessageInput(messageList);
        // Layout für den Nachrichtenbereich
        VerticalLayout messageLayout = new VerticalLayout(messageList, messageInput);
        messageLayout.setSizeFull();
        messageLayout.setFlexGrow(0, messageList);
        messageLayout.setFlexGrow(0, messageInput);
        messageLayout.setAlignItems(Alignment.STRETCH);
        return messageLayout;
    }

    private Component createChatHeader(Job job) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.STRETCH);
        card.setWidth("100%");


        HorizontalLayout frame = new HorizontalLayout();
        VerticalLayout foto = new VerticalLayout();
        Person person = job.getPerson();
        boolean hasProfileImage = person.getFoto() != null;
        String profileImageSource = hasProfileImage ? person.getFoto() : "images/blank-profile-picture.png";
        Image profileImage = new Image(profileImageSource, "Profile Image");
        profileImage.setHeight("50px");
        foto.add(profileImage);

        VerticalLayout chatDetails = new VerticalLayout();
        chatDetails.setSpacing(false);
        chatDetails.setAlignItems(Alignment.START);
        chatDetails.addClassName("student-details");
        RouterLink name = new RouterLink("", UnternehmensPersonProfilView.class, person.getId_Person());
        name.getStyle().set("font-weight", "bold");
        name.add(person.getVorname() + " " + person.getNachname());

        HorizontalLayout companyNameLayout = new HorizontalLayout();
        Label company = new Label(job.getUnternehmen().getName());
        company.getStyle().set("color", "grey");
        companyNameLayout.add(company);

        HorizontalLayout jobtitelLayout = new HorizontalLayout();
        Label jobtitel = new Label(job.getTitel());
        jobtitel.getStyle().set("color", "grey");
        jobtitelLayout.add(jobtitel);

        chatDetails.add(name, companyNameLayout, jobtitelLayout);
        VerticalLayout zurueck = new VerticalLayout();
        Icon chevronLeft = new Icon(VaadinIcon.CHEVRON_LEFT);
        chevronLeft.getStyle().set("cursor", "pointer");
        chevronLeft.setSize("1em");
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


        // Dropdown-Menü erstellen
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(dots);
        contextMenu.setOpenOnClick(true);
        contextMenu.addItem("Melden", e -> {
            // Logik, die ausgeführt wird, wenn auf "Melden" geklickt wird
            Notification.show("Gemeldet", 3000, Notification.Position.MIDDLE);
        });
        dotsLayout.add(dots);
        frame.add(zurueck,foto, chatDetails, dotsLayout);


        card.add(frame);
        card.setWidthFull();
        return card;
    }

}


