package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repository.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

public class RegistrierungsControllerStudent extends RegistrierungsController {
    final
    StudentRepository studentRepository;
    private final StudienfachRepository studienfachRepository;
    private final JobKategorieRepository jobKategorieRepository;
    private final BrancheRepository brancheRepository;
    private final BerufsFeldRepository berufsFeldRepository;
    private final OrtRepository ortRepository;

    /**
     * Konstruktor, um das StudentenRepository befüllen zu können
     *
     * @author Janina
     * @param
     */
    public RegistrierungsControllerStudent(StudentRepository studentRepository, StudienfachRepository studienfachRepository, JobKategorieRepository jobKategorieRepository, BrancheRepository brancheRepository, BerufsFeldRepository berufsFeldRepository, OrtRepository ortRepository) {
        super();
        this.studentRepository = studentRepository;
        this.studienfachRepository = studienfachRepository;
        this.jobKategorieRepository = jobKategorieRepository;
        this.brancheRepository = brancheRepository;
        this.berufsFeldRepository = berufsFeldRepository;
        this.ortRepository = ortRepository;
    }


    public boolean createStudent( Student student, boolean AGB ){
        //prüfe Email und Passwort
        //TODO prüfe rest
        boolean ret =(isValidEmail(student.getEmail()))&&(isValidPassword(student.getPasswort())&&isAGBAccepted(AGB)&&isValidStudiengang(student)&&isValidVorname(student.getVorname())&&isValidNachname(student.getNachname())&&isValidTelefonnummer(student.getTelefon())&&isValidJobKategorie(student)&&isValidBranche(student)&&isValidBerufsFeld(student)&&isValidOrt(student));
        if(ret){
            Studienfach studienfach = studienfachRepository.findByFachAndAbschluss(student.getStudienfach().getFach(),student.getStudienfach().getAbschluss());
            student.setStudienfach(studienfach);
            studentRepository.save(student);
        }
        return ret;
    }

    public boolean isValidStudiengang(Student student){
        Studienfach studienfach = studienfachRepository.findByFachAndAbschluss(student.getStudienfach().getFach(),student.getStudienfach().getAbschluss());
        if(studienfach== null){return false;}
        return true;
    }
    //TODO ersetzen string to id
    public boolean isValidJobKategorie(Student student){
        Set<JobKategorie> jobKategorieSet = new HashSet<>();
        for (JobKategorie jobKategorie: student.getJobKategorien()){
            JobKategorie gefundenJobKategorie = jobKategorieRepository.findByKategorie(jobKategorie.getKategorie());
            if(gefundenJobKategorie== null){return false;}
             jobKategorieSet.add(gefundenJobKategorie);
        }
        student.setJobKategorien(jobKategorieSet);//ersetze string to id
        return true;
    }
    public boolean isValidBranche(Student student){
        Set<Branche> brancheSet = new HashSet<>();
        for (Branche branche: student.getBranchen()){
            Branche gefundenBranche = brancheRepository.findByName(branche.getName());
            if(gefundenBranche== null){return false;}
            brancheSet.add(gefundenBranche);
        }
        student.setBranchen(brancheSet);//ersetze string to id
        return true;
    }
    public boolean isValidBerufsFeld(Student student){

        Set<BerufsFelder> berufsFelderSet = new HashSet<>();
        for (BerufsFelder berufsFelder: student.getBerufsFelder()){
            BerufsFelder gefundenBerufsFelder = berufsFeldRepository.findByName(berufsFelder.getName());
            if(gefundenBerufsFelder== null){return false;}
            berufsFelderSet.add(gefundenBerufsFelder);
        }
        student.setBerufsFelder(berufsFelderSet);//ersetze string to id
        return true;
    }
    public boolean isValidOrt(Student student){
        Set<Ort> ortSet = new HashSet<>();
        for (Ort ort: student.getOrte()){
            Ort gefundenOrte = ortRepository.findByPLZAndOrt(ort.getPLZ(), ort.getOrt());
            if(gefundenOrte== null){return false;}
            ortSet.add(gefundenOrte);
        }
        student.setOrte(ortSet);//ersetze string to id
        return true;
    }


}
