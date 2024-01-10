package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Chat;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Nachricht;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.ChatService;
import de.hbrs.easyjob.views.allgemein.LoginView;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import de.hbrs.easyjob.views.student.ChatView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "unternehmen/nachrichten", layout = UnternehmenLayout.class)
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class ChatsView extends VerticalLayout {
    private final ChatService chatService;
    private final SessionController sessionController;
    @Autowired
    public ChatsView(ChatService chatService, SessionController sessionController) {
        this.chatService = chatService;
        this.sessionController = sessionController;
        initLayout();
    }

    private void initLayout() {
        removeAll();
        List<Chat> chatList = chatService.getChatsByUnternehmensperson(sessionController.getPerson());

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
        backgroundDiv.addClassName("no-chats-background");

        return backgroundDiv;
    }

    private Component createChatComponent(Chat chat) {
        Job job = chat.getJob();
        Person student = chat.getStudent();
        String profileImageSource = student.getFoto() != null ? student.getFoto() : "images/blank-profile-picture.png";
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


        RouterLink Jobtitel = new RouterLink("", ChatView.class, job.getId_Job() + "/" + job.getPerson().getId_Person());
        Jobtitel.addClassName("name-label");
        Jobtitel.add(job.getTitel());

        HorizontalLayout studyFieldLayout = new HorizontalLayout();
        Label personInfoLabel = new Label(student.getVorname() + " " + student.getNachname());
        personInfoLabel.addClassName("detail-label");
        studyFieldLayout.add(personInfoLabel);

        HorizontalLayout locationLayout = new HorizontalLayout();
        List<Nachricht> nachrichten = chatService.getNachrichten(chat);
        String nachrichtText = ""; // Default text

        if (nachrichten != null && !nachrichten.isEmpty()) {
            Nachricht lastNachricht = nachrichten.get(nachrichten.size() - 1);
            nachrichtText = lastNachricht != null ? lastNachricht.getTextfeld() : ""; // Default if text is null
        }
        Label letzeNachrichtLabel = new Label(limitText(nachrichtText, 30));
        letzeNachrichtLabel.addClassName("detail-label");
        locationLayout.add(letzeNachrichtLabel);

        studienDetails.add(Jobtitel,studyFieldLayout,locationLayout);
        frame.add(foto,studienDetails);

        card.add(frame);
        return card;
    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }


}
