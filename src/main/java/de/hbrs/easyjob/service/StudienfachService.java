package de.hbrs.easyjob.service;
import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repository.StudienfachRepository;
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
