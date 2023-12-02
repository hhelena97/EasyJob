package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.service.StudienfachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/studienfach")
public class StudienfachControl {

    private final StudienfachService studienfachService;

    @Autowired
    public StudienfachControl(StudienfachService studienfachService) {
        this.studienfachService = studienfachService;
    }

    @PostMapping
    public ResponseEntity<Studienfach> createStudienfach(@RequestBody Studienfach studienfach) {
        Studienfach savedStudienfach = studienfachService.saveStudienfach(studienfach);
        return ResponseEntity.ok(savedStudienfach);
    }
}
