package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Faehigkeit;
import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.FaehigkeitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FaehigkeitService {

    @Autowired
    private final FaehigkeitRepository faehigkeitRepository;

    public FaehigkeitService(FaehigkeitRepository faehigkeitRepository) {
        this.faehigkeitRepository = faehigkeitRepository;
    }

    public List<Faehigkeit> findAllByKategorie(String kategorie){
        return faehigkeitRepository.findByKategorie(kategorie);
    }

    public Set<Faehigkeit> findFaehigkeitByKategorieForStudent(Student student, String kategorie){
        if (student != null) {
            return student.getFaehigkeiten()
                    .stream()
                    .filter(faehigkeit -> kategorie.equals(faehigkeit.getKategorie()))
                    .collect(Collectors.toSet());
        }
        return null;
    }

    public Faehigkeit findSingleFaehigkeitByKategorieForStudent(Student student, String kategorie){
        if (student != null) {
            return student.getFaehigkeiten()
                    .stream()
                    .filter(f -> kategorie.equals(f.getKategorie()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
