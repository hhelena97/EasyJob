package de.hbrs.easyjob.services;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;

import com.vaadin.collaborationengine.TopicConnection;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.hbrs.easyjob.entities.Chat;
import de.hbrs.easyjob.entities.Nachricht;
import de.hbrs.easyjob.repositories.NachrichtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@SpringComponent
public class DatabaseMessagePersister implements CollaborationMessagePersister  {


    @Autowired
    private ChatService chatService;

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
        Chat chat = chatService.createOrGetChat(query.getTopicId()); // Annahme, dass diese Methode den Chat anhand der Topic-ID findet oder erstellt

        return chatService.getNachrichten(chat).stream()
                .filter(nachricht -> !nachricht.getZeitpunkt().isBefore(query.getSince()))
                .filter(nachricht -> nachricht.getAbsender() != null)
                .map(nachricht -> new CollaborationMessage(
                        new UserInfo(nachricht.getAbsender().getId_Person().toString(), nachricht.getAbsender().getVorname()+" "+nachricht.getAbsender().getNachname()),
                        nachricht.getTextfeld(),
                        nachricht.getZeitpunkt())
                );
    }

    @Override
    public void persistMessage(PersistRequest request) {
        Chat chat = chatService.createOrGetChat(request.getTopicId());
        CollaborationMessage message = request.getMessage();

        Nachricht nachricht = new Nachricht();
        nachricht.setChat(chat);
        nachricht.setAbsender(chatService.getPersonById(Integer.parseInt(message.getUser().getId())));
        nachricht.setTextfeld(message.getText());
        nachricht.setZeitpunkt(message.getTime());
        nachricht.setGelesen(false); // Nachricht wird als ungelesen markiert
        nachricht.setTopicId(request.getTopicId());

        chatService.saveNachricht(nachricht);
    }
}