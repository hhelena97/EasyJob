package de.hbrs.easyjob.controllers;
import de.hbrs.easyjob.entities.Unternehmen;
import de.hbrs.easyjob.services.UnternehmenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/unternehmen")
public class UnternehmenController {

    private final UnternehmenService unternehmenService;

    @Autowired
    public UnternehmenController(UnternehmenService UnternehmenService) {
        this.unternehmenService = UnternehmenService;
    }

    @PostMapping
    public ResponseEntity<Unternehmen> createUnternehmen(@RequestBody Unternehmen unternehmen) {
        //Profil aktivieren
        unternehmen.setAktiv(true);
        Unternehmen savedUnternehmen = unternehmenService.saveUnternehmen(unternehmen);
        return new ResponseEntity<>(savedUnternehmen, HttpStatus.CREATED);

        //TODO: checken, ob das Unternehmen valide ist (siehe Test-Klasse, 2. Test)
    }
}
