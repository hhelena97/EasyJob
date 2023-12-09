package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.BerufsFelder;
import de.hbrs.easyjob.repositories.BerufsFeldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BerufsFelderService {

    private final BerufsFeldRepository berufsFeldRepository;
    @Autowired
    public BerufsFelderService(BerufsFeldRepository berufsFeldRepository) {
        this.berufsFeldRepository = berufsFeldRepository;
    }
    public BerufsFelder saveBerufsFeld(BerufsFelder berufsFeld) {
        return berufsFeldRepository.save(berufsFeld);
    }
}
