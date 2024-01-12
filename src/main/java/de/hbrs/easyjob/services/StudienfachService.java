package de.hbrs.easyjob.services;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
