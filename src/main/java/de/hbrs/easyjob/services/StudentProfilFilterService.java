package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.StudentRepository;
import de.hbrs.easyjob.services.specifications.StudentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class StudentProfilFilterService {
    @Autowired
    StudentRepository studentRepository;

    /**
     * Filtert Studentenprofile basierend auf den übergebenen Kriterien.
     * <p>
     * Diese Methode ermöglicht es, Profile anhand von spezifischen Kriterien wie Ort, Jobkategorie und Studienfächern zu filtern.
     * Es wird eine dynamische Abfrage erstellt, die alle angegebenen Bedingungen berücksichtigt. Die Methode gibt eine Liste
     * von Studenten zurück, die den angegebenen Kriterien entsprechen. Wenn ein Kriterium null oder leer ist, wird dieses
     * Kriterium beim Filtern ignoriert.
     *
     * @param ort Der Ort, nach dem gefiltert werden soll.
     * @param jobkategorien Die Jobkategorie, nach der gefiltert werden soll.
     * @param studienfaecher Eine Menge von Studienfächern, nach denen gefiltert werden soll.
     * @return Eine Liste von Studenten, die den angegebenen Filterkriterien entsprechen. Die Liste kann leer sein, wenn keine
     *         Studenten gefunden wurden, die den Kriterien entsprechen.
     */

    public List<Student> filterStudents(Ort ort, Set<JobKategorie> jobkategorien, Set<Studienfach> studienfaecher) {
        Specification<Student> spec = Specification.where(StudentSpecification.inOrt(ort))
                .and(StudentSpecification.inJobKategorie(jobkategorien))
                .and(StudentSpecification.inStudienfacher(studienfaecher));
        return studentRepository.findAll(spec);
    }
}
