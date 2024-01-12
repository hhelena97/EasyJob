package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import org.springframework.web.bind.annotation.RestController;

//der war mal in Testing und ohne den läuft akutell der Admin nicht. Kann wahrscheinlich später weg.
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
