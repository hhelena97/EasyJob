package de.hbrs.easyjob.control;
import de.hbrs.easyjob.entities.JobKategorie;
import de.hbrs.easyjob.service.JobKategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/jobkategorie")
public class JobKategorieController {


    private final JobKategorieService jobKategorieService;

    @Autowired
    public JobKategorieController(JobKategorieService jobKategorieService) {
        this.jobKategorieService = jobKategorieService;
    }

    @PostMapping
    public ResponseEntity<JobKategorie> createJobKategorie(@RequestBody JobKategorie jobKategorie) {
        JobKategorie savedJobKategorie = jobKategorieService.saveJobKategorie(jobKategorie);
        return ResponseEntity.ok(savedJobKategorie);
    }
}
