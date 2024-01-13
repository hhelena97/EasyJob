package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {

   Job findByTitel(String titel);

    // Volltextsuche
    @Query(value = "SELECT * FROM easy_job.job j\n" +
            "JOIN easy_job.unternehmen u on u.id_unternehmen = j.fk_unternehmen\n" +
            "WHERE to_tsvector('german',\n" +
            "                  coalesce(j.titel,'') || ' ' ||\n" +
            "                  coalesce((SELECT name FROM easy_job.unternehmen WHERE id_Unternehmen = j.FK_Unternehmen),'') || ' ' ||\n" +
            "                  coalesce((SELECT ort FROM easy_job.ort WHERE id_Ort = j.FK_Ort),'') || '' || \n"+
            "                  coalesce(j.freitext,'') || ' ' ||\n" +
            "                  coalesce((SELECT string_agg(name, ' ') FROM easy_job.branche INNER JOIN public.unternehmen_haben_branchen ON branche.ID_Branche = unternehmen_haben_branchen.ID_Branche WHERE unternehmen_haben_branchen.id_Unternehmen = j.FK_Unternehmen),'') || ' ' ||\n" +
            "                  coalesce((SELECT string_agg(fach || ' ' || abschluss, ' ') FROM easy_job.studienfach INNER JOIN public.job_sucht_studienfach ON studienfach.id_Studienfach = job_sucht_studienfach.id_Studienfach WHERE job_sucht_studienfach.id_Job = j.id_Job),'')) @@ to_tsquery(?1)",
            nativeQuery = true)
    List<Job> vollTextSuche(String searchText);



    // Teilzeichenkettensuche
    @Query("SELECT j FROM Job j JOIN j.unternehmen u WHERE " +
            "LOWER(j.titel) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(j.freitext) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Job> teilZeichenSuche(String searchText);


    @Query("SELECT j FROM Job j")
    List<Job> findAllJobs();

    @Query("SELECT COUNT(j) FROM Job j WHERE j.unternehmen.id_Unternehmen = :unternehmenId")
    int countByUnternehmenId_Unternehmen(@Param("unternehmenId")Integer unternehmenId);

    @Query("SELECT j FROM Job j WHERE j.unternehmen.id_Unternehmen = :unternehmenID")
    Set<Job> findAllByUnternehmen(Integer unternehmenID);
    @Query("SELECT j FROM Job j WHERE j.unternehmen.id_Unternehmen = ?1")
    List<Job> findAllByUnternehmenId(int unternehmenId);

    @Query("SELECT j FROM Job j WHERE j.person.id_Person = :uPersonID")
    List<Job> findAllJobs(int uPersonID);
}
