package de.hbrs.easyjob.control;

import com.vaadin.flow.component.UI;
import de.hbrs.easyjob.entities.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class StudentProfilController extends PersonProfilController{
    public String getOrte(){
       // Student student;
        //student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        //return student.getOrte();

        String result = "";
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        Set<Ort> s = student.getOrte();
        for (Ort orte: s
        ) {
            if(result == ""){
                result =orte.getOrt();
            }else{
                result = result  + " , "+  orte.getOrt();
            }
        }
        return result;

    }
    public String getStudienfachUndAbschluss(){
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        String ret= student.getStudienfach().getFach();
        ret= ret + ", "+student.getStudienfach().getAbschluss();
        return ret;
    }
    public String getJobKategorie(){
        String result="";
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        Set<JobKategorie> s =   student.getJobKategorien();
        for (JobKategorie job: s
             ) {
            if(result == ""){
                result =job.getKategorie();
            }else{
                result = result  + " , "+  job.getKategorie();
            }

        }
        return result;
    }
    public boolean isHomeOffice(){
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        return student.isHomeOffice();
    }
    public Set<BerufsFelder> getBerufsFelder(){
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        return student.getBerufsFelder();
    }
    public Set<Branche> getBranchen(){
        Student student;
        student = (Student) UI.getCurrent().getSession().getAttribute("current_User");
        return student.getBranchen();
    }

}
