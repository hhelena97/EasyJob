package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.repositories.OrtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrtService {
    private final OrtRepository ortRepository;

    @Autowired
    public OrtService(OrtRepository ortRepository) {
        this.ortRepository = ortRepository;
    }

    public List<Ort> getAlleOrte() {
        return ortRepository.findAll();
    }

    public Ort saveOrt(Ort ort) {
        return ortRepository.save(ort);
    }
}
