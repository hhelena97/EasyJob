package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.services.JobSucheService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/jobsuche")
public class JobSucheController {


    @Autowired
    private JobSucheService jobService;

    /**
     * Durchsucht JobsUebersichtView basierend auf einem Schlüsselwort. Die Methode zur Suche
     * (Volltext oder Teilzeichenkette) wird automatisch bestimmt.
     *
     * @param searchText Das Schlüsselwort für die Suche.
     * @return Eine Liste von JobsUebersichtView, die den Suchkriterien entsprechen.
     */
    /*@GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String searchText) {
        // Entscheiden, welche Suchmethode zu verwenden ist
        if (jobService.isVollTextSuche(searchText)) {
            return jobService.vollTextSuche(searchText);
        } else {
            return jobService.teilZeichenSuche(searchText);
        }
    }*/

    //TODO: Test zu Teilzeichensuche grün
    public List<Job> searchJobs(String searchText) {
        // Entscheiden, welche Suchmethode zu verwenden ist
        if (jobService.isVollTextSuche(searchText)) {
            return jobService.vollTextSuche(searchText);
        } else {
            return jobService.teilZeichenSuche(searchText);
        }
    }
}
