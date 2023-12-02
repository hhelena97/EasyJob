package de.hbrs.easyjob.control;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repository.StudienfachRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class ControllerRegS1 {
    private final StudienfachRepository studienfachRepository;

    public ControllerRegS1(StudienfachRepository studienfachRepository) {
        this.studienfachRepository = studienfachRepository;
    }

    public Set<Studienfach> getStudienfachNachAbschluss(String abschluss){
        return studienfachRepository.findAllByAbschluss(abschluss);
    }
}

