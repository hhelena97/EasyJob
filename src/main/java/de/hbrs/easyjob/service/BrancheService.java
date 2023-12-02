package de.hbrs.easyjob.service;

import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.repository.BrancheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrancheService {

    private final de.hbrs.easyjob.repository.BrancheRepository brancheRepository;
    @Autowired
    public BrancheService(BrancheRepository brancheRepository) {
        this.brancheRepository = brancheRepository;
    }
    public Branche saveBranche(Branche branche) {
        return brancheRepository.save(branche);
    }
}
