package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.services.StudienfachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/studienfach")
public class StudienfachController {

    private final StudienfachService studienfachService;

    @Autowired
    public StudienfachController(StudienfachService studienfachService) {
        this.studienfachService = studienfachService;
    }

    @PostMapping
    public ResponseEntity<Studienfach> createStudienfach(@RequestBody Studienfach studienfach) {
        Studienfach savedStudienfach = studienfachService.saveStudienfach(studienfach);
        return ResponseEntity.ok(savedStudienfach);
    }
}
