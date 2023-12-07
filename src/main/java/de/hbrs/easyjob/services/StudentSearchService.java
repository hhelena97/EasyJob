package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class StudentSearchService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public List<Student> vollTextSuche(String vollText) {
        String query = prepareFullTextSearchQuery(vollText);
        return studentRepository.vollTextSuche(query);
    }

    private String prepareFullTextSearchQuery(String searchText) {
        return Arrays.stream(searchText.split("\\s+"))
                .map(word -> word + ":*")
                .collect(Collectors.joining(" & "));
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
