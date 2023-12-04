package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Studienfach;
import de.hbrs.easyjob.repositories.StudienfachRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class RegS1Controller {
    private final StudienfachRepository studienfachRepository;

    public RegS1Controller(StudienfachRepository studienfachRepository) {
        this.studienfachRepository = studienfachRepository;
    }

    public Set<Studienfach> getStudienfachNachAbschluss(String abschluss){
        return studienfachRepository.findAllByAbschluss(abschluss);
    }
}

