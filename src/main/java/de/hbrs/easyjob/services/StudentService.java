package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.StudentRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    // Repositories
    private final StudentRepository studentRepository;
    private final StudienfachRepository studienfachRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final OrtRepository ortRepository;

    // Entity
    @PersistenceContext
    private EntityManager entityManager;

    // Konstante
    private static final String ID_PERSON = "id_Person";

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          StudienfachRepository studienfachRepository,
                          JobKategorieRepository jobKategorieRepository,
                          OrtRepository ortRepository) {
        this.studentRepository = studentRepository;
        this.studienfachRepository = studienfachRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.ortRepository = ortRepository;
    }

    public Student saveStudent(Student student) {

        // Finden oder Erstellen des Studienfachs
        Studienfach studienfach = studienfachRepository
                .findByFachAndAbschluss(student.getStudienfach().getFach(), student.getStudienfach().getAbschluss());
        if (studienfach == null){
           studienfach = studienfachRepository.save(new Studienfach(student.getStudienfach().getFach(), student.getStudienfach().getAbschluss()));
        }
        student.setStudienfach(studienfach);

        // Finden oder Erstellen der JobKategorien
        Set<JobKategorie> jobKategorien = new HashSet<>();
        for (JobKategorie jobKategorie : student.getJobKategorien()) {
            JobKategorie gefundeneJobKategorie = jobKategorieRepository.findByKategorie(jobKategorie.getKategorie());

            if(gefundeneJobKategorie == null){
               gefundeneJobKategorie= jobKategorieRepository.save(new JobKategorie(jobKategorie.getKategorie()));
            }
            jobKategorien.add(gefundeneJobKategorie);

        }
        student.setJobKategorien(jobKategorien);

        // Finden oder Erstellen der bevorzugten Orte
        Set<Ort> orte = new HashSet<>();
        for (Ort ort : student.getOrte()) {
            Ort gefundenerOrt = ortRepository.findByPLZAndOrt(ort.getPLZ(), ort.getOrt());

            if (gefundenerOrt == null ){
               gefundenerOrt = ortRepository.save(new Ort(ort.getPLZ(),ort.getOrt()));
            }

            orte.add(gefundenerOrt);
        }
        student.setOrte(orte);
        return studentRepository.save(student);
    }

    public List<Student> getAllStudent(){
        return studentRepository.findAllStudents();
    }

    public Student getStudentByID(Integer id){
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }
    public List<Student> getStudentenByIds(List<Integer> ids){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> student = cq.from(Student.class);
        cq.select(student).where(student.get(ID_PERSON).in(ids));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Ort> getAllOrte(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ort> cq = cb.createQuery(Ort.class);
        Root<Student> studentRoot = cq.from(Student.class);
        // Join zwischen Student und Ort
        Join<Student, Ort> studentOrtJoin = studentRoot.join("orte", JoinType.INNER);
        Predicate studentIdPredicate = cb.equal(studentRoot.get(ID_PERSON), id);
        cq.select(studentOrtJoin).where(studentIdPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    public List<JobKategorie> getAllJobKategorien(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobKategorie> cq = cb.createQuery(JobKategorie.class);
        Root<Student> studentRoot = cq.from(Student.class);
        Join<Student, JobKategorie> studentJobKategorieJoin = studentRoot.join("jobKategorien", JoinType.INNER);
        Predicate studentIdPredicate = cb.equal(studentRoot.get(ID_PERSON), id);
        cq.select(studentJobKategorieJoin).where(studentIdPredicate);
        TypedQuery<JobKategorie> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
