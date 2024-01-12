package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query("SELECT c FROM Chat c WHERE c.job.id_Job = :jobId AND c.student.id_Person = :studentId")
    Chat findByJobIdAndStudentId(@Param("jobId") Integer jobId, @Param("studentId") Integer studentId);
    @Query("SELECT c FROM Chat c WHERE c.student.id_Person = :studentId")
    List<Chat> findAllByStudentId(Integer studentId);

    @Query("SELECT c FROM Chat c WHERE c.unternehmensperson.id_Person = :unternehmenspersonId")
    List <Chat> findAllByUnternehmenspersonId(Integer unternehmenspersonId);
}