package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.StudentenProfilFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/studentfiltering")
public class StudentenprofilFilterController {


    @Autowired
    private StudentenProfilFilterService studentsFilterService;

    @GetMapping("/filter")
    public List<Student> filterJobs(@RequestParam(required = false) Ort ort,
                                    @RequestParam(required = false) Set<JobKategorie> kategorie,
                                    @RequestParam(required = false) Set<Studienfach> studienfacher) {
        return studentsFilterService.filterStudents(ort,kategorie,studienfacher);
    }

}
