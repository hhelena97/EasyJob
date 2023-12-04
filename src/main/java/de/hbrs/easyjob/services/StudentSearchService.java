package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Service
public class StudentSearchService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public List<Student> vollTextSuche(String vollText) {
        return studentRepository.vollTextSuche(vollText);
    }
    @Transactional
    public List<Student> teilZeichenSuche(String teilZeichen) {

        return studentRepository.teilZeichenSuche(teilZeichen);
    }

    public boolean istVollTextSuche(String keyword) {
        // Prüft, ob der Suchbegriff Leerzeichen enthält oder länger als die festgelegte Mindestlänge ist
        return keyword.contains(" ") || keyword.length() >= 4;
    }

}
