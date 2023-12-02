package de.hbrs.easyjob.control;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.service.JobFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/jobsfiltering")
public class JobFilterControl {

/*
    @Autowired
    private JobFilterService jobService;

    @GetMapping("/filter")
    public List<Job> filterJobs(@RequestParam(required = false) Ort ort,
                                @RequestParam(required = false) JobKategorie kategorie,
                                @RequestParam(required = false) Set<Studienfach> studienfacher) {
        return jobService.filterJobs(ort, kategorie, studienfacher);
    }*/
}
