package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StudienfachService {
    private final StudienfachRepository studienfachRepository;

    @Autowired
    public StudienfachService(StudienfachRepository studienfachRepository) {
        this.studienfachRepository = studienfachRepository;
    }

    public Studienfach saveStudienfach(Studienfach studienfach) {
        return studienfachRepository.save(studienfach);
    }

    /** Gibt eine sortierte Liste aller Studienfächer zurück.
     * Sortiert wird nach Fach und Abschluss (falls Fach gleich)
     * @return Liste aller Studienfächer
     */
    public List<Studienfach> getAlleStudienfaecher() {
        List<Studienfach> studienfaecher = studienfachRepository.findAll();
        studienfaecher.sort(Comparator.comparing(Studienfach::getFach).thenComparing(Studienfach::getAbschluss));
        return studienfaecher;
    }
}
