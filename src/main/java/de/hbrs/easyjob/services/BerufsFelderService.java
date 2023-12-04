package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.BerufsFelder;
import de.hbrs.easyjob.repositories.BerufsFelderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BerufsFelderService {

    private final de.hbrs.easyjob.repositories.BerufsFelderRepository berufsFelderRepository;
    @Autowired
    public BerufsFelderService(BerufsFelderRepository berufsFelderRepository) {
        this.berufsFelderRepository = berufsFelderRepository;
    }
    public BerufsFelder saveBerufsFeld(BerufsFelder berufsFeld) {
        return berufsFelderRepository.save(berufsFeld);
    }
}
