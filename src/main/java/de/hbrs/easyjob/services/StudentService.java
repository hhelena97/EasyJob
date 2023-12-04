package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.JobKategorieRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import de.hbrs.easyjob.repositories.StudentRepository;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(JobKategorieService.class);


    private final StudentRepository studentRepository;
    private final StudienfachRepository studienfachRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final OrtRepository ortRepository;

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
                 //   .orElse(ortRepository.save(new Ort(ort.getPLZ(), ort.getOrt())));
            if (gefundenerOrt == null ){
               gefundenerOrt = ortRepository.save(new Ort(ort.getPLZ(),ort.getOrt()));
            }

            orte.add(gefundenerOrt);
        }
        student.setOrte(orte);
        return studentRepository.save(student);
    }

}
