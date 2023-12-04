package de.hbrs.easyjob.services;
import de.hbrs.easyjob.entities.Job;
import de.hbrs.easyjob.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobSucheService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public JobSucheService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    @Transactional
    public List<Job> vollTextSuche(String vollText) {
        String query = prepareFullTextSearchQuery(vollText);
        return jobRepository.vollTextSuche(query);
    }
    private String prepareFullTextSearchQuery(String searchText) {
        return Arrays.stream(searchText.split("\\s+"))
                .map(word -> word + ":*")
                .collect(Collectors.joining(" | "));
    }
    @Transactional
    public List<Job> teilZeichenSuche(String teilZeichen) {
        return jobRepository.teilZeichenSuche(teilZeichen);
    }

    public boolean isVollTextSuche(String keyword) {
        // Prüft, ob der Suchbegriff Leerzeichen enthält oder länger als die festgelegte Mindestlänge ist
        return keyword.contains(" ") || keyword.length() >= 4;
    }
}
