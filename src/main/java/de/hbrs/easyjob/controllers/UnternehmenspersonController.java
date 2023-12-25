package de.hbrs.easyjob.controllers;
import de.hbrs.easyjob.entities.Unternehmensperson;
import de.hbrs.easyjob.services.UnternehmenspersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/unternehmenperson")
public class UnternehmenspersonController {

    private final UnternehmenspersonService unternehmenspersonService;

    @Autowired
    public UnternehmenspersonController(UnternehmenspersonService unternehmenspersonService) {
        this.unternehmenspersonService = unternehmenspersonService;
    }

    @PostMapping
    public ResponseEntity<Unternehmensperson> createOrUpdateUnternehmensperson(@RequestBody Unternehmensperson unternehmensperson) {
        //Account aktivieren
        unternehmensperson.setAktiv(true);
        //Unternehmensperson savedUnternehmensperson = unternehmenspersonService.saveUnternehmensperson(unternehmensperson);
      //  return new ResponseEntity<>(savedUnternehmensperson, HttpStatus.CREATED);
        return null;
    }
}
