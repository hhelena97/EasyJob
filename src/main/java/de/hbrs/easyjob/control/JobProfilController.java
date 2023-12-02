package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Job;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobProfilController {


    public String getTitel(Job job){
    return job.getTitel();
    }
    public String getFreitext(Job job){
        return job.getFreitext();
    }

    public String getJobKategorie(Job job){
        return job.getJobKategorie().getKategorie();
    }
    public String getOrt(Job job){
        return job.getOrt().getOrt();
    }
}
