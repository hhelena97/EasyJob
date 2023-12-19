package de.hbrs.easyjob.repositories;

import de.hbrs.easyjob.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentRepository extends JpaRepository<Student, Integer> ,JpaSpecificationExecutor<Student>{
    @Query(value = "SELECT p.* FROM easy_job.person p " +
            "JOIN easy_job.studienfach ON p.fk_studienfach = studienfach.id_studienfach " +
            "WHERE to_tsvector('german', " +
            "coalesce(p.nachname,'') || ' ' || " +
            "coalesce(p.vorname,'') || ' ' || " +
            "coalesce(studienfach.fach,'') || ' ' || " +
            "coalesce(studienfach.abschluss,'') || ' ' || " +
            "coalesce((SELECT string_agg(ort.ort, ' ') FROM easy_job.ort " +
            "JOIN public.student_ort ON ort.id_ort = student_ort.id_ort " +
            "WHERE student_ort.id_student = p.id_pesron), '') || ' ' || " +
            "coalesce((SELECT string_agg(job_kategorie.kategorie_name, ' ') FROM easy_job.job_kategorie " +
            "JOIN public.student_job_kategorie ON job_kategorie.id_job_kategorie = student_job_kategorie.id_job_kategorie " +
            "WHERE student_job_kategorie.id_student = p.id_pesron), '') " +
            ") @@ plainto_tsquery(?1)",
            nativeQuery = true)
    List<Student> vollTextSuche(String volltext);

    @Query(value = "SELECT DISTINCT p.* FROM easy_job.person p " +
            "JOIN easy_job.studienfach sf ON p.fk_studienfach = sf.id_studienfach " +
            "LEFT JOIN public.student_ort so ON p.id_pesron = so.id_student " +
            "LEFT JOIN easy_job.ort o ON so.id_ort = o.id_ort " +
            "LEFT JOIN public.student_job_kategorie sjk ON p.id_pesron = sjk.id_student " +
            "LEFT JOIN easy_job.job_kategorie jk ON sjk.id_job_kategorie = jk.id_job_kategorie " +
            "WHERE LOWER(sf.fach) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(sf.abschluss) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(o.ort) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(jk.kategorie_name) LIKE LOWER(CONCAT('%', ?1, '%'))",
            nativeQuery = true)
    List<Student> teilZeichenSuche(String teilZeichen);

    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    Student findByEmail(String mail);
}