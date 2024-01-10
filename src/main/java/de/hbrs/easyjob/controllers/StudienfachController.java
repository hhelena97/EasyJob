package de.hbrs.easyjob.controllers;

import com.vaadin.flow.component.ItemLabelGenerator;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.services.StudienfachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /** Gibt eine sortierte Liste aller Studienfächer zurück.
     * Sortiert wird nach Fach und Abschluss (falls Fach gleich)
     * @return Liste aller Studienfächer
     */
    public List<Studienfach> getAlleStudienfaecher() {
        return studienfachService.getAlleStudienfaecher();
    }

    /** Gibt einen ItemLabelGenerator für die ComboBox zurück, der Fach und Abschluss zurückgibt
     * @return ItemLabelGenerator für die ComboBox, der Fach und Abschluss zurückgibt
     */
    public ItemLabelGenerator<Studienfach> getStudienfachItemLabelGenerator() {
        return studienfach -> studienfach.getFach() + " (" + studienfach.getAbschluss() + ")";
    }
}
