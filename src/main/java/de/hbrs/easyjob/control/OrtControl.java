package de.hbrs.easyjob.control;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.service.OrtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/ort")
public class OrtControl {

    private final OrtService ortService;

    @Autowired
    public OrtControl(OrtService ortService) {
        this.ortService = ortService;
    }

    /** Gibt einen ItemFilter für die ComboBox zurück, der nach Ort und PLZ filtert
     * @return ItemFilter für die ComboBox, der nach Ort und PLZ filtert
     */
    public ComboBox.ItemFilter<Ort> getOrtItemFilter() {
        return (ort, filter) -> ort.getOrt().toLowerCase().contains(filter.toLowerCase()) || ort.getPLZ().toLowerCase().contains(filter.toLowerCase());
    }

    /** Gibt alle Orte zurück, sortiert nach Ort und PLZ (falls Ort gleich)
     * @return Alle Orte, sortiert nach Ort und PLZ (falls Ort gleich)
     */
    public List<Ort> getAlleOrte() {
        List<Ort> orte = ortService.getAlleOrte();
        orte.sort(Comparator.comparing(Ort::getOrt).thenComparing(Ort::getPLZ));
        return orte;
    }

    /** Gibt einen ItemLabelGenerator für die ComboBox zurück, der Ort und PLZ zurückgibt
     * @return ItemLabelGenerator für die ComboBox, der Ort und PLZ zurückgibt
     */
    public ItemLabelGenerator<Ort> getOrtItemLabelGenerator() {
        return ort -> ort.getOrt() + " (" + ort.getPLZ() + ")";
    }

    @PostMapping
    public ResponseEntity<Ort> createOrt(@RequestBody Ort ort) {
        Ort savedOrt = ortService.saveOrt(ort);
        return new ResponseEntity<>(savedOrt, HttpStatus.CREATED);
    }
}
