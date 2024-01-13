package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Route(value = "unternehmen/nachrichten", layout = UnternehmenLayout.class)
@StyleSheet("styles/MitarbeiterFindenView.css")
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


        //Person und Image
        Job job = chat.getJob();
        Person student = chat.getStudent();
        boolean hasProfileImage = student.getFoto() != null;
        String profileImageSource = hasProfileImage ? student.getFoto() : "images/blank-profile-picture.png";
        //letzte Nachricht
        List<Nachricht> nachrichten = chatService.getNachrichten(chat);
        String nachrichtText = ""; // Default text
        Nachricht lastNachricht = null;
        if (nachrichten != null && !nachrichten.isEmpty()) {
            nachrichten.sort(Comparator.comparing(Nachricht::getZeitpunkt).reversed());
            lastNachricht = nachrichten.get(0);
            nachrichtText = lastNachricht != null ? lastNachricht.getTextfeld() : ""; // Default if text is null
        }

        //Zeit und Zeitformat
        if(lastNachricht == null) {
            return new VerticalLayout();
        }
        Instant zeitpunkt = lastNachricht.getZeitpunkt();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(zeitpunkt, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = localDateTime.format(formatter);
        //gelesen
        boolean isLastNachrichtVonUnternehmensperson = Objects.equals(lastNachricht.getAbsender().getId_Person(), sessionController.getPerson().getId_Person());
        boolean isLastNachrichtGelesen = isLastNachrichtVonUnternehmensperson || lastNachricht.isGelesen();


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


        RouterLink Jobtitel = new RouterLink("", chatViewUnternehmensperson.class, chat.getTopicId());
        Jobtitel.addClassName("name-label");
        Jobtitel.add(job.getTitel());

        HorizontalLayout nameLayout = new HorizontalLayout();
        Label personInfo = new Label(student.getVorname() + " " + student.getNachname());
        personInfo.addClassName("detail-label");
        Label timeLabel = new Label(time);
        timeLabel.getStyle().set("font-size","12px");
        nameLayout.add(personInfo,timeLabel);
        nameLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);


        HorizontalLayout locationLayout = new HorizontalLayout();

        Label letzeNachrichtLabel = new Label(limitText(nachrichtText, 30));
        letzeNachrichtLabel.addClassName("detail-label");
        locationLayout.add(letzeNachrichtLabel);

        studienDetails.add(Jobtitel,nameLayout,locationLayout);
        studienDetails.getStyle().set("margin","8px 0px 0px 32px");
        frame.add(foto,studienDetails);

        card.add(frame);
        if(!isLastNachrichtGelesen) {
            card.getStyle().set("background-color", "rgba(254, 137, 151, 0.25)");
        }
        return card;
    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }


}
