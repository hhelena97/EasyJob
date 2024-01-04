package de.hbrs.easyjob.controllers;

import de.hbrs.easyjob.entities.*;
import de.hbrs.easyjob.repositories.MeldungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MeldungController {

    private final MeldungRepository meldungRepository;

    @Autowired
    public MeldungController(MeldungRepository meldungRepository) {
        this.meldungRepository = meldungRepository;

    }

    /**
     * zum Speichern von Meldungen
     * Kann eine Person, ein Unternehmen, einen Job oder einen Chat bekommen
     * @param meldung die neue Meldung, die gespeichert werden soll
     * @param person, unternehmen, job oder chat der gemeldet wird
     * @return true, wenn gespeichert
     */
    public boolean saveMeldung(Meldung meldung, Person person) {

        meldung.setPerson(person);
        meldung.setBearbeitet(false);
        meldungRepository.save(meldung);
        Meldung t = meldungRepository.findById_Meldung(meldung.getId_Meldung());
        return !t.isBearbeitet();
    }

    //weitermachen
    public Meldung saveMeldung(Meldung meldung, Unternehmen u) {

        meldung.setUnternehmen(u);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }

    public Meldung saveMeldung(Meldung meldung, Job job) {

        meldung.setJob(job);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }


    public Meldung saveMeldung(Meldung meldung, Chat chat) {

        meldung.setChat(chat);
        meldung.setBearbeitet(false);
        return meldungRepository.save(meldung);
    }


    /**
     * Finde alle gemeldeten Personen
     * @return Liste aller Meldungen zu Personen
     */
    public List<Meldung> getAllGemeldetePersonen(){
        return meldungRepository.findAllPersonen();
    }


    /**
     * Meldung bearbeiten
     */
    public boolean meldungBearbeiten(Meldung meldung) {
        if (meldung == null) {
            return false;
        }
        meldung.setBearbeitet(true);
        meldungRepository.save(meldung);

        return true;
    }

}
