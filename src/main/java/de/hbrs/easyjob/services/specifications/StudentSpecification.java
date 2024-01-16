package de.hbrs.easyjob.services.specifications;

import de.hbrs.easyjob.entities.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentSpecification {

    /**
     * Erstellt eine Spezifikation für die Filterung von StudentenProfile basierend auf einen Ort.
     * @param ort
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die Jobsuche verwendet wird.
     */
    public static Specification<Student> inOrt(Ort ort) {
        return (root, query, criteriaBuilder) -> ort == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orte"), ort);
    }

    /**
     * Erstellt eine Spezifikation für die Filterung von StudentenProfile und erzeugt dynamische Suchkriterien, um StudentenProfile zu finden,
     * die einem oder mehreren der angegebenen Studienfächer entsprechen.
     * @param studienfacher Kann leer sein oder eine Menge sein
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die StudentenProfilsuche verwendet wird.
     * Gibt alle StudentenProfile zurück, wenn die Menge leer oder null ist.
     */
    public static Specification<Student> inStudienfacher(Set<Studienfach> studienfacher) {
        return (root, query, criteriaBuilder) -> {
            if (studienfacher == null || studienfacher.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (Studienfach fach : studienfacher) {
                predicates.add(criteriaBuilder.isMember(fach, root.get("studienfach")));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Erstellt eine Spezifikation für die Filterung von StudentenProfile basierend auf eine Jobkategorie.
     * @param kategorie
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die StudentenProfilsuche verwendet wird.
     * Gibt alle StudentenProfile zurück, wenn die Menge leer oder null ist.
     */
    public static Specification<Student> inJobKategorie(Set<JobKategorie> kategorie) {
        return (root, query, criteriaBuilder) -> {
            if (kategorie == null || kategorie.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (JobKategorie jobKategorie : kategorie) {
                predicates.add(criteriaBuilder.isMember(jobKategorie, root.get("jobKategorien")));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}

