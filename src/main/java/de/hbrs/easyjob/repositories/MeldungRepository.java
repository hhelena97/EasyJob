package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public interface MeldungRepository extends JpaRepository<Meldung, Integer> {


    //eigentlich braucht es hier findAll deren bearbeitet noch auf false steht
    //findAll deren Person nicht null ist
    //findAll deren Unternehmen nicht null ist
    //findAll deren Job nicht null ist



    //public Meldung findById(Integer id);
    public Meldung findAllByPerson(Person person);

    public Meldung findAllByUnternehmen(Unternehmen unternehmen);

    public Meldung findAllByJob(Job job);

    //public Meldung findAllByChat(Chat chat)



    @Query("SELECT m FROM Meldung m")
    List<Meldung> findAllMeldungen();

    @Query("SELECT COUNT(m) FROM Meldung m WHERE m.person.id_Person = :personId")
    int countByPersonId_Person(@Param("personId")Integer personId);

    @Query("SELECT COUNT(m) FROM Meldung m WHERE m.unternehmen.id_Unternehmen = :unternehmenId")
    int countByUnternehmenId_Unternehmen(@Param("unternehmenId")Integer unternehmenId);

    @Query("SELECT COUNT(m) FROM Meldung m WHERE m.job.id_Job = :jobId")
    int countByJobId_Job(@Param("jobId")Integer jobId);

    /*
    @Query("SELECT COUNT(m) FROM Meldung m WHERE m.chat.id_Chat = :chatId")
    int countByChatId_Chat(@Param("chatId")Integer chatId);
     */


}
