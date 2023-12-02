package de.hbrs.easyjob.service.Spezifikation;

import de.hbrs.easyjob.entities.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JobSpezifikation {

    /**
     * Erstellt eine Spezifikation für die Filterung von Jobs basierend auf einen Ort.
     * @param orte
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die Jobsuche verwendet wird.
     */
    public static Specification<Job> inOrte(Set<Ort> orte) {
        return (root, query, criteriaBuilder) -> {
            if (orte == null || orte.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (Ort ort : orte) {
                predicates.add(criteriaBuilder.equal(root.get("ort"), ort));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Erstellt eine Spezifikation für die Filterung von Jobs und erzeugt dynamische Suchkriterien, um Jobs zu finden,
     * die einem oder mehreren der angegebenen Studienfächer entsprechen.
     * @param studienfacher Kann leer sein oder eine Menge sein
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die Jobsuche verwendet wird.
     * Gibt alle Jobs zurück, wenn die Menge leer oder null ist.
     */
    public static Specification<Job> inStudienfacher(Set<Studienfach> studienfacher) {
        return (root, query, criteriaBuilder) -> {
            if (studienfacher == null || studienfacher.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (Studienfach fach : studienfacher) {
                predicates.add(criteriaBuilder.isMember(fach, root.get("studienfacher")));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Erstellt eine Spezifikation für die Filterung von Jobs basierend auf eine Jobkategorie.
     * @param kategorien
     * @return Eine Spezifikation Instanz, die als Filterkriterium für die Jobsuche verwendet wird.
     */
    public static Specification<Job> inJobKategorien(Set<JobKategorie> kategorien) {
        return (root, query, criteriaBuilder) -> {
            if (kategorien == null || kategorien.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (JobKategorie kategorie : kategorien) {
                predicates.add(criteriaBuilder.equal(root.get("jobKategorie"), kategorie));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

    }
    public static Specification<Job> isHomeOffice(boolean homeOffice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("homeOffice"), homeOffice);
    }

    public static Specification<Job> inBranchen(Set<Branche> branchen) {
        return (root, query, criteriaBuilder) -> {
            if (branchen == null || branchen.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Join Job mit Unternehmen und dann Unternehmen mit Branche
            Join<Job, Unternehmen> unternehmenJoin = root.join("unternehmen", JoinType.INNER);
            Join<Unternehmen, Branche> branchenJoin = unternehmenJoin.join("branchen", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            for (Branche branche : branchen) {
                predicates.add(criteriaBuilder.equal(branchenJoin.get("id_Branche"), branche.getId_Branche()));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }


}

