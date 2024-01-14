package de.hbrs.easyjob.services;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;

import com.vaadin.collaborationengine.TopicConnection;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.Chat;
import de.hbrs.easyjob.entities.Nachricht;
import de.hbrs.easyjob.repositories.NachrichtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@SpringComponent
public class DatabaseMessagePersister implements CollaborationMessagePersister  {


    @Autowired
    private ChatService chatService;

    private final transient SessionController sessionController;

    public DatabaseMessagePersister(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
        Chat chat = chatService.createOrGetChat(query.getTopicId());

        List<Nachricht> nachrichten = chatService.getNachrichten(chat);
        if(!nachrichten.isEmpty()) {
            nachrichten.sort(Comparator.comparing(Nachricht::getZeitpunkt).reversed());
            Nachricht lastNachricht = nachrichten.get(0);
            if(lastNachricht.getAbsender() != null && !Objects.equals(lastNachricht.getAbsender().getId_Person(), sessionController.getPerson().getId_Person())&&
                    !lastNachricht.isGelesen()){
                lastNachricht.setGelesen(true);
                chatService.updateNachricht(lastNachricht);
            }
        }
        return chatService.getNachrichten(chat).stream()
                .filter(nachricht -> nachricht.getZeitpunkt().equals(query.getSince()) || nachricht.getZeitpunkt().isAfter(query.getSince()))
                .filter(nachricht -> nachricht.getAbsender() != null)
                .map(nachricht -> new CollaborationMessage(
                        new UserInfo(nachricht.getAbsender().getId_Person().toString(), nachricht.getAbsender().getVorname()+" "+nachricht.getAbsender().getNachname(),
                                nachricht.getAbsender().getFoto()!= null ? nachricht.getAbsender().getFoto() : "images/blank-profile-picture.png"),
                        nachricht.getTextfeld(),
                        nachricht.getZeitpunkt()
                        )
                );
    }

    @Override
    public void persistMessage(PersistRequest request) {
        Chat chat = chatService.createOrGetChat(request.getTopicId());
        CollaborationMessage message = request.getMessage();

        Nachricht nachricht = new Nachricht();
        nachricht.setChat(chat);
        nachricht.setAbsender(sessionController.getPerson());
        nachricht.setTextfeld(message.getText());
        nachricht.setZeitpunkt(message.getTime());
        nachricht.setTopicId(request.getTopicId());
        nachricht.setGelesen(false); // Nachricht wird als ungelesen markiert

        chatService.saveNachricht(nachricht);
    }
}