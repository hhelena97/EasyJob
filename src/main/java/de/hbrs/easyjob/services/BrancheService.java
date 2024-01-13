package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.repositories.BrancheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrancheService {

    private final BrancheRepository brancheRepository;
    @Autowired
    public BrancheService(BrancheRepository brancheRepository) {
        this.brancheRepository = brancheRepository;
    }
    public Branche saveBranche(Branche branche) {
        return brancheRepository.save(branche);
    }
}
