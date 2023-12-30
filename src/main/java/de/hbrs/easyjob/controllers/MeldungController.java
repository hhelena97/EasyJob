package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Meldung;
import de.hbrs.easyjob.services.MeldungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meldung")
public class MeldungController {

    private final MeldungService meldungService;

    @Autowired
    public MeldungController(MeldungService meldungService) {
        this.meldungService = meldungService;
    }

    @PostMapping
    public ResponseEntity<Meldung> createMeldung(@RequestBody Meldung meldung) {
        meldung.setBearbeitet(false);
        Meldung savedMeldung = meldungService.saveMeldung(meldung);
        return new ResponseEntity<>(savedMeldung, HttpStatus.CREATED);
    }



}
