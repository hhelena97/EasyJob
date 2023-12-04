package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.Branche;
import de.hbrs.easyjob.entities.Ort;
import de.hbrs.easyjob.repositories.BrancheRepository;
import de.hbrs.easyjob.repositories.OrtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegU1Controller {
    @Autowired
    private OrtRepository ortRepository;
    @Autowired
    private BrancheRepository brancheRepository;
    public List<Branche> getBranche(){
        return brancheRepository.findAll();
    }
    public List<Ort> getOrte(){
        return ortRepository.findAll();
    }
}
