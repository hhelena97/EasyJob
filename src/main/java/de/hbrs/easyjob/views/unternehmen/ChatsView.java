package de.hbrs.easyjob.views.unternehmen;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Chat;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Nachricht;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.services.ChatService;
import de.hbrs.easyjob.views.components.UnternehmenLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Route(value = "unternehmen/nachrichten", layout = UnternehmenLayout.class)
@StyleSheet("ChatList.css")
@RolesAllowed("ROLE_UNTERNEHMENSPERSON")
public class ChatsView extends VerticalLayout {
    private final ChatService chatService;
    private final SessionController sessionController;
    private final VerticalLayout chatListLayout;
    private int colorIndex;
    private int lineCount;
    @Autowired
    public ChatsView(ChatService chatService, SessionController sessionController) {
        this.chatService = chatService;
        this.sessionController = sessionController;
        chatListLayout = new VerticalLayout();
        chatListLayout.getStyle().set("gap", "5px");
        initLayout();
    }

    private void initLayout() {
        removeAll();
        List<Chat> chatList = chatService.getChatsByUnternehmensperson(sessionController.getPerson());
        Label header = new Label("Nachrichten");
        header.addClassName("header");
        add(header);

        if (chatList.isEmpty()) {
            chatListLayout.add(createNoChatsLayout());
            add(chatListLayout);
        } else {
            colorIndex = 0;
            lineCount = chatList.size() -1;
            chatList.forEach(chat -> {
                chatListLayout.add(createChatComponent(chat));
                if (lineCount > 0) {
                    Div line = new Div();
                    line.addClassName("line");
                    chatListLayout.add(line);
                    lineCount--;
                }
            });
            add(chatListLayout);
        }
    }

    private Component createNoChatsLayout() {
        Label empty = new Label("Sie haben noch keine Nachrichten.");
        empty.addClassName("empty");
        add(empty);
        Image backgroundImage = new Image("images/unternehmen-messages.png", "Keine Chats");
        backgroundImage.setSizeFull();
        HorizontalLayout backgroundDiv = new HorizontalLayout(backgroundImage);
        backgroundDiv.addClassName("grafik");

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

        HorizontalLayout frameHorizontal = new HorizontalLayout();


        //Avatar
        Avatar avatar = new Avatar();
        avatar.setImage(profileImageSource);
        avatar.setColorIndex(colorIndex++);

        VerticalLayout studienDetails = new VerticalLayout();
        studienDetails.setSpacing(false);
        studienDetails.setAlignItems(Alignment.START);
        studienDetails.addClassName("message-details");

        RouterLink Jobtitel = new RouterLink("", ChatViewUnternehmensperson.class, chat.getTopicId());
        Jobtitel.addClassName("title");
        Jobtitel.getStyle().set("width", "auto");
        Jobtitel.add(job.getTitel());

        HorizontalLayout nameLayout = new HorizontalLayout();
        Label personInfo = new Label(student.getVorname() + " " + student.getNachname());
        personInfo.addClassName("user-name");
        Label timeLabel = new Label(time);
        timeLabel.getStyle().set("font-size","14px");
        nameLayout.add(personInfo,timeLabel);
        nameLayout.setWidth("100%");
        nameLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);


        HorizontalLayout locationLayout = new HorizontalLayout();

        Label letzeNachrichtLabel = new Label(limitText(nachrichtText, 30));
        letzeNachrichtLabel.addClassName("details");
        locationLayout.add(letzeNachrichtLabel);

        studienDetails.add(Jobtitel,nameLayout,locationLayout);
        frameHorizontal.add(avatar,studienDetails);

        card.add(frameHorizontal);
        if(!isLastNachrichtGelesen) {
            card.getStyle().set("background-color", "var(--hintergrund-sky)");
            card.getStyle().set("border-radius","10px");
            card.getStyle().set("padding", "5px 8px");
        }
        return card;
    }

    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }


}
