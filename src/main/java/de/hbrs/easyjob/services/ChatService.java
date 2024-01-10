package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.ChatRepository;
import de.hbrs.easyjob.repositories.NachrichtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

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

    public Chat createOrGetChat(String topicId) {
        // Logik zur Extraktion von jobId und studentId aus topicId
        String[] parts = topicId.split("-");
        Integer jobId = Integer.parseInt(parts[1]);
        Integer studentId = Integer.parseInt(parts[2]);

        return createOrGetChat(jobId, studentId);
    }

    public Chat createOrGetChat(Integer jobId, Integer studentId) {
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
            chatRepository.save(newChat);
            return newChat;
        }
    }

    public List<Nachricht> getNachrichten(Chat chat) {
        return nachrichtRepository.findByChat(chat);
    }

    public Student getPersonById(Integer id) {
        return studentService.getStudentByID(id);
    }

    public List<Chat> getChatsByStudent(Person person) {
        return chatRepository.findAllByStudentId(person.getId_Person());
    }
    public List<Chat> getChatsByUnternehmensperson(Person person) {
        return chatRepository.findAllByUnternehmenspersonId(person.getId_Person());
    }
    public void saveNachricht(Nachricht nachricht) {
        nachrichtRepository.save(nachricht);
    }
}
