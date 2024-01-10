package de.hbrs.easyjob.views.student;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.views.components.StudentLayout;
import com.vaadin.flow.component.html.*;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "student/nachrichten", layout = StudentLayout.class)
@StyleSheet("styles/MitarbeiterFindenView.css")
@RolesAllowed("ROLE_STUDENT")
public class ChatsView extends VerticalLayout {
    private final ChatService chatService;
    private final SessionController sessionController;
    private VerticalLayout chatListLayout;
    @Autowired
    public ChatsView(ChatService chatService, SessionController sessionController) {
        this.chatService = chatService;
        this.sessionController = sessionController;
        initLayout();
    }

    private void initLayout() {
        removeAll();
        List<Chat> chatList = chatService.getChatsByStudent(sessionController.getPerson());

        if (chatList.isEmpty()) {
            add(createNoChatsLayout());
        } else {
            chatList.forEach(chat -> add(createChatComponent(chat)));
        }
    }

    private Component createNoChatsLayout() {
        Image backgroundImage = new Image("images/filter.png", "Keine Chats");
        backgroundImage.setSizeFull();
        Div backgroundDiv = new Div(backgroundImage);
        backgroundDiv.addClassName("no-chats-background"); // CSS-Klasse für Styling

        return backgroundDiv;
    }

    private Component createChatComponent(Chat chat) {
        Job job = chat.getJob();
        Person person = job.getPerson();
        boolean hasProfileImage = person.getFoto() != null;
        String profileImageSource = hasProfileImage ? person.getFoto() : "images/blank-profile-picture.png";
        VerticalLayout card = new VerticalLayout();
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.STRETCH);
        card.setWidth("100%");

        HorizontalLayout frame = new HorizontalLayout();


        VerticalLayout foto = new VerticalLayout();
        foto.setWidth("42px");
        Image profilePic = new Image(profileImageSource, "Profilbild");
        profilePic.addClassName("ellipse-profile-picture");
        foto.add(profilePic);

        VerticalLayout studienDetails = new VerticalLayout();
        studienDetails.setSpacing(false);
        studienDetails.setAlignItems(Alignment.START);
        studienDetails.addClassName("student-details");


        RouterLink Jobtitel = new RouterLink("", ChatView.class, job.getId_Job() + "/" + chat.getStudent().getId_Person());
        Jobtitel.addClassName("name-label");
        Jobtitel.add(job.getTitel());

        HorizontalLayout nameLayout = new HorizontalLayout();
        Label personInfoLabel = new Label(person.getVorname() + " " + person.getNachname());
        personInfoLabel.addClassName("detail-label");
        nameLayout.add(personInfoLabel);

        HorizontalLayout nachrichtTextLayout = new HorizontalLayout();
        List<Nachricht> nachrichten = chatService.getNachrichten(chat);
        String nachrichtText = ""; // Default text

        if (nachrichten != null && !nachrichten.isEmpty()) {
            Nachricht lastNachricht = nachrichten.get(nachrichten.size() - 1);
            nachrichtText = lastNachricht != null ? lastNachricht.getTextfeld() : ""; // Default if text is null
        }
        Label letzeNachrichtLabel = new Label(limitText(nachrichtText, 30));
        letzeNachrichtLabel.addClassName("detail-label");
        nachrichtTextLayout.add(letzeNachrichtLabel);

        studienDetails.add(Jobtitel,nameLayout,nachrichtTextLayout);
        frame.add(foto,studienDetails);

        card.add(frame);
        return card;
    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}
