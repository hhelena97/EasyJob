package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.services.StudentSearchService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/studentsuche")
public class SearchStudentProfilesController {
    @Autowired
    StudentSearchService studentService;

    /**
     * Durchsucht Studentenprofile basierend auf einem Schlüsselwort. Die Methode zur Suche
     * (Volltext oder Teilzeichenkette) wird automatisch bestimmt.
     *
     * @param searchText Das Schlüsselwort für die Suche.
     * @return Eine Liste von Studenten, die den Suchkriterien entsprechen.
     */
    @GetMapping("/search")
    public List<Student> searchStudent(@RequestParam String searchText) {
        // Entscheiden, welche Suchmethode zu verwenden ist
        if (studentService.istVollTextSuche(searchText)) {
            return studentService.vollTextSuche(searchText);
        } else {
            return studentService.teilZeichenSuche(searchText);
        }
    }

}
