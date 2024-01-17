package de.hbrs.easyjob.services;

import de.hbrs.easyjob.controllers.SessionController;
import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.ChatRepository;
import de.hbrs.easyjob.repositories.NachrichtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

/**
 * ChatService bietet verschiedene Methoden für den Umgang mit Chats, Nachrichten und Studenten.
 * Es enthält Methoden zum Erstellen und Abrufen von Chats, zum Abrufen von Nachrichten und Chats eines Studenten,
 * zum Speichern und Aktualisieren von Nachrichten.
 */
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private NachrichtRepository nachrichtRepository;
    @Autowired
    private JobService jobService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PersonService personService;

    private final transient SessionController sessionController;

    public ChatService(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    /**
     * Erstellt oder holt einen Chat basierend auf einer bestimmten Themen-ID.
     *
     * @param topicId Die ID des Themas.
     * @return Der neu erstellte oder vorhandene Chat.
     */
    public Chat createOrGetChat(String topicId) {
        // Logik zur Extraktion von jobId und studentId aus topicId
        String[] parts = topicId.split("-");
        String topic = parts[0];
        if (topic.equals("Job")) {
            Integer jobId = Integer.parseInt(parts[1]);
            Integer personId = Integer.parseInt(parts[2]);
            return createOrGetChatForJob(jobId, personId, topicId);
        } else {
            return null;
        }
    }


    /**
     * Erstellt oder holt einen Chat basierend auf einer bestimmten Job-ID und Studenten-ID.
     *
     * @param jobId     Die ID des Jobs.
     * @param studentId Die ID des Studenten.
     * @param topicId   Die ID des Themas.
     * @return Der neu erstellte oder vorhandene Chat.
     */

    public Chat createOrGetChatForJob(Integer jobId, Integer studentId, String topicId) {
        Student student = studentService.getStudentByID(studentId);
        if (student == null) {
            System.out.println("Student is null");
            return null;
        }

        Chat existingChat = chatRepository.findByJobIdAndStudentId(jobId, studentId);
        if (existingChat != null) {
            return existingChat;
        } else {
            Chat newChat = new Chat();
            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("No job found with the id " + jobId);
                return null;
            }
            Unternehmensperson unternehmensperson = (Unternehmensperson) job.getPerson();
            newChat.setJob(job);
            newChat.setStudent(student);
            newChat.setUnternehmensperson(unternehmensperson);
            newChat.setAktiv(true);
            newChat.setTopicId(topicId);
            chatRepository.save(newChat);
            return newChat;
        }
    }

    /**
     * Holt die Nachrichten für einen bestimmten Chat.
     *
     * @param chat Der Chat, für den die Nachrichten abgerufen werden sollen.
     * @return Eine Liste von Nachrichten für den angegebenen Chat.
     */
    public List<Nachricht> getNachrichten(Chat chat) {
        return nachrichtRepository.findByChat(chat);
    }

    /**
     * Holt die Chats eines bestimmten Studenten.
     *
     * @param person Der Student, für den die Chats abgerufen werden sollen.
     * @return Eine Liste von Chats für den angegebenen Studenten.
     */
    public List<Chat> getChatsByStudent(Person person) {
        return chatRepository.findAllByStudentId(person.getId_Person());
    }

    /**
     * Holt die Chats einer bestimmten Unternehmensperson.
     *
     * @param person Die Unternehmensperson, für die die Chats abgerufen werden sollen.
     * @return Eine Liste von Chats für die angegebene Unternehmensperson.
     */
    public List<Chat> getChatsByUnternehmensperson(Person person) {
        return chatRepository.findAllByUnternehmenspersonId(person.getId_Person());
    }

    /**
     * Speichert eine bestimmte Nachricht.
     *
     * @param nachricht Die zu speichernde Nachricht.
     */
    public void saveNachricht(Nachricht nachricht) {
        nachrichtRepository.save(nachricht);
    }

    /**
     * Aktualisiert eine bestimmte Nachricht.
     *
     * @param nachricht Die zu aktualisierende Nachricht.
     */
    public void updateNachricht(Nachricht nachricht) {
        nachrichtRepository.save(nachricht);
    }
}
